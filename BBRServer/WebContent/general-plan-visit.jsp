<%@page import="BBRClientApp.BBRContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<%
	BBRContext context = BBRContext.getContext(request);
	String lastVisit = context.getLastVisitScheduled();
	request.setAttribute("lastVisit", lastVisit);
	if (context.user != null)
		request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
	else
		request.setAttribute("userName", "");
	request.setAttribute("closestPoS", "10"/*BBRManagementApplication.getClosestPoS()*/);
	request.setAttribute("closestPoSName", "10"/*BBRManagementApplication.getClosestPoS()*/);
	request.setAttribute("location", Float.toString(context.getLocation().lat) + ", " + Float.toString(context.getLocation().lng));
%>
<t:general-wrapper title="Plan your visit">
<jsp:body>
<c:out value="${location}"></c:out>
<c:choose>
	<c:when test="${lastVisit == null}">
		<t:card title="Plan your visit" gridPage="general-plan-visit.jsp" method="BBRVisits">
			<t:card-item label="Select place" type="reference" field="pos" isRequired="required" referenceFieldTitle="title" referenceMethod="BBRPoSes" defaultValue="${closestPoS}" defaultDisplay="${closestPoSName}"/>
			<t:card-item label="Date and time YYYY-MM-DD HH-MM" type="text" field="timeScheduled" isRequired="required" />
			<t:card-item label="Your name" type="text" field="userName" isRequired="required" defaultValue="${userName}"/>
			<t:card-item label="Your phone" type="text" field="userContacts" isRequired="required" />
			<t:card-item label="Select procedure" type="reference" field="procedure" referenceFieldTitle="title" referenceMethod="BBRProcedures"/>
			<t:card-item label="Select specialist" type="reference" field="spec" referenceFieldTitle="name" referenceMethod="BBRSpecialists"/>
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
