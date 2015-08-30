<%@tag import="BBRAcc.BBRPoSManager"%>
<%@tag import="java.util.*"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="BBRCust.BBRVisitManager"%>
<%@tag import="BBRCust.BBRVisitManager.*"%>
<%@tag import="BBRCust.BBRSpecialistManager"%>
<%@tag import="BBRCust.BBRSpecialist"%>
<%@tag import="BBRAcc.BBRUser.BBRUserRole"%>
<%@tag import="BBR.BBRDataSet"%>
<%@tag import="BBR.BBRUtil"%>

<%@ attribute name="mode" %>
<%@ attribute name="posId" %>

<%@tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	int datesPerPage = 7;

	BBRContext context = BBRContext.getContext(request);
	
	BBRPoS pos = null;
	if (mode == null)
		mode = "";
	
	if (mode.isEmpty() || mode.equals("general-edit"))	
		pos = context.planningVisit.getPos();
	else
		if (mode.equals("manager-view") || mode.equals("manager-edit"))	{
			BBRPoSManager pmgr = new BBRPoSManager();
			
			if (posId != null && !posId.isEmpty())
				pos = pmgr.findById(Long.parseLong(posId));
					
			if (pos == null) {
				if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN ||
					context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
					pos = context.user.getPos();
				else  
					if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) {
						pos = pmgr.list("", "title asc", pmgr.whereShop(context.user.getShop().getId())).data.get(0);
					}
					else
						if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER) {
							pos = pmgr.list("", "title asc", "").data.get(0);
						}
			}
		}
	if (pos == null) return;
	
	Date dateSelected = new Date();

	SimpleDateFormat df = new SimpleDateFormat("dd MMMM");
	SimpleDateFormat sf = new SimpleDateFormat(BBRUtil.fullDateTimeFormat);
	
	BBRVisitManager vmgr = new BBRVisitManager();
	
	BBRSpecialistManager smgr = new BBRSpecialistManager();
	BBRDataSet<BBRSpecialist> slist = smgr.listAvailableSpecialists(pos);
	
	Calendar calendar = Calendar.getInstance(context.getLocale());
	
	if (pos.getStartWorkHour() != null)
		calendar.setTime(pos.getStartWorkHour());
	int startWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int startWorkMin = calendar.get(Calendar.MINUTE);

	if (pos.getEndWorkHour() != null)
		calendar.setTime(pos.getEndWorkHour());
	int endWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int endWorkMin = calendar.get(Calendar.MINUTE);
	
	String schOut = "";
	String specOut = "<td><small><span class='glyphicon glyphicon-user'></span>&nbsp;</small></td>";
	
	if (startWorkHour <= 0)
		startWorkHour = 8;
	
	if (endWorkHour <= startWorkHour)
		endWorkHour = startWorkHour + 9;
	
	if (endWorkHour >= 23)
		endWorkHour = 23;
	
	int hh = startWorkHour;
	String hs;

	schOut += "<thead><tr>" + specOut;
	if (startWorkMin > 0) {
		if (hh < 10) hs = "0" + hh; else hs = "" + hh;
		schOut += "<th><small>" + hs + ":30</small></th>";
		hh++;
	}
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		if (h < 10) hs = "0" + h; else hs = "" + h;
		schOut += "<th colspan='2'><small>" + hs + ":00</small></th>";
	}
	if (endWorkMin > 0) {
		hh = endWorkHour;
		if (hh < 10) hs = "0" + hh; else hs = "" + hh;
		schOut += "<th><small>" + hs + ":30</small></th>";
	}
	schOut += "</tr></thead><tbody>";

	for (BBRSpecialist spec : slist.data) {
		hh = startWorkHour;
		schOut += "<tr>";
		schOut += "<td><small>" + spec.getName() + "</small></td>";
		String sid = spec.getId().toString(); 
		if (startWorkMin > 0) {
			schOut += "<td id='sp"+ sid + "_oc" + hh + "_30'><small>&nbsp;</small></td>";
			hh++;
		}
		for (Integer h = hh; h <= endWorkHour - 1; h++) {
			schOut += "<td id='sp"+ sid + "_oc" + h + "_00'><small>&nbsp;</small></td>";
			schOut += "<td id='sp"+ sid + "_oc" + h + "_30'><small>&nbsp;</small></td>";
		}
		if (endWorkMin > 0) {
			hh = endWorkHour;
			schOut += "<td id='sp"+ sid + "_oc" + hh + "_00'><small>&nbsp;</small></td>";
		}
		schOut += "</tr>";
	}
	
	schOut += "</tbody>";
	
	Map<String, Integer> months = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, context.getLocale());
	
	calendar.setTime(dateSelected);
