<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="My visits">
	<jsp:body>
		<t:grid method="BBRVisit" editPage="general-cancel-visit.jsp" createPage="general-plan-visit.jsp" title="Visits">
			<t:grid-item label="Date and Time" field="timeScheduled" sort="descending"/>
			<t:grid-item label="Point of sale (service)" field="posTitle" sort="ascending"/>
			<t:grid-item label="Name" field="userName"/>
			<t:grid-item label="Contacts" field="userContacts"/>
			<t:grid-item label="Specialist" field="status"/>
			<t:grid-item label="Status" field="spec.title"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>
