<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_EDIT_SPEC_TITLE">
	<jsp:body>
		<t:card method="BBRSpecialists" gridPage="manager-spec-list.jsp" title="LBL_EDIT_SPEC_TITLE">
			<t:card-item label="LBL_NAME" field="name" type="text" isRequired="required" />
			<t:card-item label="LBL_POS" type="reference" field="pos" referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
			<t:card-item label="LBL_POSITION" field="position" type="text" isRequired="required" />
			<t:card-item label="LBL_SPEC_STATUS" type="select" field="status" options="OPT_SPEC_STATUS"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>