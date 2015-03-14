<%@ tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc" import="BBRClientApp.BBRContext"%>
<%@ attribute name="fieldTime" required="true" %>
<%@ attribute name="fieldSpecialist" required="true" %>
<%@ attribute name="fieldProcedure" required="true" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	BBRContext context = BBRContext.getContext(request);

%>

<div class="panel col-md-8">
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<td class="col-md-1">Time</td>
				<td class="col-md-3">Occupation</td>
			</tr>
		</thead>
		<tbody>
			
			<tr><td class="col-md-1">8:00</td><td rowspan="2" class="info col-md-3"></td></tr>
			<tr><td class="col-md-1">8:30</td></tr>
			<tr><td class="col-md-1">9:00</td><td class="col-md-3"></td></tr>
			<tr><td class="col-md-1">9:30</td><td class="col-md-3"></td></tr>
			<tr><td class="col-md-1">10:00</td><td class="col-md-3"></td></tr>
			<tr><td class="col-md-1">10:30</td><td class="col-md-3"></td></tr>
		</tbody>
	</table>
</div>
<div class="panel col-md-4">
	<button type="button" class="btn btn-default">Today</button>
	<button type="button" class="btn btn-default">Tomorrow</button>
	<button type="button" class="btn btn-default">Saturday</button>
	<button type="button" class="btn btn-default">Sunday</button>
	<div id="datepicker"></div>
</div>

<script>
$(function() {
    $('#datepicker').datepicker({
    	onSelect: function(dateSelected) {
    		$.get('BBRSchedule', {
    			date: dateSelected
    		}, function (responseText) {
    			
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