%>

<% if (mode.equals("manager-edit") || mode.equals("manager-view")) { %>
	<div class="row">
		<div class="col-md-10">
			<t:card-item label="LBL_SELECT_POS" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes"></t:card-item>
		</div>
	</div>
<% } %>
	
<% if (mode.isEmpty() || mode.equals("general-edit") || mode.equals("manager-edit")) { %>	
	<div class="row">
		<div class="col-md-10">
			<t:card-item label="LBL_SELECT_PROCEDURE" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
		</div>
	</div>
<% } %>

<div class="row">
	<div class="form-group col-md-12 col-sm-12 col-xs-12">
		<label>${context.gs('LBL_SET_DATE_TIME_TITLE')}</label>
	</div>

	<div class="form-group col-md-2 col-sm-3">
		<div class='input-group date' id='datepicker'>
        	<input type='text' class="form-control" />
   			<span class="input-group-addon">
				<span class="glyphicon glyphicon-calendar"></span>
			</span>
       </div>
	</div>
	
	<% if (mode.equals("manager-view") || mode.equals("manager-edit")) { %>
	<div class="btn-group" role="group" aria-label="...">
		<button class='btn btn-default' id='openVisits' type="button">
		<span class="glyphicon glyphicon-calendar" aria-hidden="true"></span> <%=context.gs("LBL_OPEN_VISITS_BTN") %>
		</button>
		<button class='btn btn-default' id='openAllUnapprovedVisits' type="button">
		<span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span> <%=context.gs("LBL_OPEN_ALL_VISITS_BTN") %>
		</button>
	</div>
			
		<script>
			$(document).ready(function() {
				$("#openVisits").click(function(){
					dt = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
					pos = $("#posinput").val();
					window.location.href = "manager-visit-list.jsp?t=datepos&query="+dt+"@@"+pos; 
				});
			});

			$(document).ready(function() {
				$("#openAllUnapprovedVisits").click(function(){
					dt = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
					pos = $("#posinput").val();
					window.location.href = "manager-visit-list.jsp?t=unapproved&query="+pos; 
				});
			});
		</script>
	<% } %>
</div>

<div class="row">
	<div class="col-md-2 col-sm-2 col-xs-2 hidden-sm hidden-xs">
		<button class='btn btn-link' id='prevDateBtn' type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
		<button class='btn btn-link' id='todayDateBtn' type="button"><span class="glyphicon glyphicon-time"></span></button>
		<button class='btn btn-link' id='nextDateBtn' type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>

	</div>
	<div class="col-md-8 col-sm-8 col-xs-8 hidden-sm hidden-xs" >
		<div class="btn-group btn-group-justified" role="group">
		<%
			out.println("<a href='#' role='button' class='btn btn-info btn-sm' id='sd" + sf.format(calendar.getTime()) + "'></a>");
			for (int i = 1; i < datesPerPage; i++) {
				calendar.add(Calendar.DATE, 1);
				out.println("<a href='#' role='button' class='btn btn-default btn-sm' id='sd" + sf.format(calendar.getTime()) + "'></a>");
			}
		%>
		</div>
	</div>
</div>
<div class="row">
	<div class="panel col-md-10" style="overflow-x: auto">
		<table class="table table-condensed table-bordered noselection" id="scheduleTable">
			<%=schOut %>
		</table>
	</div>
</div>	
	
<% if (mode.equals("manager-edit")) { %>
<div class="row">
	<div class="col-md-10">
		<t:card-item label="LBL_YOUR_NAME" type="text" field="userName" isRequired="required"/>
		<t:card-item label="LBL_YOUR_PHONE" type="text" field="userContacts" isRequired="required" />
	</div>
</div>
<% } %>

<t:card-item label="" type="text" field="timeScheduled" isHidden="hidden"/>
<t:card-item label="" type="text" field="spec" isHidden="hidden"/>
<t:card-item label="" type="text" field="formMode" isHidden="hidden" defaultValue="<%=mode%>"/>

