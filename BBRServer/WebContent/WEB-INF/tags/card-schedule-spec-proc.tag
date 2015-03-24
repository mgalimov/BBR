<%@ tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@tag import="BBRAcc.BBRPoS"%>
<%@tag import="java.util.Calendar"%>
<%@ attribute name="fieldTime" required="true" %>
<%@ attribute name="fieldSpecialist" required="true" %>
<%@ attribute name="fieldProcedure" required="true" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	BBRContext context = BBRContext.getContext(request);
	BBRPoS pos = context.planningVisit.getPos();
	Calendar calendar = Calendar.getInstance();
	
	calendar.setTime(pos.getStartWorkHour());
	int startWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int startWorkMin = calendar.get(Calendar.MINUTE);

	calendar.setTime(pos.getEndWorkHour());
	int endWorkHour = calendar.get(Calendar.HOUR_OF_DAY);
	int endWorkMin = calendar.get(Calendar.MINUTE);
	
	String schOut = "";
	String arrOut = "var sch = {};";
	
	if (startWorkHour <= 0)
		startWorkHour = 8;
	
	if (endWorkHour <= startWorkHour)
		endWorkHour = startWorkHour + 9;
	
	if (endWorkHour >= 23)
		endWorkHour = 23;
	
	int hh = startWorkHour;
	
	if (startWorkMin > 0) {
		schOut += "<tr id='sc" + hh + "30'><td class='col-md-1'>" + hh + ":30</td><td class='col-md-1' id='oc" + hh + "30'></tr>";
		arrOut += "sch['" + hh + "30'] = 0;";
		hh++;
	}
		
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		schOut += "<tr id='sc" + h + "00'><td class='col-md-1'>" + h + ":00</td><td class='col-md-1' id='oc" + h + "00'></tr>";
		schOut += "<tr id='sc" + h + "30'><td class='col-md-1'>" + h + ":30</td><td class='col-md-1' id='oc" + h + "30'></tr>";
		arrOut += "sch['" + h + "00'] = 0;";
		arrOut += "sch['" + h + "30'] = 0;";
	}

	if (endWorkMin > 0) {
		hh = endWorkHour;
		schOut += "<tr id='sc" + hh + "00'><td class='col-md-1'>" + hh + ":00</td><td class='col-md-1' id='oc" + hh + "00'></tr>";
		arrOut += "sch['" + hh + "00'] = 0;";
	}
%>

<div class="panel col-md-4">
	<div id="dateinput" class="panel"></div>
	<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
	<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
</div>
<div class="panel col-md-8">
	<table class="table table-striped table-hover">
		<tbody>
			<%=schOut %>
		</tbody>
	</table>
</div>

<script>
	function initSch() {
		<%=arrOut %>
	}

	function select(dateSelected, specSelected) {
		$.get('BBRSchedule', {
				date: dateSelected,
				spec: specSelected
			}, 
			function (responseText) {
				obj = $.parseJSON(responseText);
				arr = obj.list;
				specCount = obj.specCount;
				for (var s in sch) {
					sch[s] = 0;
				}
				
				for (i = 0; i < arr.length; i++) {
					sz = Math.round(arr[i][1] * 2);
					sp = arr[i][2];
					for (m = 0; m < sz; m++) {
						
					}
				}
				
				$("td.info").removeClass('info');
				for (var s in sch) {
					if (sch[s] < specCount) {
						$("#oc"+s).addClass('info');
					}
				}

			});
	}

 	$("#dateinput").datepicker({
    	onSelect: 	function(dateSelected) {
    					specSelected = $("#specinput").val();
    					select(dateSelected, specSelected);
    				}
    });
 	
 	$("#specinput").on("change", function(e) {
 		dateSelected = $("#dateinput").val();
 		specSelected = $("#specinput").val();
		select(dateSelected, specSelected);
 	});
</script>

<div class="panel">
	<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
</div>