<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:wrapper title="LBL_STOCK_ITEM_TITLE">
<jsp:body>
		<t:card title="LBL_STOCK_ITEM_TITLE" gridPage="admin-stock-item-list.jsp" method="BBRStockItems">
			<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
			<t:card-item label="LBL_GROUP" type="reference" field="group" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRStockItemGroups" />
			<t:card-item label="LBL_DESCRIPTION" field="description" type="text"/>
			<t:card-item label="LBL_STATE" field="state" type="select" options="OPT_STOCK_ITEM_STATES"/>
		</t:card>
</jsp:body>
</t:wrapper>