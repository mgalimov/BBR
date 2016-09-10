<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRContext context = BBRContext.getContext(request);
	request.setAttribute("userName", context.user.getFirstName() + " " + context.user.getLastName());
%>
<t:wrapper title="LBL_CONTROL_PANEL">
<jsp:body>
  <p>Welcome to control panel, ${userName}</p>
</jsp:body>
</t:wrapper>
