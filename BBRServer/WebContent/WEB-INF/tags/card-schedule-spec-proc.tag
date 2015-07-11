<%@tag import="BBRAcc.BBRPoSManager"%>
<%@tag import="java.util.*"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="BBRCust.BBRVisitManager"%>
<%@tag import="BBRCust.BBRVisitManager.*"%>
<%@tag import="BBRCust.BBRSpecialistManager"%>
<%@tag import="BBRCust.BBRSpecialist"%>
<%@tag import="BBRAcc.BBRUser.BBRUserRole"%>
<%@tag import="BBR.BBRDataSet"%>

<%@ attribute name="mode" %>

<%@tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	int datesPerPage = 7;

	BBRContext context = BBRContext.getContext(request);
	
	BBRPoS pos = null;
	
	if (mode.isEmpty() || mode.equals("general-edit"))	
		pos = context.planningVisit.getPos();
	else 
		if (mode.equals("manager-view") || mode.equals("manager-edit"))	{
			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN ||
				context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST)
				pos = context.user.getPos();
			else
				if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN) {
					BBRPoSManager pmgr = new BBRPoSManager();
					pos = pmgr.list("", "title asc", pmgr.whereShop(context.user.getShop().getId())).data.get(0);
				}
				else
					if (context.user.getRole() == BBRUserRole.ROLE_BBR_OWNER) {
						BBRPoSManager pmgr = new BBRPoSManager();
						pos = pmgr.list("", "title asc", "").data.get(0);
					}
		}
	
	if (pos == null) return;
	
	Date dateSelected = new Date();
	SimpleDateFormat df = new SimpleDateFormat("dd MMMM");
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
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
	
	String dtOut = "var months = {};\n";
	for (String month : months.keySet()) {
		dtOut += "months[" + months.get(month) + "] = '" + month + "';\n";
	}
	
	calendar.setTime(dateSelected);
%>
<% if (mode.isEmpty() || mode.equals("general-edit")) { %>	
	<div class="row">
		<div class="panel col-md-10">
			<t:card-item label="LBL_SELECT_PROCEDURE" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
		</div>
	</div>
<% } else { %>
	<div class="row">
		<div class="panel col-md-10">
			<t:card-item label="LBL_SELECT_POS" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes"></t:card-item>
		</div>
	</div>
<% } %>

<div class="row">
	<div class="panel col-md-10">
		<label>${context.gs('LBL_SET_DATE_TIME_TITLE')}</label>
	</div>
</div>
<div class="row">
	<div class="panel col-sm-2">
		<button class='btn btn-link' id='prevDateBtn' type="button"><span class="glyphicon glyphicon-chevron-left"></span></button>
		<button class='btn btn-link' id='todayDateBtn' type="button"><span class="glyphicon glyphicon-time"></span></button>
		<button class='btn btn-link' id='nextDateBtn' type="button"><span class="glyphicon glyphicon-chevron-right"></span></button>
	</div>
	<div class="panel col-md-8" >
		<%
			out.print("<button type='button' class='btn btn-info btn-sm' id='sd" + sf.format(calendar.getTime()) + "'>" + df.format(calendar.getTime()) + "</button>");
			for (int i = 1; i < datesPerPage; i++) {
				calendar.add(Calendar.DATE, 1);
				out.print("<button type='button' class='btn btn-link btn-sm' id='sd" + sf.format(calendar.getTime()) + "'>" + df.format(calendar.getTime()) + "</button>");
			}
		%>
	</div>
	<div class="panel col-md-1">
		
	</div>
</div>
<div class="row">
	<div class="panel col-md-10" style="overflow-x: auto">
		<table class="table table-condensed table-bordered noselection" id="scheduleTable">
			<%=schOut %>
		</table>
	</div>
</div>	
	
<t:card-item label="" type="text" field="timeScheduled" isHidden="hidden"/>
<t:card-item label="" type="text" field="spec" isHidden="hidden"/>

<script>
	var timeSelected = "12:00";
	var specSelected = -1;
	var procLength = 1;
	<%=dtOut %>

	$(document).ready(function() {
		$("button[id^='sd']").click(function(e) {
			$("button[id^='sd']").removeClass('btn-info').addClass('btn-link');
			$(this).removeClass('btn-link').addClass('btn-info');
			select();
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
	 		el.on("change", select);
	 	}

<% if (mode.isEmpty() || mode.equals("general-edit")) { %>	
	 	$("#scheduleTable td").on("click", function(e) {setTime($(e.target));});
<% } %>	 	
	 	$("#nextDateBtn").click(function(e) { changeDatesOnButtons(<%=datesPerPage %>); });
	 	$("#prevDateBtn").click(function(e) { changeDatesOnButtons(-<%=datesPerPage %>); });
	 	$("#todayDateBtn").click(function(e) { changeDatesOnButtons(0); });
	 	
	 	select();
	 });
	
	function changeDatesOnButtons(modifier) {
		$("button[id^='sd']").each(function (i) {
			dt = new Date();
 			if (modifier != 0) {
 				dt.setTime(Date.parse($(this).attr('id').substring(2, 12)));
	 			dt.setDate(dt.getDate() + modifier);
 			} else
 				dt.setDate(dt.getDate() + i);
 			$(this).attr("id", "sd" + dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate());
 			$(this).text(dt.getDate() + " " + months[dt.getMonth()]);
 		}); 
		select();
	}

	function select() {
 		dateSelected = $("button[id^='sd'].btn-info").attr('id').substring(2, 12);
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
				arr = obj.list;
				specs = obj.specs;
				procLength = obj.procLength;
				
				var spc = new Object();
				
				var sch = new Array(specs.length);
				for (j = 0; j < specs.length; j++) {
					sch[j] = new Array(47);
					spc[specs[j][0]] = j;
				}
				
				for (i = 0; i <= 47; i++)
					for (j = 0; j < specs.length; j++)
						sch[j][i] = 2; 

				$("td.occupied").removeClass('occupied');
				$("td.order").removeClass('order');

				for (i = 0; i < specs.length; i++) {
					if (specs[i][3] == true)
						for (m = specs[i][1]; m < specs[i][2]; m++)
							sch[spc[specs[i][0]]][m] = 0;
				}

				for (i = 0; i < arr.length; i++) {
					for (m = arr[i][0]; m < arr[i][0] + arr[i][2]; m++)
						if (arr[i][1])
							sch[spc[arr[i][1]]][m] = 1;
				}
				
				for (i = 0; i <= 23; i++)
					for (j = 0; j < specs.length; j++) {
						if (sch[j][i*2] > 0) {
							e = $("#sp"+specs[j][0]+"_oc"+i+"_00");
							if (e.length > 0) {
								e.addClass('occupied');
								if (sch[j][i*2] == 1)
									e.addClass('order');
							}
							
						}
						if (sch[j][i*2 + 1] > 0) {
							e = $("#sp"+specs[j][0]+"_oc"+i+"_30");
							if (e.length > 0) {
								e.addClass('occupied');
								if (sch[j][i*2 + 1] == 1)
									e.addClass('order');
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
 		dateSelected = $("button[id^='sd'].btn-info").attr('id').substring(2, 12);
 		dtString = dateSelected + " " + timeSelected;
 	 	$("#timeScheduledinput").val(dtString);
 	 	$("#specinput").val(specSelected);
	}
 	
</script>
