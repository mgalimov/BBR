<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<t:wrapper title="LBL_STOCK_GROUPS_TITLE">
<jsp:body>
		<t:card title="LBL_STOCK_GROUPS_TITLE" gridPage="admin-stock-group-list.jsp" method="BBRStockItemGroups">
			<t:card-item label="LBL_TITLE" type="text" field="title" isRequired="required" />
			<t:card-item label="LBL_SHOP" type="reference" field="shop" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRShops" />
			<t:card-item label="LBL_DESCRIPTION" field="description" type="text"/>
		</t:card>
</jsp:body>
</t:wrapper>