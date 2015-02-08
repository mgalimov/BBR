<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Create new user">
	<jsp:body>
		<t:card method="BBRUsers" gridPage="admin-user-list.jsp" title="Create new user">
			<t:card-item label="Email" field="email" type="text" isRequired="required"/>
			<t:card-item label="First name" field="firstName" type="text" isRequired="required"/>
			<t:card-item label="Last name" field="lastName" type="text" isRequired="required"/>
			<t:card-item label="Password" field="password" type="password" isRequired="required"/>
			<t:card-item label="Role" field="role" type="select" options="1:Visitor,2:Shop specialist,4:Branch administrator,8:Shop administrator,16:Shop owner,256:BBR Service Owner" isRequired="required" />		</t:card>
	</jsp:body>
</t:admin-card-wrapper>