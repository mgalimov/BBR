<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	

	if (context.get("viewtasks") == null)
		request.setAttribute("viewBtn", "#viewIncomplete");
	else
		request.setAttribute("viewBtn", "#viewAll");
%>

<t:wrapper title="LBL_TASKS_TITLE">
	<jsp:body>
		<t:grid method="BBRTasks" editPage="manager-task-edit.jsp" 
				createPage="manager-task-create.jsp" title="LBL_TASKS_TITLE" 
				customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_OPEN_BTN" id="edit" accent="btn-info"></t:toolbar-item>
			</t:toolbar-group>
			<t:toolbar-group>
				<t:toolbar-item label="LBL_TOGGLE_ALL_BTN" id="viewAll" icon="glyphicon-tasks"></t:toolbar-item>
				<t:toolbar-item label="LBL_TOGGLE_INCOMPLETE_BTN" id="viewIncomplete" icon="glyphicon-tasks"></t:toolbar-item>
			</t:toolbar-group>
			<t:grid-button label="" icon="glyphicon-ok" condition="state != 2"/>
			<t:grid-button label="" icon="glyphicon-remove" condition="state != 2"/>
			<t:grid-item label="LBL_TITLE" field="title"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_PERFORMER" field="performer"/>
			<t:grid-item label="LBL_TASK_TEXT" field="text"/>
			<t:grid-item label="LBL_DEADLINE" field="deadline" sort="desc"/>
			<t:grid-item label="LBL_CREATEDAT" field="createdAt" sort="desc"/>
			<t:grid-item label="LBL_TASK_STATE" field="state" type="select" options="OPT_TASK_STATE"/>
		</t:grid>
	</jsp:body>
</t:wrapper>

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
			$("${viewBtn}").addClass("active");

			$("#viewAll").click(function () {
				clickHandler("togglealltasks", "#viewAll");
			})		

			$("#viewIncomplete").click(function () {
				clickHandler("toggleincompletetasks", "#viewIncomplete");
			})		
		});
		

		function clickHandler(operation, btn) {
			$.ajax({
		    	url: 'BBRTasks',
		    	data: {
		    		operation: operation
		    	}
		    }).success(function (d) {
		    	table = $("#grid").DataTable();
		    	table.ajax.reload();
		    	table.draw();
		    	$(".active").removeClass("active");
		    	$(btn).addClass("active");			
		    });			
		}
</script>