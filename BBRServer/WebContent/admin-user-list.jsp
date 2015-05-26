<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="LBL_USERS_TITLE">
	<jsp:body>
		<t:grid method="BBRUsers" editPage="admin-user-edit.jsp" createPage="admin-user-create.jsp" title="LBL_USERS_TITLE">
			<t:grid-item label="LBL_EMAIL" field="email" sort="asc"/>
			<t:grid-item label="LBL_FIRST_NAME" field="firstName"/>
			<t:grid-item label="LBL_LAST_NAME" field="lastName"/>
			<t:grid-item label="LBL_SHOP" field="shop.title"/>
			<t:grid-item label="LBL_POS" field="pos.title"/>
			<t:grid-item label="LBL_APPROVED" field="approved"/>
			<t:grid-item label="LBL_ROLE" field="role"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>