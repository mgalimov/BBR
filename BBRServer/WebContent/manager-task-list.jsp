<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Tasks">
	<jsp:body>
		<t:grid method="BBRTasks" editPage="manager-task-edit.jsp" 
				createPage="manager-task-create.jsp" title="Tasks" 
				customToolbar="true">
			<t:toolbar-item label="Open" id="edit" accent="btn-info"></t:toolbar-item>
			<t:toolbar-item label="View all" id="viewAll" icon="glyphicon-tasks"></t:toolbar-item>
			<t:grid-button label="" icon="glyphicon-ok" condition="state != 2"/>
			<t:grid-item label="Title" field="title"/>
			<t:grid-item label="Point of Service" field="pos.title"/>
			<t:grid-item label="Performer" field="performer"/>
			<t:grid-item label="Text" field="text"/>
			<t:grid-item label="Deadline" field="deadline" sort="desc"/>
			<t:grid-item label="State" field="state" type="select" options="0:Initialized,1:Read,2:Completed"/>
		</t:grid>
		
		<script>
		$(document).ready(function() {
			$('#grid tbody').on("click", "[data-btncolumn=1]", function(event) {
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
		
		$(document).ready(function() {
			$("#viewAll").on("click", function() {
				$.ajax({
		        	url: 'BBRTasks',
		        	data: {
		        		operation: 'togglealltasks'
		        	}
		        }).success(function(d) {
		        	table = $("#grid").DataTable();
		        	table.ajax.reload();
		        	table.draw();
		        });
			});
		});
		</script>
	</jsp:body>
</t:admin-grid-wrapper>
