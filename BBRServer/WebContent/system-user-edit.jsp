<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_EDIT_USER_TITLE">
	<jsp:body>
		<t:card method="BBRUsers" gridPage="system-user-list.jsp" title="LBL_EDIT_USER_TITLE">
			<t:card-item label="LBL_EMAIL" field="email" type="info" isRequired="required" />
			<t:card-item label="LBL_FIRST_NAME" field="firstName" type="text" isRequired="required" />
			<t:card-item label="LBL_LAST_NAME" field="lastName" type="text" isRequired="required" />
			<t:card-item label="LBL_SHOP" field="shop" type="reference" referenceFieldTitle="title" referenceMethod="BBRShops" isRequired="required" />
			<t:card-item label="LBL_POS" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" isRequired="required" />
			<t:card-item label="LBL_APPROVED" field="approved" type="select" options="true:Approved,false:Not approved yet" isRequired="required" />
			<t:card-item label="LBL_ROLE" field="role" type="select" options="OPT_ROLES" isRequired="required" />
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>