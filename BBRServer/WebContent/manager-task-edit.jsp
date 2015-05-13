<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Task">
	<jsp:body>
		<t:card method="BBRTasks" gridPage="manager-task-list.jsp" title="Task">
			<t:card-item label="Title" field="title" type="text" isDisabled="disabled"/>
			<t:card-item label="Point of Service" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="disabled"/>
			<t:card-item label="Performer" field="performer" type="reference" referenceFieldTitle="name" referenceMethod="BBRUsers" isDisabled="disabled"/>
			<t:card-item label="Text" field="text" type="text" isDisabled="disabled"/>
			<t:card-item label="Deadline" field="deadline" type="text" isDisabled="disabled"/>
			<t:card-item label="State" field="state" type="select" options="0:Initialized,1:Read,2:Completed" isDisabled="disabled"/>
		</t:card>
		
		<script>
			$('#grid tbody').on("click", "[data-btncolumn=0]", function(event) {
				tr = $(this).closest('tr');
				table = $("#grid").DataTable();
		        row = table.row(tr);
		        data = row.data(); 
		        
		        $.ajax({
		        	url: 'BBRTasks',
		        	data: {
		        		operation: 'approve',
		        		taskId: data.id
		        	}
		        }).success(function(d) {
					table.draw();
		        });
			});
		</script>
	</jsp:body>
</t:admin-card-wrapper>