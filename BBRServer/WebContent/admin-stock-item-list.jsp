<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_STOCK_ITEMS_TITLE">
	<jsp:body>
		<t:grid method="BBRStockItems" editPage="admin-stock-item-edit.jsp" createPage="admin-stock-item-edit.jsp" 
				title="LBL_STOCK_ITEMS_TITLE" standardFilters="false">
			<t:grid-filter label="LBL_GROUP" type="reference" field="group" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRStockItemGroups"/> 
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_GROUP" field="group.title"/>
			<t:grid-item label="LBL_DESCRIPTION" field="description"/>
			<t:grid-item label="LBL_STATE" field="state" type="select" options="OPT_STOCK_ITEM_STATES"/>
		</t:grid>
	</jsp:body>
</t:wrapper>