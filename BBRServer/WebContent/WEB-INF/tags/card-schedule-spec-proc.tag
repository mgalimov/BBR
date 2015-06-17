<%@tag import="java.util.*"%>
<%@tag import="java.text.SimpleDateFormat"%>
<%@tag import="BBRCust.BBRVisitManager"%>
<%@tag import="BBRCust.BBRVisitManager.*"%>
<%@tag import="BBRCust.BBRSpecialistManager"%>
<%@tag import="BBRCust.BBRSpecialist"%>
<%@tag import="BBR.BBRDataSet"%>

<%@tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	int datesPerPage = 7;

	BBRContext context = BBRContext.getContext(request);
	BBRPoS pos = context.planningVisit.getPos();
	Date dateSelected = new Date();
	SimpleDateFormat df = new SimpleDateFormat("dd MMMM");
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	
	BBRVisitManager vmgr = new BBRVisitManager();
	
	BBRSpecialistManager smgr = new BBRSpecialistManager();
	BBRDataSet<BBRSpecialist> slist = smgr.list("", "spec.name ASC", pos);
	
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
	String specOut = "<tr><td><small><span class='glyphicon glyphicon-user'></span>&nbsp;</small></td></tr>";
	
	if (startWorkHour <= 0)
		startWorkHour = 8;
	
	if (endWorkHour <= startWorkHour)
		endWorkHour = startWorkHour + 9;
	
	if (endWorkHour >= 23)
		endWorkHour = 23;
	
	int hh = startWorkHour;
	String hs;

	schOut += "<thead><tr>";
	if (startWorkMin > 0) {
		if (hh < 10) hs = "0" + hh; else hs = "" + hh;
		schOut += "<th><small>" + hs + ":30</small></th>";
		hh++;
	}
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		if (h < 10) hs = "0" + h; else hs = "" + h;
		schOut += "<th colspan='2'><small>" + hs + ":00</small></th>";
		//schOut += "<th class='col-sm-1'><small>" + hs + ":30</small></th>";
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
		specOut += "<tr><td><small>" + spec.getName() + "</small></td></tr>";
		String sid = spec.getId().toString(); 
		if (startWorkMin > 0) {
			if (hh < 10) hs = "0" + hh; else hs = "" + hh;
			schOut += "<td id='sp"+ sid + "_oc" + hs + "_30'><small>&nbsp;</small></td>";
			hh++;
		}
		for (Integer h = hh; h <= endWorkHour - 1; h++) {
			if (h < 10) hs = "0" + h; else hs = "" + h;
			schOut += "<td id='sp"+ sid + "_oc" + hs + "_00'><small>&nbsp;</small></td>";
			schOut += "<td id='sp"+ sid + "_oc" + hs + "_30'><small>&nbsp;</small></td>";
		}
		if (endWorkMin > 0) {
			hh = endWorkHour;
			if (hh < 10) hs = "0" + hh; else hs = "" + hh;
			schOut += "<td id='sp"+ sid + "_oc" + hs + "_00'><small>&nbsp;</small></td>";
		}
		schOut += "</tr></tbody>";
	}
	
	Map<String, Integer> months = calendar.getDisplayNames(Calendar.MONTH, Calendar.LONG, context.getLocale());
	
	String dtOut = "var months = {};\n";
	for (String month : months.keySet()) {
		dtOut += "months[" + months.get(month) + "] = '" + month + "';\n";
	}
	
	calendar.setTime(dateSelected);
%>
<div class="row">
	<div class="panel col-md-10">
		<t:card-item label="LBL_SELECT_PROCEDURE" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
	</div>
</div>

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
		<nobr>
		<%
			out.print("<button type='button' class='btn btn-info btn-sm' style='width: 90px' id='sd" + sf.format(calendar.getTime()) + "'>" + df.format(calendar.getTime()) + "</button>");
			for (int i = 1; i < datesPerPage; i++) {
				calendar.add(Calendar.DATE, 1);
				out.print("<button type='button' class='btn btn-link btn-sm' style='width: 90px' id='sd" + sf.format(calendar.getTime()) + "'>" + df.format(calendar.getTime()) + "</button>");
			}
		%>
		</nobr>
	</div>
	<div class="panel col-md-1">
		
	</div>
</div>
<div class="row">
	<div class="panel col-md-2" style="overflow:hidden">
		<table class="table table-condensed noselection">
			<tbody>
				<%=specOut %>
			</tbody>
		</table>
	</div>
	<div class="panel col-md-8" style="overflow-x: auto">
		<table class="table table-hover table-condensed table-bordered noselection" id="scheduleTable">
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
	 	$("#scheduleTable td").on("click", function(e) {setTime($(e.target));});
	 	
	 	$("#nextDateBtn").click(function(e) { changeDatesOnButtons(<%=datesPerPage %>); });
	 	$("#prevDateBtn").click(function(e) { changeDatesOnButtons(-<%=datesPerPage %>); });
	 	
	 	select();
	 });
	
	function changeDatesOnButtons(modifier) {
		$("button[id^='sd']").each(function (i) {
			dt = new Date();
 			if (modifier != 0) 
 				dt.setTime(Date.parse($(this).attr('id').substring(2, 12)));
 			dt.setDate(dt.getDate() + modifier);
 			$(this).attr("id", "sd" + dt.getFullYear() + "-" + (dt.getMonth() + 1) + "-" + dt.getDate());
 			$(this).text(dt.getDate() + " " + months[dt.getMonth()]);
 		}); 
	}
	
	function select() {
 		dateSelected = $("button[id^='sd'].btn-info").attr('id').substring(2, 12);
 		procSelected = $("#procedureinput").val();
 		
 		$("#timeScheduledinput").val(dateSelected + " ");
 		
		$.get('BBRSchedule', {
				date: dateSelected,
				proc: procSelected
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
				
				for (i = 0; i < 47; i++)
					for (j = 0; j < specs.length; j++)
						sch[j][i] = 0; 

				$("td.info").removeClass('info');
				
				for (i = 0; i < arr.length; i++) {
					for (m = arr[i][0]; m < arr[i][0] + arr[i][2]; m++)
						sch[spc[arr[i][1]]][m] = 1;
				}
				
				for (i = 0; i <= 23; i++)
					for (j = 0; j < specs.length; j++) {
						if (sch[j][i*2] > 0) {
							e = $("#sp"+specs[j][0]+"_oc"+i+"_00");
							if (e.length > 0) e.addClass('info');
						}
						if (sch[j][i*2 + 1] > 0) {
							e = $("#sp"+specs[j][0]+"_oc"+i+"_30");
							if (e.length > 0) e.addClass('info');			
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

 		if (!obj.hasClass('info')) {
 	 		$("td.success").removeClass('success');
 	 		$("td.danger").addClass('success');
 	 		$("td.danger").removeClass('danger');
 	 		obj.addClass('success');
 	 		
 	 		for (i = 2; i <= procLength; i++) {
 	 			obj = obj.next();
 	 	 		if (!obj.hasClass('info')) {
 	 	 	 		obj.addClass('success');
 	 	 		} else
 	 	 		{
 	 	 	 		obj.addClass('danger');
 	 	 		}
 	 		}
		}
 		dateSelected = $("button[id^='sd'].btn-info").attr('id').substring(2, 12);
 		dtString = dateSelected + " " + timeSelected;
 	 	$("#timeScheduledinput").val(dtString);
 	 	$("#specinput").val(specSelected);
	}
 	
</script>
