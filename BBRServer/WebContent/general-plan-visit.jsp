<%@page import="BBRClientApp.BBRManagementApplication"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRManagementApplication" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	BBRManagementApplication app = BBRManagementApplication.getApp(request);
	String lastVisit = app.getLastVisitScheduled();
	request.setAttribute("lastVisit", lastVisit);		
%>
<t:general-wrapper title="Plan your visit">
<jsp:body>
<c:choose>
	<c:when test="${lastVisit == null}">
		<t:card title="Plan your visit" gridPage="general-plan-visit.jsp" method="BBRVisit">
			<t:card-item label="Select place" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes"/>
			<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
			<t:card-item label="Your name" type="text" field="userName" isRequired="required" />
			<t:card-item label="Your phone" type="text" field="userContacts" isRequired="required" />
			<t:card-item label="Procedure" type="text" field="procedure" />
		</t:card>
	</c:when>
	<c:when test="${lastVisit != null}">
		<div>
			<p>Thanks for your visit! Your visit id is ${lastVisit}.
			</p>
		</div>
	</c:when>
</c:choose>
</jsp:body>
</t:general-wrapper>
