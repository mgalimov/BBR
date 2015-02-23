<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:general-wrapper title="Plan your visit">
<jsp:body>
	<t:card title="Plan your visit" gridPage="general-plan-visit.jsp" method="BBRVisit">
		<t:card-item label="Select place" type="referenece" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoS"/>
		<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
		<t:card-item label="Your name" type="text" field="userName" isRequired="required" />
		<t:card-item label="Your phone" type="text" field="userContacts" isRequired="required" />
		<t:card-item label="Procedure" type="text" field="procedure" />
	</t:card>
</jsp:body>
</t:general-wrapper>
