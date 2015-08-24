<%@ page import="BBRClientApp.BBRContext"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<%
	BBRContext context = BBRContext.getContext(request);

	if (context.get("VisitorsDays") == null || (Integer)context.get("VisitorsDays") == 0)
		request.setAttribute("viewBtn", "#viewAll");
	else
		request.setAttribute("viewBtn", "#view" + context.get("VisitorsDays") + "Days");
%>


<t:admin-grid-wrapper title="LBL_VISITORS_TITLE">
	<jsp:body>
		<t:grid method="BBRVisitors" editPage="manager-visitor-edit.jsp" 
				createPage="" title="LBL_VISITORS_TITLE" 
				customToolbar="true">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_OPEN_BTN" id="edit" accent="btn-info"/>
			</t:toolbar-group>
			<t:toolbar-group>
				<t:toolbar-item label="LBL_LAST_30_DAYS" id="view30Days" icon="glyphicon-tasks"/>
				<t:toolbar-item label="LBL_LAST_120_DAYS" id="view120Days" icon="glyphicon-tasks"/>
				<t:toolbar-item label="LBL_LAST_360_DAYS" id="view360Days" icon="glyphicon-tasks"/>
				<t:toolbar-item label="LBL_VIEW_ALL_VISITORS" id="viewAll" icon="glyphicon-tasks"/>
			</t:toolbar-group>
			<t:grid-item label="LBL_USER_NAME" field="userName"/>
			<t:grid-item label="LBL_CONTACT_INFO" field="userContacts"/>
			<t:grid-item label="LBL_LAST_VISIT_DATE" field="lastVisitDate" sort="desc"/>
		</t:grid>
		
		<script>
		$(document).ready(function() {
			$("${viewBtn}").addClass("active");	
			
			$("#view30Days").on("click", function() {
				clickHandler('toggle30Days', "#view30Days");
			});

			$("#view120Days").on("click", function() {
				clickHandler('toggle120Days', "#view120Days");
			});

			$("#view360Days").on("click", function() {
				clickHandler('toggle360Days', "#view360Days");
			});

			$("#viewAll").on("click", function() {
				clickHandler('toggleAll', "#viewAll");
			});
		});
		
		function clickHandler(operation, btn) {
			$.ajax({
	        	url: 'BBRVisitors',
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
	</jsp:body>
</t:admin-grid-wrapper>
