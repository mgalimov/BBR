<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:wrapper title="LBL_TASK_TITLE">
	<jsp:body>
		<script>
			$(document).ready(function() {
				idParam = getUrlParameter('id');
				if (idParam && idParam != 'new') {
					$.ajax({
			        	url: 'BBRTasks',
			        	data: {
			        		operation: 'markread',
			        		taskId: idParam
			        	}
			        });									
				}
			});
		</script>
		
		<t:card method="BBRTasks" gridPage="manager-task-list.jsp" title="LBL_TASK_TITLE" showFooter="false" showToolbar="true">
			<t:toolbar-item label="LBL_APPROVE_BTN" id="approveButton" accent="btn-info" icon="glyphicon-ok"/> 
<%-- 			<t:toolbar-item label="LBL_DISAPPROVE_BTN" id="disapproveButton" accent="btn-warning"  icon="glyphicon-remove"/>  --%>
			<t:toolbar-item label="LBL_OPEN_VISIT_BTN" id="openVisitButton"  condition="obj.objectType=='BBRCust.BBRVisit'"  icon="glyphicon-open"/> 
			<t:toolbar-item label="LBL_OPEN_SUBSCR_BTN" id="openSubscriptionButton" condition="obj.objectType=='BBRAcc.BBRServiceSubscription'" icon="glyphicon-credit-card"/> 
			<t:card-item label="LBL_TITLE" field="title" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_POS" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="readonly"/>
			<t:card-item label="LBL_PERFORMER" field="performer" type="reference" referenceFieldTitle="name" referenceMethod="BBRUsers" isDisabled="readonly"/>
			<t:card-item label="LBL_TASK_TEXT" field="text" type="textarea" isDisabled="readonly"/>
			<t:card-item label="LBL_DEADLINE" field="deadline" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_CREATEDAT" field="createdAt" type="text" isDisabled="readonly"/>
			<t:card-item label="LBL_TASK_STATE" field="state" type="select" options="OPT_TASK_STATE" isDisabled="readonly"/>
		</t:card>
		
		<script>
			$(document).ready(function() {
				$("#approveButton").on("click", function() {
					idParam = getUrlParameter('id');
					if (idParam && idParam != 'new') {
						$.ajax({
				        	url: 'BBRTasks',
				        	data: {
				        		operation: 'approve',
				        		taskId: idParam
				        	}
				        }).success(function(d) {
				        	window.location.href = 'manager-task-list.jsp'; 
				        });									
					}					
				});

				$("#openVisitButton").on("click", function() {
					idParam = getUrlParameter('id');
					if (idParam && idParam != 'new') {
						$.ajax({
				        	url: 'BBRTasks',
				        	data: {
				        		operation: 'getvisit',
				        		taskId: idParam
				        	}
				        }).success(function(d) {
				        	if (d) window.location.href = 'manager-visit-edit.jsp?id=' + d; 
				        });									
					}					
				});
				
				$("#openSubscriptionButton").on("click", function() {
					idParam = getUrlParameter('id');
					if (idParam && idParam != 'new') {
						$.ajax({
				        	url: 'BBRTasks',
				        	data: {
				        		operation: 'getsubscription',
				        		taskId: idParam
				        	}
				        }).success(function(d) {
				        	if (d) window.location.href = 'admin-subscription-edit.jsp?id=' + d; 
				        });									
					}					
				});
			});
		</script>
	</jsp:body>
</t:wrapper>