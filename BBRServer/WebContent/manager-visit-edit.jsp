<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:admin-card-wrapper title="LBL_EDIT_VISIT_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_VISIT_TITLE" gridPage="manager-visit-list.jsp" method="BBRVisits">
			<t:modal cancelButtonLabel="LBL_CANCEL_CHANGES" processButtonLabel="LBL_SAVE_DATA" title="LBL_EDIT_FINAL_PRICE" id="enterFinalPrice">
				<t:card-item label="LBL_FINAL_PRICE" type="text" field="finalPrice"/>
			</t:modal>
			<t:toolbar-item label="LBL_APPROVE_VISIT" id="approveButton" accent="btn-info" condition="obj.status==0"></t:toolbar-item>
			<t:toolbar-item label="LBL_DISAPPROVE_VISIT" id="disapproveButton" accent="btn-warning" condition="obj.status==0"></t:toolbar-item>
			<t:toolbar-item label="LBL_CLOSE_VISIT" id="closeVisitButton" accent="btn-success" condition="obj.status<=1"></t:toolbar-item>
			<t:card-item label="LBL_POS" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="readonly"/>
			<t:card-item label="LBL_DATE_TIME" type="datetime" field="timeScheduled" />
			<t:card-item label="LBL_VISIT_STATUS" field="status" type="select" options="OPT_VISIT_STATUS" isDisabled="readonly"/>
			<t:card-item label="LBL_USER_NAME" type="text" field="userName" isDisabled="readonly"/>
			<t:card-item label="LBL_PHONE" type="text" field="userContacts" />
			<t:card-item label="LBL_PROCEDURE" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures" />
			<t:card-item label="LBL_SPEC" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists" />
			<t:card-item label="LBL_POS_START_WORKHOUR" field="pos.startWorkHour" type="time" isDisabled="readonly"/>
			<t:card-item label="LBL_POS_END_WORKHOUR" field="pos.endWorkHour" type="time" isDisabled="readonly"/>
		</t:card>
</jsp:body>
</t:admin-card-wrapper>

<script>
	var posId; 

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
			idParam = getUrlParameter('id');
			if (idParam && idParam != 'new') {
				$.ajax({
		        	url: 'BBRVisits',
		        	data: {
		        		operation: 'approve',
		        		visitId: idParam
		        	}
		        });									
				window.location.href = window.location.href; 
			}					
		});
		
		$("#disapproveButton").click(function() {
			idParam = getUrlParameter('id');
			if (idParam && idParam != 'new') {
				$.ajax({
		        	url: 'BBRVisits',
		        	data: {
		        		operation: 'disapprove',
		        		visitId: idParam
		        	}
		        });									
				window.location.href = window.location.href; 
			}					
		});

		$("#closeVisitButton").click(function() {
			idParam = getUrlParameter('id');
			if (idParam && idParam != 'new') {
				$('#enterFinalPrice').modal();
			}					
		});

		$("#enterFinalPriceprocess").click(function() {
			idParam = getUrlParameter('id');
			if (idParam && idParam != 'new') {
				$.ajax({
		        	url: 'BBRVisits',
		        	data: {
		        		operation: 'close',
		        		visitId: idParam
		        	}
		        }).success(function () {
					$("#saveChanges").click();
		        });
			}					
		});

	});
	
</script>