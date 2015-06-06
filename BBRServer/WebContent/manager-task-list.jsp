<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_TASKS_TITLE">
	<jsp:body>
		<t:grid method="BBRTasks" editPage="manager-task-edit.jsp" 
				createPage="manager-task-create.jsp" title="LBL_TASKS_TITLE" 
				customToolbar="true">
			<t:toolbar-item label="LBL_OPEN_BTN" id="edit" accent="btn-info"></t:toolbar-item>
			<t:toolbar-item label="LBL_TOGGLE_ALL_BTN" id="viewAll" icon="glyphicon-tasks"></t:toolbar-item>
			<t:grid-button label="" icon="glyphicon-ok" condition="state != 2"/>
			<t:grid-button label="" icon="glyphicon-remove" condition="state != 2"/>
			<t:grid-item label="LBL_TITLE" field="title"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_PERFORMER" field="performer"/>
			<t:grid-item label="LBL_TASK_TEXT" field="text"/>
			<t:grid-item label="LBL_DEADLINE" field="deadline" sort="desc"/>
			<t:grid-item label="LBL_TASK_STATE" field="state" type="select" options="OPT_TASK_STATE"/>
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
			$('#grid tbody').on("click", "[data-btncolumn=2]", function(event) {
				tr = $(this).closest('tr');
				table = $("#grid").DataTable();
		        row = table.row(tr);
		        data = row.data(); 
		        
		        $.ajax({
		        	url: 'BBRTasks',
		        	data: {
		        		operation: 'disapprove',
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
