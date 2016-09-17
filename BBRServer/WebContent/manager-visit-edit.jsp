<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper title="LBL_EDIT_VISIT_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_VISIT_TITLE" gridPage="manager-visit-list.jsp" method="BBRVisits" showTabs="true">
			<t:toolbar-item label="LBL_APPROVE_VISIT" id="approveButton" accent="btn-success" condition="obj.status==0 || obj.status==2"></t:toolbar-item>
<%-- 			<t:toolbar-item label="LBL_DISAPPROVE_VISIT" id="disapproveButton" accent="btn-danger" condition="obj.status==0 || obj.status==2"></t:toolbar-item> --%>
			<t:toolbar-item label="LBL_CANCEL_VISIT" id="cancelVisitButton" accent="btn-default" condition="obj.status<=1"></t:toolbar-item>
			<t:toolbar-item label="LBL_CLOSE_VISIT" id="closeVisitButton" accent="btn-primary" condition="obj.status<=1"></t:toolbar-item>
			<t:card-tab label="LBL_MAIN_VISIT_TAB" id="mainTab" isActive="true">
				<t:card-item label="LBL_POS" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
				<t:card-item label="LBL_DATE_TIME" type="datetime" field="timeScheduled"/>
				<t:card-item label="LBL_REAL_TIME" type="datetime" field="realTime" timeStepping="5" defaultValue="now" isRequired="required" />
				<t:card-item label="LBL_USER_NAME" type="text" field="userName" isRequired="required" />
				<t:card-item label="LBL_PHONE" type="text" field="userContacts" isRequired="required" />
				<t:card-item label="LBL_BOOKING_CODE" type="text" field="bookingCode" isDisabled="readonly" />
				<t:card-item label="LBL_PROCEDURE" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures" isRequired="required" />
				<t:card-item label="LBL_ADDITIONAL_PROCEDURES" field="procedures" type="reference" referenceFieldTitle="title" referenceMethod="BBRProcedures" multiple="true" isRequired="required"/>
				<t:card-item label="LBL_LENGTH" type="text" field="length" isRequired="required" />
				<t:card-item label="LBL_SPEC" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists" isRequired="required" />
				<t:card-item label="LBL_VISIT_STATUS" field="status" type="select" options="OPT_VISIT_STATUS" isDisabled="readonly" defaultValue="3"/>
				<t:card-item label="LBL_COMMENT" type="text" field="comment" />
			</t:card-tab>
			<t:card-tab label="LBL_MONEY_TAB" id="moneyTab">
				<t:card-item label="LBL_FINAL_PRICE" type="text" field="finalPrice" isRequired="required" />
				<t:card-item label="LBL_DISCOUNT_PERCENT" type="text" field="discountPercent" defaultValue="0" isRequired="required"/>
				<t:card-item label="LBL_DISCOUNT_AMOUNT" type="text" field="discountAmount" defaultValue="0" isRequired="required"/>
				<t:card-item label="LBL_PRICE_PAID" type="text" field="pricePaid"  isRequired="required"/>
				<t:card-item label="LBL_AMOUNT_TO_SPECIALIST" type="text" field="amountToSpecialist" isRequired="required"/>
				<t:card-item label="LBL_AMOUNT_TO_MATERIALS" type="text" field="amountToMaterials" isRequired="required"/>
			</t:card-tab>
		</t:card>
</jsp:body>
</t:wrapper>

<script>
	var posId; 
	var price = 0;
	var length = 0;
	var procedurePercent = 0;

	$(document).ready(function() {
		posId = $("#posinput").val();
		
		$("#posinput").on("change", function () {
			newPosId = $("#posinput").val();
			if (newPosId != posId) {
				posId = newPosId;
				inp = $("#specinput")[0].selectize;
				inp.clear();
				inp.clearOptions();
				inp.load(specLoadInitialData);
				
				inp = $("#procedureinput")[0].selectize;
				inp.clear();
				inp.clearOptions();
				inp.load(procedureLoadInitialData);
			}
		});

		specSetConstrains = function () {
			return $("#posinput").val();
		}
		
		procedureSetConstrains = function () {
			return $("#posinput").val();
		}

		$("#approveButton").click(function() {
			performOperation('approve');
		});
		
		$("#disapproveButton").click(function() {
			performOperation('disapprove');
		});
		
		$("#cancelVisitButton").click(function() {
			performOperation('cancelVisit');
		});
		
		$("#closeVisitButton").click(function() {
			performOperation('close');
		});

		function performOperation(operation) {
			idParam = getUrlParameter('id');
			if (idParam && idParam != 'new') {
				$.ajax({
		        	url: 'BBRVisits',
		        	data: {
		        		operation: operation,
		        		visitId: idParam
		        	}
		        }).done(function () {
		        	saveChanges();
		        	window.location.href = window.location.href;		        	
		        }).fail(function () {
		        	window.location.href = window.location.href;
		        });									
				 
			}			
		}
		
		$("#discountPercentinput").change(onChange);
		$("#finalPriceinput").change(onChange);
		$("#specinput").change(function () {
			specId = $("#specinput").val(); 
			$.ajax({
	        	url: 'BBRSpecialists',
	        	data: {
	        		operation: "getdata",
	        		id: specId
	        	}
        	}).done(function (data) {
        		d = $.parseJSON(data);
        		procedurePercent = d.procedurePercent;
        		if (isNaN(procedurePercent))
        			procedurePercent = 0;
        		onChange();
        	});	
		});
		
		function onChange () {
			var v = $("#discountPercentinput").val();
			var vf = parseFloat(v);
			var fp = $("#finalPriceinput").val();
			var fpf = parseFloat(fp);

			if (!isNaN(fpf) && !isNaN(vf)) {
				$("#discountAmountinput").val(Math.round(fpf * vf / 100));
				$("#pricePaidinput").val(Math.round(fpf - Math.round(fpf * vf / 100)));
			}
			
			$("#amountToSpecialistinput").val(Math.round(fpf * procedurePercent / 100));
		}
		
		$("#posinput")[0].selectize.on("load", function () {
			idParam = getUrlParameter('id');
			if (!idParam || idParam == 'new') {
				var inp = $("#posinput")[0].selectize;
				firstOptionIndex = Object.keys(inp.options)[0];
				inp.setValue(inp.options[firstOptionIndex].id);
			}
		});
		
		$("#procedureinput").on("change", procChange);
		$("#proceduresinput").on("change", procChange);
			
		
		function procChange() {
			price = 0;
			length = 0;
			var prids = $("#procedureinput").val();
			var prids1 = $("#proceduresinput").val(); 
			if (prids1 != null && prids1 != "")
				prids = prids + "," + prids1;

			var procIds = prids.split(",");
			for (i = 0; i < procIds.length; i++) {
				procId = procIds[i];
				$.ajax({
		        	url: 'BBRProcedures',
		        	data: {
		        		operation: "getdata",
		        		id: procId
		        	}
	        	}).done(function (data) {
	        		d = $.parseJSON(data);
	        		length += d.length;
	        		price += d.price;
	        		$("#lengthinput").val(length)
	        		$("#finalPriceinput").val(price);
	        		onChange();
	        	});	
			}
		}
		
/*		idParam = getUrlParameter('id');
		if (!idParam || idParam == 'new') {
			$("#timeScheduledinput").attr("readonly", "");
		}*/
	});
	
</script>