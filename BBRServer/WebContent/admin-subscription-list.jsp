<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ page import="BBRClientApp.BBRContext"%>
<%@ page import="BBR.BBRUtil"%>
<%@ page import="BBRClientApp.BBRParams"%>

<%
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());	

	if (context.get("viewsubscriptions") == null)
		request.setAttribute("viewBtn", "#viewActive");
	else
		request.setAttribute("viewBtn", "#viewAll");
%>

<t:admin-grid-wrapper title="LBL_SUBSCRIPTIONS_TITLE">
	<jsp:body>
		<t:grid method="BBRSubscriptions" editPage="admin-subscription-edit.jsp" createPage="admin-subscription-create.jsp" 
				title="LBL_SUBSCRIPTIONS_TITLE" standardFilters="false" customToolbar="True">
			<t:toolbar-group>
				<t:toolbar-item label="LBL_GRID_CREATE_RECORD_BTN" id="create" accent="btn-default" icon="glyphicon-plus"/>
				<t:toolbar-item label="LBL_GRID_EDIT_RECORD_BTN" id="edit" accent="btn-info" icon="glyphicon-pencil"/>
			</t:toolbar-group>
			<t:toolbar-group>
				<t:toolbar-item label="LBL_TOGGLE_ALL_SUBSCR_BTN" id="viewAll" icon="glyphicon-tasks"/>
				<t:toolbar-item label="LBL_TOGGLE_ACTIVE_SUBSCR_BTN" id="viewActive" icon="glyphicon-tasks"/>
			</t:toolbar-group>
			<t:grid-item label="LBL_SHOP" field="shop.title" />
			<t:grid-item label="LBL_SERVICE" field="shop.title" />
			<t:grid-item label="LBL_SUBSCR_START_DATE" field="startDate" />
			<t:grid-item label="LBL_SUBSCR_END_DATE" field="endDate" />
			<t:grid-item label="LBL_SUBSCR_BALANCE" field="currency" />
			<t:grid-item label="LBL_SUBSCR_CURRENCY" field="currency" />
			<t:grid-item label="LBL_SUBSCR_LIMIT" field="creditLimit" />
			<t:grid-item label="LBL_SUBSCR_STATUS" field="status" type="select" options="OPT_SUBSCR_STATUS" />
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>


<script>
		$(document).ready(function() {
			$("${viewBtn}").addClass("active");

			$("#viewAll").click(function () {
				clickHandler("toggleallsubscriptions", "#viewAll");
			})		

			$("#viewActive").click(function () {
				clickHandler("toggleactivesubscriptions", "#viewActive");
			})		
		});
		

		function clickHandler(operation, btn) {
			$.ajax({
		    	url: 'BBRSubscriptions',
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