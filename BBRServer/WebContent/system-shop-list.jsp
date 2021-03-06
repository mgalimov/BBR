<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_SHOPS_TITLE">
	<jsp:body>
		<t:grid method="BBRShops" editPage="system-shop-edit.jsp" createPage="system-shop-create.jsp" title="LBL_SHOPS_TITLE">
			<t:grid-item label="LBL_TITLE" field="title" sort="asc"/>
			<t:grid-item label="LBL_COUNTRY" field="country" />
			<t:grid-item label="LBL_TIMEZONE" field="timeZone" />
			<t:grid-item label="LBL_SHOP_STATUS" field="status" type="select" options="OPT_SHOP_STATUS"/>
		</t:grid>
	</jsp:body>
</t:wrapper>