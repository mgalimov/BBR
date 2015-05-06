<%@ tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	BBRContext context = BBRContext.getContext(request);
	BBRPoS pos = context.planningVisit.getPos();
	Calendar calendar = Calendar.getInstance();
	
	if (pos.getStartWorkHour() != null)
		calendar.setTime(pos.getStartWorkHour());
	int startWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int startWorkMin = calendar.get(Calendar.MINUTE);

	if (pos.getEndWorkHour() != null)
		calendar.setTime(pos.getEndWorkHour());
	int endWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int endWorkMin = calendar.get(Calendar.MINUTE);
	
	String schOut = "";
	
	if (startWorkHour <= 0)
		startWorkHour = 8;
	
	if (endWorkHour <= startWorkHour)
		endWorkHour = startWorkHour + 9;
	
	if (endWorkHour >= 23)
		endWorkHour = 23;
	
	int hh = startWorkHour;
	
	if (startWorkMin > 0) {
		schOut += "<tr><td class='col-md-1'>" + hh + ":30</td><td class='col-md-3' id='oc" + hh + "30'></tr>";
		hh++;
	}
		
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		schOut += "<tr><td class='col-md-1'>" + h + ":00</td><td class='col-md-3' id='oc" + h + "00'></tr>";
		schOut += "<tr><td class='col-md-1'>" + h + ":30</td><td class='col-md-3' id='oc" + h + "30'></tr>";
	}

	if (endWorkMin > 0) {
		hh = endWorkHour;
		schOut += "<tr><td class='col-md-1'>" + hh + ":00</td><td class='col-md-3' id='oc" + hh + "00'></tr>";
	}
%>

<h4 id="dateInfo">Set date and time</h4>

<div class="panel col-md-4">
	<div id="dateinput" class="panel"></div>
	<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
	<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
	<t:card-item label="" type="text" field="timeScheduled" isHidden="hidden"/>
	<div id="summary"></div>
</div>
<div class="panel col-md-8" style="min-height: 100px; max-height: 300px; overflow-y: scroll;">
	<table class="table table-striped table-hover" id="scheduleTable">
		<tbody>
			<%=schOut %>
		</tbody>
	</table>
</div>

<script>
	var timeSelected = "12:00";
	var procLength = 1;

	function select() {
 		dateSelected = $("#dateinput").val();
 		specSelected = $("#specinput").val();
 		procSelected = $("#procedureinput").val();
 		
 		$("#timeScheduled").val(dateSelected + " ");
 		
		$.get('BBRSchedule', {
				date: dateSelected,
				spec: specSelected,
				proc: procSelected
			}, 
			function (responseText) {
				obj = $.parseJSON(responseText);
				arr = obj.list;
				specCount = obj.specCount;
				procLength = obj.procLength;
				
				var sch = new Array(47);

				for (i = 0; i <= 47; i++)
					sch[i] = 0; 
				
				for (i = 0; i < arr.length; i++) {
					for (m = arr[i][0]; m < arr[i][0] + arr[i][1]; m++)
						sch[m]++;
				}
				
				$("td.info").removeClass('info');
				for (i = 0; i <= 23; i++) {
					if (sch[i*2] >= specCount)
						if ($("#oc"+i+"00").length > 0)
							$("#oc"+i+"00").addClass('info');
					if (sch[i*2 + 1] >= specCount) 
						if ($("#oc"+i+"30").length > 0)
							$("#oc"+i+"30").addClass('info');			
				}
				
				setTime(null);
			});
	}

	function setTime(trObj) {
		if (trObj == null) {
			trObj = $("#oc"+timeSelected.replace(":", "")).parent();
		}
		obj1 = $(trObj).children().first();
		obj2 = $(obj1).next();
 		if (!$(obj2).hasClass('info')) {
 	 		$("td.success").removeClass('success');
 	 		$("td.danger").addClass('success');
 	 		$("td.danger").removeClass('danger');
 	 		$(obj2).addClass('success');
 	 		timeSelected = $(obj1).text();
 	 		
 	 		for (i = 2; i <= procLength; i++) {
 	 			trObj = $(trObj).next();
 	 			obj1 = $(trObj).children().first();
 	 			obj2 = $(obj1).next();
 	 	 		if (!$(obj2).hasClass('info')) {
 	 	 	 		$(obj2).addClass('success');
 	 	 		} else
 	 	 		{
 	 	 	 		$(obj2).addClass('danger');
 	 	 		}
 	 		}
		}
 		dateSelected = $("#dateinput").val();
 		dtString = dateSelected + " " + timeSelected;
 	 	$("#timeScheduledinput").val(dtString);
 		$("#dateInfo").text(dtString);
	}
	
 	$("#dateinput").datepicker({onSelect: select});
 	$("#specinput").on("change", select);
 	$("#procedureinput").on("change", select);
 	$("#scheduleTable td").on("click", function(e) {setTime($(e.target).parent());}); 	
</script>
