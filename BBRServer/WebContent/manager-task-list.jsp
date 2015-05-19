<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Tasks">
	<jsp:body>
		<t:grid method="BBRTasks" editPage="manager-task-edit.jsp" 
				createPage="manager-task-create.jsp" title="Tasks" 
				customToolbar="true">
			<t:toolbar-item label="Open" id="edit"></t:toolbar-item>
			<t:grid-button label="" icon="glyphicon-ok" condition="state != 2"/>
			<t:grid-item label="Title" field="title" sort="asc"/>
			<t:grid-item label="Point of Service" field="pos.title"/>
			<t:grid-item label="Performer" field="performer"/>
			<t:grid-item label="Text" field="text"/>
			<t:grid-item label="Deadline" field="deadline"/>
			<t:grid-item label="State" field="state" type="select" options="0:Initialized,1:Read,2:Completed"/>
		</t:grid>
		
		<script>
		$(document).ready(function() {
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
		});
		</script>
	</jsp:body>
</t:admin-grid-wrapper>