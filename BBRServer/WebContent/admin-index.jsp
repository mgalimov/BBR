<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRContext context = BBRContext.getContext(request);
	request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
%>
<t:admin-card-wrapper title="Control Panel">
<jsp:body>
  <p>Welcome to control panel, ${userName}</p>
</jsp:body>
</t:admin-card-wrapper>
