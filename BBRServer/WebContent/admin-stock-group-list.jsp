<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_STOCK_ITEM_GROUPS_TITLE">
	<jsp:body>
		<t:grid method="BBRStockItemGroups" editPage="admin-stock-group-edit.jsp" createPage="admin-stock-group-edit.jsp" 
				title="LBL_STOCK_ITEM_GROUPS_TITLE" standardFilters="false">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_SHOP" field="shop.title"/>
			<t:grid-item label="LBL_DESCRIPTION" field="description"/>
		</t:grid>
	</jsp:body>
</t:wrapper>