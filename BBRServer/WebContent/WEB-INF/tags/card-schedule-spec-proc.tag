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
	
	if (startWorkHour <= 0)
		startWorkHour = 8;
	
	if (endWorkHour <= startWorkHour)
		endWorkHour = startWorkHour + 9;
	
	if (endWorkHour >= 23)
		endWorkHour = 23;
	
	int hh = startWorkHour;
	
	if (startWorkMin > 0) {
		schOut += "<tr id='sc" + hh + "30'><td class='col-md-1'>" + hh + ":30</td><td class='col-md-1' id='oc" + hh + "30'></tr>";
		hh++;
	}
		
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		schOut += "<tr id='sc" + h + "00'><td class='col-md-1'>" + h + ":00</td><td class='col-md-1' id='oc" + h + "00'></tr>";
		schOut += "<tr id='sc" + h + "30'><td class='col-md-1'>" + h + ":30</td><td class='col-md-1' id='oc" + h + "30'></tr>";
	}

	hh = endWorkHour;
	schOut += "<tr id='sc" + hh + "00'><td class='col-md-1'>" + hh + ":00</td><td class='col-md-1' id='oc" + hh + "00'></tr>";
	
	if (endWorkMin > 0) {
		schOut += "<tr id='sc" + hh + "30'><td class='col-md-1'>" + hh + ":30</td><td class='col-md-1' id='oc" + hh + "30'></tr>";
	}

%>

<div class="panel col-md-4">
	<button type="button" class="btn btn-default">Today</button>
	<button type="button" class="btn btn-default">Tomorrow</button>
	<button type="button" class="btn btn-default">Saturday</button>
	<button type="button" class="btn btn-default">Sunday</button>
	<div id="datepicker"></div>
</div>
<div class="panel col-md-8">
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<td class="col-md-1">Time</td>
				<td class="col-md-3">Occupation</td>
			</tr>
		</thead>
		<tbody>
			<%=schOut %>
		</tbody>
	</table>
</div>

<script>
$(function() {
    $('#datepicker').datepicker({
    	onSelect: function(dateSelected) {
    		$.get('BBRSchedule', {
    			date: dateSelected
    		}, function (responseText) {
    			var arr = $.parseJSON(responseText);
    			$("td.info").removeClass('info');
    			for (i = 0; i < arr.length; i++) {
    				idoc = "#oc"+arr[i][0];
					$(idoc).addClass('info');
    				sz = Math.round(arr[i][1] * 2);
    				obj = $(idoc); 
    				for (m = 1; m < sz; m++) {
    					obj = obj.parent().next().children().first().next();
        				obj.addClass('info');
    				}
    			}
    		})
    	}
    });
  });
</script>

<div class="panel">
	<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
	<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
	<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
</div>