<script>
	var timeSelected = "12:00";
	var specSelected = -1;
	var procLength = 1;
	var letChangeButtons = true;

	$(document).ready(function() {
		moment.locale('<%=context.getLocaleString()%>');
		
		$('#datepicker').datetimepicker({
			format: 'YYYY-MM-DD',
			locale: '<%=context.getLocaleString()%>'
        });

		changeDatesOnButtons(0);
		
		$("a[id^='sd']").click(function(e) {
			$("a[id^='sd']").removeClass('btn-info').addClass('btn-default');
			$(this).removeClass('btn-default').addClass('btn-info');
			dt = $(this).attr('id').substring(2, 12);
			letChangeButtons = false;
			$('#datepicker').data('DateTimePicker').date(dt);

			select();
		});
		
		$('#datepicker').on("dp.change", function(e) {
			newDate = e.date;
			$.cookie("dateSelected", newDate.year() + "-" + (newDate.month() + 1) + "-" + newDate.date());
			
			if (letChangeButtons) {
				$("a[id^='sd']").each(function (i) {
					dt = new moment(newDate);
	 				dt.add(i, "days");
		 			$(this).attr("id", "sd" + dt.year() + "-" + (dt.month() + 1) + "-" + dt.date());
		 			$(this).text(dt.date() + " " + moment.months()[dt.month()]);
		 		}); 
				$("a[id^='sd']").removeClass('btn-info').addClass('btn-default');
				$("a[id^='sd']").first().removeClass('btn-default').addClass('btn-info');
				
				select();
			}
			letChangeButtons = true;
		});
	
	 	$("#procedureinput").on("change", select);
	 	el = $("#posinput");
	 	if (el.length) {
			els=el[0].selectize;
			els.addOption({id: <%=pos.getId()%>, title: '<%=pos.getTitle()%>'});
			els.addItem(<%=pos.getId()%>);
			els.load(posLoadInitialData);
			els.refreshOptions(false);
			els.refreshItems();
	 		el.on("change", function() {
	 			reloadWithNewParam("posId=" + $("#posinput").val());
	 		});
	 	}

<% if (mode.isEmpty() || mode.equals("general-edit") || mode.equals("manager-edit")) { %>	
	 	$("#scheduleTable td").on("click", function(e) {setTime($(e.target));});
<% } %>	 	
	 	$("#nextDateBtn").click(function(e) { changeDatesOnButtons(<%=datesPerPage %>); });
	 	$("#prevDateBtn").click(function(e) { changeDatesOnButtons(-<%=datesPerPage %>); });
	 	$("#todayDateBtn").click(function(e) { changeDatesOnButtons(0); });
	 	
	 	select();
	 	
	 	ds = $.cookie("dateSelected");
		if (ds != null && ds != "")
			$('#datepicker').data("DateTimePicker").date(ds);
	 });
	
	function changeDatesOnButtons(modifier) {
		$("a[id^='sd']").each(function (i) {
			dt = new Date();
 			if (modifier != 0) {
 				dt.setTime(Date.parse($(this).attr('id').substring(2, 12)));
	 			dt.setDate(dt.getDate() + modifier);
 			} else
 				dt.setDate(dt.getDate() + i);
 			$(this).attr("id", "sd" + dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate());
 			$(this).text(dt.getDate() + " " + moment.months()[dt.getMonth()]);
 		}); 
		if (modifier == 0) {
 			$("a[id^='sd']").removeClass('btn-info').addClass('btn-default');
			$("a[id^='sd']").first().removeClass('btn-default').addClass('btn-info');
		}
		
		letChangeButtons = false;
		dt = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
		$('#datepicker').data('DateTimePicker').date(dt);
		
		select();
		letChangeButtons = true;
	}

	function select() {
		dateSelected = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
 		procSelected = $("#procedureinput").val();
 		if ($("#posinput").length) 
 			posSelected = $("#posinput").val();
 		else
 			posSelected = "";
 		
 		$("#timeScheduledinput").val(dateSelected + " ");
 		
		$.get('BBRSchedule', {
				date: dateSelected,
				proc: procSelected,
				pos: posSelected
			}, 
			function (responseText) {
				obj = $.parseJSON(responseText);
				
				if (obj === undefined || obj == null) return;
				
				arr = obj.list;
				specs = obj.specs;
				procLength = obj.procLength;
				
				var spc = new Object();
				var sch = new Array(specs.length);
				var schVis = new Array(specs.length);
				
				for (j = 0; j < specs.length; j++) {
					sch[j] = new Array(47);
					schVis[j] = new Array(47);
					spc[specs[j][0]] = j;
				}
				
				for (i = 0; i <= 47; i++)
					for (j = 0; j < specs.length; j++)
						sch[j][i] = "o"; 

				$("td.occupied").html("<small>&nbsp;</small>")
					.removeClass('occupied')
					.removeClass("start-cell")
					.removeClass("end-cell")
					.removeClass("middle-cell")
					.removeClass("single-cell");
				$("td.order").html("<small>&nbsp;</small>")
					.removeClass('order')
					.removeClass("start-cell")
					.removeClass("end-cell")
					.removeClass("middle-cell")
					.removeClass("single-cell");

				for (i = 0; i < specs.length; i++) {
					if (specs[i][3] == true)
						for (m = specs[i][1]; m < specs[i][2]; m++) {
							sch[spc[specs[i][0]]][m] = 0;
						}
				}

				for (i = 0; i < arr.length; i++) {
					start = arr[i][0];
					end = arr[i][0] + arr[i][2] - 1;
					for (m = start; m <= end; m++) {
						specCode = arr[i][1];
						if (specCode !== undefined)
							specIndex = spc[specCode];
						if (specIndex !== undefined) {
							if (m == start && m == end)
								sch[specIndex][m] = "single";
							else if (m == start)
								sch[specIndex][m] = "start";
							else if (m == end)
								sch[specIndex][m] = "end";
							else 
								sch[specIndex][m] = "middle";
							
							schVis[specIndex][m] = i;
						}
					}
				}
				
				<% if (mode.equals("manager-view") || mode.equals("manager-edit")) { %>
				$("td.clickable").off('click').tooltip('destroy').removeClass('clickable');
				<% } %>
				
				for (i = 0; i <= 23; i++)
					for (j = 0; j < specs.length; j++) {
						for (k = 0; k <= 1; k++) {
							schVal = sch[j][i*2 + k];
							if (schVal.length > 0) {
								e = $("#sp"+specs[j][0]+"_oc"+i+"_"+(3*k)+"0");
								if (e.length > 0) {
									if (schVal != "o") {
										//e.addClass('occupied');
										e.addClass('order');
										arrIndex = schVis[j][i*2 + k];
										e.addClass(schVal + "-cell");
										e.html("<div class='"+schVal+"-cell visit-status-"+arr[arrIndex][6]+"'></div>");
									<% if (mode.equals("manager-view") || mode.equals("manager-edit")) { %>
										e.addClass('clickable');
										title = arr[arrIndex][3] + ", " + arr[arrIndex][4];
										e.prop("title", title);
										e.data("visitId", arr[arrIndex][5]);
										e.data("toggle", "tooltip");
										e.tooltip({container: 'small'});
										e.on('click', function(ev) {
											window.location.href = "manager-visit-edit.jsp?id=" + $(this).data("visitId");
										})
									<% } %>
									} else
										e.addClass('occupied');
								}
							}
						}
					}
				setTime(null);
			});
	}
	
	function setTime(obj) {
		if (obj == null) {
			if (specSelected >= 0)
				obj = $("#sp"+specSelected+"_oc"+timeSelected.replace(":", "_"));
		}

		if (!obj) return;
		
		objId = obj.attr('id');
		
		if (!objId) return;
		
 		timeSelected = objId.substring(objId.length - 5, objId.length).replace('_', ':');
 		specSelected = objId.substring(2, objId.length - 8);

 		if (!obj.hasClass('occupied')) {
 	 		$("td.selected").removeClass('selected');
 	 		//$("td.conflict").addClass('selected');
 	 		$("td.conflict").removeClass('conflict');
	 		
 	 		for (i = 1; i <= procLength; i++) {
 	 	 		if (obj.hasClass('occupied') || obj.hasClass('order'))
 	 	 			obj.addClass('conflict');
 	 	 		else
 	 	 			obj.addClass('selected');
 	 			obj = obj.next();
 	 		}
		}
 		dateSelected = $("a[id^='sd'].btn-info").attr('id').substring(2, 12);
 		dtString = dateSelected + " " + timeSelected;
 	 	$("#timeScheduledinput").val(dtString);
 	 	$("#specinput").val(specSelected);
	}

<% if (mode.equals("manager-edit")) { %>	
	procedureSetConstrains = function () {
		return $("#posinput").val();
	}
<%
}
%>	 	

</script>
