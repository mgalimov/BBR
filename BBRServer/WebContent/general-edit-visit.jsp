<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:general-wrapper title="LBL_EDIT_VISIT_TITLE">
<jsp:body>
		<t:card title="LBL_EDIT_VISIT_TITLE" gridPage="general-plan-visit.jsp" method="BBRVisits">
			<t:card-item label="LBL_POS_SELECTED" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="readonly"/>
			<t:card-item label="LBL_DATE_TIME_SELECTED" type="text" field="timeScheduled"  isDisabled="readonly" />
			<t:card-item label="LBL_USER_NAME_SELECTED" type="text" field="userName" isDisabled="readonly"/>
			<t:card-item label="LBL_PHONE_SELECTED" type="text" field="userContacts"  isDisabled="readonly" />
			<t:card-item label="LBL_PROCEDURE_SELECTED" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures" isDisabled="readonly"/>
			<t:card-item label="LBL_SPEC_SELECTED" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists" isDisabled="readonly"/>
		</t:card>
</jsp:body>
</t:general-wrapper>
