<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="User">
	<jsp:body>
		<t:card method="BBRUserUpdate" gridPage="admin-users.jsp" title="User">
			<t:card-item label="Email" field="email" type="info"  isRequired="required"/>
			<t:card-item label="First name" field="firstName" type="text" isRequired="required"/>
			<t:card-item label="Last name" field="lastName" type="text" isRequired="required"/>
			<t:card-item label="Approved" field="approved" type="select" options="true:Approved,false:Not approved yet" isRequired="required"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>