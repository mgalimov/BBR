<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Task">
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
		
		<t:card method="BBRTasks" gridPage="manager-task-list.jsp" title="Task" showFooter="false" showToolbar="true">
			<t:toolbar-item label="Approve" id="approveButton"/> 
			<t:card-item label="Title" field="title" type="text" isDisabled="readonly"/>
			<t:card-item label="Point of Service" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="readonly"/>
			<t:card-item label="Performer" field="performer" type="reference" referenceFieldTitle="name" referenceMethod="BBRUsers" isDisabled="readonly"/>
			<t:card-item label="Text" field="text" type="textarea" isDisabled="readonly"/>
			<t:card-item label="Deadline" field="deadline" type="text" isDisabled="readonly"/>
			<t:card-item label="State" field="state" type="select" options="0:Initialized,1:Read,2:Completed" isDisabled="readonly"/>
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
				})
			});
		</script>
	</jsp:body>
</t:admin-card-wrapper>