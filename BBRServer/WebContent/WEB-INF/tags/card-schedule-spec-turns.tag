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

<%@ attribute name="posId" %>

<%@tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	int datesPerPage = 7;

	BBRContext context = BBRContext.getContext(request);
	BBRPoSManager pmgr = new BBRPoSManager(); 
	BBRPoS pos = null;
	try {
		if (posId != null && posId != "")
			pos = pmgr.findById(Long.parseLong(posId));
	} catch (Exception ex) {
	}

	if (pos == null)
		return;
	
	Date dateSelected = new Date();

	SimpleDateFormat df = new SimpleDateFormat("dd MMMM");
	SimpleDateFormat sf = new SimpleDateFormat(BBRUtil.fullDateFormat);
	
	BBRSpecialistManager smgr = new BBRSpecialistManager();
	BBRDataSet<BBRSpecialist> slist = smgr.listAvailableSpecialists(pos);
	
	Calendar calendar = Calendar.getInstance(context.getLocale());
	calendar.setTime(dateSelected);
	
	String schOut = "";
	String specOut = "<td style='width:100px;' nowrap='nowrap'>" +
					 "  	<div class='btn-group btn-group-justified' role='group'>" +
					 "			<button class='btn btn-link' id='prevDateBtn' type='button'><span class='glyphicon glyphicon-chevron-left'></span></button>" +
					 "			<button class='btn btn-link' id='todayDateBtn' type='button'><span class='glyphicon glyphicon-time'></span></button>" +
					 "			<button class='btn btn-link' id='nextDateBtn' type='button'><span class='glyphicon glyphicon-chevron-right'></span></button>" +
					 "		</div>" +
					 "</td>";
	
	schOut += "<thead><tr >" + specOut;
	for (int i = 0; i < datesPerPage; i++) {
		calendar.add(Calendar.DATE, 1);
		schOut += "<th style='width: 90px; vertical-align: middle;' class='text-center' ><nobr><span id='sd" + i + "' data-date='" + sf.format(calendar.getTime()) + "'></span></nobr></th>";
	}
	schOut += "</tr></thead><tbody>";

	for (BBRSpecialist spec : slist.data) {
		schOut += "<tr>";
		schOut += "<td><a href='manager-spec-edit.jsp?id=" + spec.getId() + "'>" + spec.getName() + ", " + spec.getPosition() + "</a></td>";
		String sid = spec.getId().toString(); 
		calendar.setTime(dateSelected);
		for (int i = 1; i <= datesPerPage; i++) {
			schOut += "<td id='sp"+ sid + "_" + i + "' data-spec='"+sid+"' data-date='"+sf.format(calendar.getTime()) + "' class='text-center nobr' nowrap='nowrap'></td>";
			calendar.add(Calendar.DATE, 1);
		}
		schOut += "</tr>";
	}
	
	schOut += "</tbody>";
	
	Map<String, Integer> months = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, context.getLocale());
%>

<form class="form-inline">
	<div class="form-group col-md-4 col-sm-4" style="margin-bottom: 0; vertical-align: middle;">
		<div class='input-group date' id='datepicker' style='width:100%; margin-top: 4px;'>
	       	<input type='text' class="form-control" />
	  			<span class="input-group-addon">
				<span class="glyphicon glyphicon-calendar"></span>
			</span>
	      </div>
	</div>

	<t:select-shop-pos field="shoppos" />
</form>

<div class="table-responsive col-md-10">
	<table class="table table-bordered  table-condensed noselection table-striped small" id="scheduleTable">
		<%=schOut %>
	</table>
</div>

