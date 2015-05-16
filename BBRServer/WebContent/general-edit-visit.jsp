<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:general-wrapper title="See or cancel your visit">
<jsp:body>
		<t:card title="See or cancel your visit" gridPage="general-plan-visit.jsp" method="BBRVisits">
			<t:card-item label="Place selected" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" isDisabled="readonly"/>
			<t:card-item label="Date and time selected" type="text" field="timeScheduled"  isDisabled="readonly" />
			<t:card-item label="Name" type="text" field="userName" isDisabled="readonly"/>
			<t:card-item label="Phone" type="text" field="userContacts"  isDisabled="readonly" />
			<t:card-item label="Procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures" isDisabled="readonly"/>
			<t:card-item label="Specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists" isDisabled="readonly"/>
		</t:card>
</jsp:body>
</t:general-wrapper>
