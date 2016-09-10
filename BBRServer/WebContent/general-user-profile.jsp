<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" import="BBRClientApp.BBRContext" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRContext context = BBRContext.getContext(request);
	if (context.user == null)
		response.sendRedirect(request.getContextPath() + "/" + context.getSignInPage());
	 else
		if (request.getParameter("id") == null)
			response.sendRedirect(request.getContextPath() + "/" + context.getUserProfilePage() + "?id=" + context.user.getId().toString());
	
%>
<t:wrapper title="LBL_USER_PROFILE_TITLE">
<jsp:body>
 	<t:card title="LBL_USER_PROFILE_TITLE" gridPage="general-user-profile.jsp" method="BBRUserProfile">
 		<t:card-item type="info" field="userName" label="LBL_USER_NAME" />
 		<t:card-item type="select" field="language" label="LBL_LANGUAGE" options="OPT_LANGUAGE" />
 	</t:card>
</jsp:body>
</t:wrapper>
