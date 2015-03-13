<%@ tag language="java" pageEncoding="UTF-8" description="Card Schedule-Spec-Proc"%>
<%@ attribute name="fieldTime" required="true" %>
<%@ attribute name="fieldSpecialist" required="true" %>
<%@ attribute name="fieldProcedure" required="true" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<script src="js/jquery.supercal.js"></script>
<link href="css/bbr-calendar.css" rel="stylesheet" type="text/css">

<div class="panel col-md-8">
	<table class="table table-striped  table-hover">
		<thead>
			<tr>
				<td class="col-md-1">Time<td>
				<td class="col-md-4">Occupation</td>
			</tr>
		</thead>
		<tbody>
			<tr><td>8:00<td><td rowspan="2" class="info"></td></tr>
			<tr><td>8:30<td></tr>
			<tr><td>9:00<td><td></td></tr>
			<tr><td>9:30<td><td></td></tr>
			<tr><td>10:00<td><td></td></tr>
			<tr><td>10:30<td><td></td></tr>
		</tbody>
	</table>
</div>
<div class="panel col-md-4">
	<button type="button" class="btn btn-default">Today</button>
	<button type="button" class="btn btn-default">Tomorrow</button>
	<button type="button" class="btn btn-default">Saturday</button>
	<button type="button" class="btn btn-default">Sunday</button>
	<div id="calendar"></div>
</div>

<script>
$('#calendar').supercal({
	todayButton: false,
	showInput: true,
	weekStart: 1,
	widget: true,
	cellRatio: 1,
	format: 'd/m/y',
	footer: true,
	dayHeader: true,
	mode: 'widget', // 'widget' (default), 'tiny', 'popup', 'page'
	animDuration: 200,
	transition: '',
	tableClasses: 'table table-condensed',
	hidden: true,
	setOnMonthChange: true,
	condensed: false
});
</script>

<div class="panel">
	<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
	<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
	<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
</div>

<!-- http://www.jqueryscript.net/time-clock/Simple-Animated-jQuery-Calendar-Plugin-with-Bootstrap-SuperCal.html -->