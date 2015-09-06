<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard" import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="titleModifier" %>

<% BBRContext context = BBRContext.getContext(request); %>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<c:set var="items" scope="request" value=""/>
<c:set var="chartpackages" scope="request" value=""/>

<div class="row">
	<h3>${context.gs(title).concat(titleModifier)}</h3>
	<form class="form-inline pull-right">
		<div class="form-group">
			<span class="glyphicon glyphicon-calendar"></span>&nbsp;
	       	<input type='text' class="form-control" name="baseDatePicker" id="baseDatePicker"/>
		</div>

		<div class="form-group" style="width: 80px">
	       	<select id="detailSelect" name="detailSelect" class="selectized" style="display: none">
	       		<option value="1">${context.gs("OPT_DATE_DETAIL_HOUR")}</option>
	       		<option value="2" selected="selected">${context.gs("OPT_DATE_DETAIL_DAY")}</option>
	       		<option value="3">${context.gs("OPT_DATE_DETAIL_MONTH")}</option>
	       		<option value="4">${context.gs("OPT_DATE_DETAIL_YEAR")}</option>
	       	</select>
	    </div>
	    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    <div class="checkbox">
	    	<label>
	      		<input type="checkbox" id="compareToCheckbox"> ${context.gs("LBL_DATERANGE_APPLY_COMPARE_TO")}
	    	</label>
  		</div>
  		&nbsp;
		<div class="form-group">
	       	<input type='text' class="form-control" name="compareToDatePicker" id="compareToDatePicker" disabled="disabled" />
	    </div>
	    &nbsp;
	 </form>
</div> 

<div class="row">
	&nbsp;
</div>

<div class="row">
    <jsp:doBody/>
</div>

<script>
	google.load('visualization', '1.0', {'packages':[${chartpackages}]});
	google.setOnLoadCallback(drawCharts);
	
	$(document).ready(function () {
		moment.locale('<%=context.getLocaleString()%>');

		locale = {
				"format": "YYYY-MM-DD",
		        "separator": " - ",
		        "applyLabel": "${context.gs('LBL_DATERANGE_APPLY_BTN')}",
		        "cancelLabel": "${context.gs('LBL_DATERANGE_CANCEL_BTN')}",
		        "fromLabel": "${context.gs('LBL_DATERANGE_FROM')}",
		        "toLabel": "${context.gs('LBL_DATERANGE_TO')}",
		        "customRangeLabel": "${context.gs('LBL_DATERANGE_CUSTOM')}",
		        "daysOfWeek": moment.weekdaysShort(),
		        "monthNames": moment.months(),
		        "firstDay": 1
		    };
		
		$('#baseDatePicker').daterangepicker({
			autoApply: true,
			locale: locale
		});
		$('#compareToDatePicker').daterangepicker({
			autoApply: true,
			locale: locale
		});
		
		$("#compareToCheckbox").change(function () {
			if (this.checked)
				$("#compareToDatePicker").removeAttr("disabled");
			else
				$("#compareToDatePicker").attr("disabled", "disabled");
		});
		
		$("#detailSelect").selectize({
		    openOnFocus: true
		 });
	});
	
	function drawCharts() {
		${items}
	}
</script>
