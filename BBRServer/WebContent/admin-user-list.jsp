<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Users">
	<jsp:body>
		<t:grid method="BBRUsers" editPage="admin-user-edit.jsp" createPage="admin-user-create.jsp" title="Users">
			<t:grid-item label="Email" field="email" sort="asc"/>
			<t:grid-item label="First name" field="firstName"/>
			<t:grid-item label="Last name" field="lastName"/>
			<t:grid-item label="Shop" field="shop"/>
			<t:grid-item label="PoS" field="pos"/>
			<t:grid-item label="Approved" field="approved"/>
			<t:grid-item label="Role" field="role"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>