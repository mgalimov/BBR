<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper>
	<jsp:attribute name="title">
      User
    </jsp:attribute>
	<jsp:body>
		<t:card method="BBRUserUpdate" pageBack="admin-users.jsp" title="User">
			<t:card-item label="Email" field="email" type="info" />
			<t:card-item label="First name" field="firstName" type="text"/>
			<t:card-item label="Last name" field="lastName" type="text"/>
			<t:card-item label="Approved" field="approved" type="select" options="true:Approved,false:Not approved yet"/>
		</t:card>
		</jsp:body>
</t:admin-card-wrapper>