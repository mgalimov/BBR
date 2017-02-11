<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_CREATE_USER_TITLE">
	<jsp:body>
		<t:card method="BBRUsers" gridPage="admin-user-list.jsp" title="LBL_CREATE_USER_TITLE">
			<t:card-item label="LBL_EMAIL" field="email" type="text" isRequired="required"/>
			<t:card-item label="LBL_FIRST_NAME" field="firstName" type="text" isRequired="required"/>
			<t:card-item label="LBL_LAST_NAME" field="lastName" type="text" isRequired="required"/>
			<t:card-item label="LBL_PASSWORD" field="password" type="password" isRequired="required"/>
			<t:card-item label="LBL_POS" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" />
			<t:card-item label="LBL_ROLE" field="role" type="select" options="OPT_ROLES_SUBSET" isRequired="required" defaultValue="4"/>
		</t:card>
	</jsp:body>
</t:wrapper>