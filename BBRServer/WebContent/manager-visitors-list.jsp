<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_VISITORS_TITLE">
	<jsp:body>
		<t:grid method="BBRVisitors" editPage="manager-visitor-edit.jsp" 
				createPage="manager-visitor-create.jsp" title="LBL_TASKS_TITLE" 
				customToolbar="true">
			<t:toolbar-item label="LBL_OPEN_BTN" id="edit" accent="btn-info"></t:toolbar-item>
			<t:toolbar-item label="LBL_LAST_30_DAYS" id="view30Days" icon="glyphicon-tasks"></t:toolbar-item>
			<t:toolbar-item label="LBL_LAST_120_DAYS" id="view120Days" icon="glyphicon-tasks"></t:toolbar-item>
			<t:toolbar-item label="LBL_LAST_360_DAYS" id="view360Days" icon="glyphicon-tasks"></t:toolbar-item>
			<t:toolbar-item label="LBL_VIEW_ALL_VISITORS" id="viewAll" icon="glyphicon-tasks"></t:toolbar-item>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_LAST_VISIT_DATE" field="lastVisitDate" sort="desc"/>
		</t:grid>
		
		<script>
		$(document).ready(function() {
			$("#view30Days").on("click", function() {
				$.ajax({
		        	url: 'BBRVisitors',
		        	data: {
		        		operation: 'toggle30Days'
		        	}
		        }).success(function(d) {
		        	table = $("#grid").DataTable();
		        	table.ajax.reload();
		        	table.draw();
		        });
			});

			$("#view120Days").on("click", function() {
				$.ajax({
		        	url: 'BBRVisitors',
		        	data: {
		        		operation: 'toggle120Days'
		        	}
		        }).success(function(d) {
		        	table = $("#grid").DataTable();
		        	table.ajax.reload();
		        	table.draw();
		        });
			});

			$("#view360Days").on("click", function() {
				$.ajax({
		        	url: 'BBRVisitors',
		        	data: {
		        		operation: 'toggle360Days'
		        	}
		        }).success(function(d) {
		        	table = $("#grid").DataTable();
		        	table.ajax.reload();
		        	table.draw();
		        });
			});

			$("#viewAll").on("click", function() {
				$.ajax({
		        	url: 'BBRVisitors',
		        	data: {
		        		operation: 'toggleAll'
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
