<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_EDIT_USER_TITLE">
	<jsp:body>
		<t:card method="BBRUsers" gridPage="admin-user-list.jsp" title="LBL_EDIT_USER_TITLE" showTabs="true">
			<t:card-tab label="LBL_USER" id="generalTab" combined="true" isActive="true">
				<t:card-item label="LBL_EMAIL" field="email" type="text" isRequired="required" isDisabled="readonly"/>
				<t:card-item label="LBL_FIRST_NAME" field="firstName" type="text" isRequired="required" />
				<t:card-item label="LBL_LAST_NAME" field="lastName" type="text" isRequired="required" />
				<t:card-item label="LBL_POS" field="pos" type="reference" referenceFieldTitle="title" referenceMethod="BBRPoSes" />
				<t:card-item label="LBL_APPROVED" field="approved" type="select" options="OPT_USER_APPROVED" isRequired="required" />
				<t:card-item label="LBL_ROLE" field="role" type="select" options="OPT_ROLES_SUBSET" isRequired="required" />
			</t:card-tab>
			<t:card-tab label="LBL_PHOTO" id="photoTab" combined="true">
				<t:card-item label="LBL_PHOTO" field="photo" type="picture" />
			</t:card-tab>
		</t:card>
	</jsp:body>
</t:wrapper>