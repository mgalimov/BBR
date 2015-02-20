<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRAdminApplication" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRAdminApplication app = BBRAdminApplication.getApp(request);
	request.setAttribute("userName", app.user.getFirstName() + " " + app.user.getLastName());
%>
<t:general-wrapper title="User profile">
<jsp:body>
 ${userName}
</jsp:body>
</t:general-wrapper>
