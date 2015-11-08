<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_SUBSCRIPTIONS_TITLE">
	<jsp:body>
		<t:grid method="BBRSubscriptions" editPage="admin-subscription-edit.jsp" createPage="admin-subscription-create.jsp" 
				title="LBL_SUBSCRIPTIONS_TITLE" standardFilters="false">
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