<script>
	var letChangeButtons = true;

	$(document).ready(function() {
		moment.locale('<%=context.getLocaleString()%>');
		
		$('#datepicker').datetimepicker({
			format: 'YYYY-MM-DD',
			locale: '<%=context.getLocaleString()%>'
        });

		$('#datepicker').on("dp.change", function(e) {
			newDate = e.date;
			$.cookie("dateSelected", newDate.year() + "-" + (newDate.month() + 1) + "-" + newDate.date());
			
			if (letChangeButtons) {
				$("span[id^='sd']").each(function (i) {
					dt = new moment(newDate);
	 				dt.add(i, "days");
	 				var nd = dt.year()+ "-" + (dt.month() + 1) + "-" + dt.date();
	 	 			var od = $(this).attr("data-date");
	 	 			$("td[id^='sp'][id$='_" + (i + 1) + "']").attr("data-date", nd);
		 			$(this).attr("data-date",  nd)
		 			$(this).text(dt.date() + " " + moment.months()[dt.month()]);
		 		});
				updateTurns();
			}
			letChangeButtons = true;
		});
	
	 	$("#nextDateBtn").click(function(e) { changeDatesOnButtons(<%=datesPerPage-datesPerPage+1 %>); });
	 	$("#prevDateBtn").click(function(e) { changeDatesOnButtons(-<%=datesPerPage-datesPerPage+1 %>); });
	 	$("#todayDateBtn").click(function(e) { changeDatesOnButtons(0); });
	 	
	 	ds = $.cookie("dateSelected");
		if (ds != null && ds != "")
			$('#datepicker').data("DateTimePicker").date(ds);
		else
			changeDatesOnButtons(0);
	 });
	
	function changeDatesOnButtons(modifier) {
		var dt;
		if (modifier != 0)
			dt = new moment($("span[id='sd0']").attr('data-date'));
		else
			dt = new moment(new Date());
		
		$("span[id^='sd']").each(function (i) {
			var ndt = moment(dt);
			ndt.add(modifier + i, "days");
 			var nd = ndt.year() + "-" + (ndt.month() + 1) + "-" + ndt.date();
 			var od = $(this).attr("data-date");
 			$("td[id^='sp'][id$='_" + (i + 1) + "']").attr("data-date", nd);
 			$(this).attr("data-date", nd);
 			$(this).text(ndt.date() + " " + moment.months()[ndt.month()]);
 		}); 
		
		letChangeButtons = false;
		dt = $("span[id='sd0']").attr('data-date');
		$('#datepicker').data('DateTimePicker').date(dt);
		
		updateTurns();
		
		letChangeButtons = true;
	}
	
	function updateTurns() {
		var sd = $("span[id='sd0']").attr('data-date');
		var ed = moment(sd).add(<%=datesPerPage%>-1, 'days');
		$.ajax({
			url: 'BBRTurns',
			method: 'get',
			data: {
				operation: 'getTurns',
				posId: <%=posId%>,
				startDate: sd,
				endDate: ed.year() + "-" + (ed.month() + 1) + "-" + ed.date()
			}
		}).done(function (data) {
			if (data == "")
				return;
			turns = $.parseJSON(data);
			
			$("td[id^='sp']").html("<a href='#' class='btn btn-default btn-xs' data-op='add'>+</a>");
			
			for (i = 0; i < turns.data.length; i++) {
				turn = turns.data[i];
				$("td[id^='sp" + turn.specialist.id + "'][data-date='" + turn.date + "']").html("<a href='#' class='btn btn-link btn-xs' data-op='edit' data-id='"+turn.id+"'>"+turn.startTime+"&ndash;"+turn.endTime+"</a><a href='#' class='btn btn-warning btn-xs' data-op='del' data-id='"+turn.id+"'>x</a>");
			}
			
			$("td[id^='sp'] a").click(function () {
				var op = $(this).attr("data-op");
				var el = $(this).parent();
				
				var d = el.attr("data-date");
				var s = el.attr("data-spec");
				if (op == "add") {
					$.get('BBRTurns', {
					     specialist : s,
					     date : d,
					     operation : 'create'
					  }).done(function() {
						  updateTurns();
					  })					
				}
				if (op == "edit") {
					var id = $(this).attr("data-id");
					window.location.href = "manager-turn-edit.jsp?id=" + id;					
				}
				if (op == "del") {
					var id = $(this).attr("data-id");
					$.get('BBRTurns', {
						     id : id,
						     operation : 'delete'
						  }).done(function() {
							  updateTurns();
						  })
				}

			});
		});
		
		$shopposfirstLoad = true;
		var el = $("#shopposinput")[0].selectize;
		el.load(shopposLoadData);
		el.on("load", function () {
			if ($shopposfirstLoad) {
				var el = $("#shopposinput")[0].selectize;
				var firstOptionIndex = "${posId}";
				if (firstOptionIndex == "") {
					firstOptionIndex = Object.keys(el.options)[0];
					for (i = 0; i < Object.keys(el.options).length; i++) {
						var s = Object.keys(el.options)[i];
						if (s.charAt(0) == "s") {
							firstOptionIndex = Object.keys(el.options)[i];
							break;
						}
					}
				}
	    		el.addItem(el.options[firstOptionIndex].id);
	    		el.refreshItems();
	    		$shopposfirstLoad = false;
			}
		});
		el.on("change", function () {
			if ($shopposfirstLoad)
				return;
			var posId = $("#shopposinput").val();
			if (posId.charAt(0) == "s")
				return;
			reloadWithNewParam("posId=" + posId);
		})
	}
</script>
