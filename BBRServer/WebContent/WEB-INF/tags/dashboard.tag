<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard" import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<%@ attribute name="title" required="true" %>
<%@ attribute name="titleModifier" %>

<% 
	BBRContext context = BBRContext.getContext(request);
%>
<!-- <script type="text/javascript" src="https://www.google.com/jsapi"></script> -->

<c:set var="items" scope="request" value=""/>
<c:set var="chartpackages" scope="request" value="'corechart'"/>

<div class="row">
	<h3>${context.gs(title).concat(titleModifier)}</h3>
	<form class="form-inline pull-right">
		<span class="glyphicon glyphicon-globe"></span>&nbsp;
		<t:select-shop-pos field="shoppos" />&nbsp;&nbsp;&nbsp;&nbsp;
	
		<div class="form-group">
			<span class="glyphicon glyphicon-calendar"></span>&nbsp;
	       	<input type='text' class="form-control" name="baseDatePicker" id="baseDatePicker"/>
		</div>

		<div class="form-group" style="width: 80px">
	       	<select id="detailSelect" name="detailSelect" class="selectized" style="display: none">
	       		<option value="1">${context.gs("OPT_DATE_DETAIL_HOUR")}</option>
	       		<option value="2">${context.gs("OPT_DATE_DETAIL_DAY")}</option>
	       		<option value="3" selected="selected">${context.gs("OPT_DATE_DETAIL_MONTH")}</option>
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
	    &nbsp;&nbsp;&nbsp;
	    <div class="form-group">
	       	<button type='button' class="btn btn-primary" id="applyBtn">${context.gs("LBL_DATERANGE_APPLY_BTN")}</button>
	    </div>
	 </form>
</div> 

<div class="row">
	&nbsp;
</div>

<div class="row">
    <jsp:doBody/>
</div>

<script>
	var periods = null;
	
	$(document).ready(function () {
		google.charts.load('current', {'packages':[${chartpackages}]});
		google.charts.setOnLoadCallback(drawCharts);
		
		moment.locale('<%=context.getLocaleString()%>');

		locale = {
				"format": "YYYY-MM-DD",
		        "separator": " — ",
		        "applyLabel": "${context.gs('LBL_DATERANGE_APPLY_BTN')}",
		        "cancelLabel": "${context.gs('LBL_DATERANGE_CANCEL_BTN')}",
		        "fromLabel": "${context.gs('LBL_DATERANGE_FROM')}",
		        "toLabel": "${context.gs('LBL_DATERANGE_TO')}",
		        "customRangeLabel": "${context.gs('LBL_DATERANGE_CUSTOM')}",
		        "daysOfWeek": moment.weekdaysShort(),
		        "monthNames": moment.months(),
		        "firstDay": 1
		    };

		var startDate = moment();
		startDate.subtract(3, "months");
		
		$('#baseDatePicker').daterangepicker({
			autoApply: true,
			locale: locale,
			startDate: startDate
		});
		
		$('#compareToDatePicker').daterangepicker({
			autoApply: true,
			locale: locale
		});
		
		$("#compareToCheckbox").change(function () {
			if (this.checked) {
				var dtp = $("#baseDatePicker").data("daterangepicker");
				var compEl = $("#compareToDatePicker");
				
				compEl.removeAttr("disabled");
				var range = dtp.endDate.diff(dtp.startDate, "days");
				var detail = $("#detailSelect").val();
				
				if (detail >= 3) {
					range = Math.round(range / 30);
					startDate = moment(dtp.startDate);
					startDate.subtract(range, "months");
					endDate = moment(dtp.endDate);
					endDate.subtract(range, "months");
				}
				else {
					endDate = moment(dtp.startDate);
					endDate.subtract(1, "days");
					startDate = moment(endDate);
					startDate.subtract(range, "days");

				}; 
				
				compEl.daterangepicker({
					autoApply: true,
					locale: locale,
					startDate: startDate,
					endDate: endDate
				})
			}
			else
				$("#compareToDatePicker").attr("disabled", "disabled");
		});
		
		$("#detailSelect").selectize({
		    openOnFocus: true
		 });
		
		$("#applyBtn").click(function () {
			setPeriods();
			drawCharts();
		});
		
		$shopposfirstLoad = true;
		var el = $("#shopposinput")[0].selectize;
		el.load(shopposLoadData);
		el.on("load", function () {
			if ($shopposfirstLoad) {
				var el = $("#shopposinput")[0].selectize;
				var firstOptionIndex = Object.keys(el.options)[0];
				for (i = 0; i < Object.keys(el.options).length; i++) {
					var s = Object.keys(el.options)[i];
					if (s.charAt(0) == "s") {
						firstOptionIndex = Object.keys(el.options)[i];
						break;
					}
				}
				
	    		el.addItem(el.options[firstOptionIndex].id);
	    		el.refreshItems();
	    		$("#applyBtn").click();
	    		$shopposfirstLoad = false;
			}
		});
	});
	
	function drawCharts() {
		${items}
	}
	
	function setPeriods() {
		var baseDtp = $("#baseDatePicker").data("daterangepicker");
		
		var compToStartDate = null;
		var compToEndDate = null;
		
		if (document.getElementById("compareToCheckbox").checked) {
			var compDtp = $("#compareToDatePicker").data("daterangepicker");
			compToStartDate = compDtp.startDate.format("YYYY-MM-DD");
			compToEndDate = compDtp.endDate.format("YYYY-MM-DD");
		}
		
		var startDate = baseDtp.startDate.format("YYYY-MM-DD");
		var endDate = baseDtp.endDate.format("YYYY-MM-DD");
		var detail = $("#detailSelect").val();
		
		periods = {
			startDate: startDate,
			endDate: endDate,
			detail: detail,
			compareToStartDate: compToStartDate,
			compareToEndDate: compToEndDate
		};
	}
</script>
