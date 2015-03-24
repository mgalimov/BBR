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
		schOut += "<tr id='sc" + hh + "30'><td class='col-md-1'>" + hh + ":30</td></tr>";
		hh++;
	}
		
	for (Integer h = hh; h <= endWorkHour - 1; h++) {
		schOut += "<tr id='sc" + h + "00'><td class='col-md-1'>" + h + ":00</td></tr>";
		schOut += "<tr id='sc" + h + "30'><td class='col-md-1'>" + h + ":30</td></tr>";
	}

	schOut += "<tr id='sc" + endWorkHour + "00'><td class='col-md-1'>" + endWorkHour + ":00</td></tr>";
	
	if (endWorkMin > 0) {
		schOut += "<tr id='sc" + endWorkHour + "30'><td class='col-md-1'>" + endWorkHour + ":30</td></tr>";
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
    			hh = <%=startWorkHour%>;
    			if (<%=startWorkMin%> > 0) {
    				$("#oc" + hh + "30").remove();
    				$("#sc" + hh + "30").append("<td class='col-md-1' id='oc" + hh + "30'>");
    				hh++;
    			}
    			for (h = hh; h <= <%=endWorkHour - 1%>; h++) {
    				$("#oc" + h + "00").remove();
    				$("#sc" + h + "00").append("<td class='col-md-1' id='oc" + h + "00'>");
    				$("#oc" + h + "30").remove();
    				$("#sc" + h + "30").append("<td class='col-md-1' id='oc" + h + "30'>");
    			}
    			
    			hh = <%=endWorkHour%>;

    			$("#oc" + hh + "00").remove();
				$("#sc" + hh + "00").append("<td class='col-md-1' id='oc" + hh + "00'>");

    			if (<%=endWorkMin%> > 0) {
    				$("#oc" + hh + "30").remove();
    				$("#sc" + hh + "30").append("<td class='col-md-1' id='oc" + hh + "30'>");
    			}
    			
    			for (i = 0; i < arr.length; i++) {
    				idsc = "#sc"+arr[i][0];
    				idoc = "#oc"+arr[i][0];
    				sz = Math.round(arr[i][1] * 2);
    				$(idoc).remove();
    				$(idsc).append("<td class='col-md-1' rowspan='" + sz + "'>");
    				
    				id = idsc;
    				for (m = 2; m <= sz; m++) {
    					$(id).next().children().first().next().remove();
    					id = $(id).next().id;
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