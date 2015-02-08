<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRApplication" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<% 
	BBRApplication app = BBRApplication.getApp(request);
	request.setAttribute("userName", app.user.getFirstName() + " " + app.user.getLastName());
%>
<t:admin-card-wrapper title="Control Panel">
<jsp:body>
  <p>Welcome to control panel, ${userName}</p>
</jsp:body>
</t:admin-card-wrapper>
