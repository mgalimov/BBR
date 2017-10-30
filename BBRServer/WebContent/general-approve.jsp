<%@page import="BBR.BBRDataSet"%>
<%@page import="BBRAcc.BBRUser.BBRUserRole"%>
<%@page import="BBRAcc.BBRUser"%>
<%@page import="BBRAcc.BBRUserManager"%>
<%@page import="BBR.BBRUtil"%>
<%@page import="BBRClientApp.BBRContext"%>
<%@page import="BBRClientApp.BBRParams"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	BBRContext context = BBRContext.getContext(request);
	BBRParams params = new BBRParams(request.getQueryString());
	
	String sUserId = params.get("userId");
	String sCode = params.get("code");
	
	try {
		Long userId = Long.parseLong(sUserId);
		BBRUserManager mgr = new BBRUserManager();
		BBRUser user = mgr.findById(userId);
		if (user != null) {
			user = mgr.checkUserForApproval(user, sCode);
			request.setAttribute("user", user);
		}
	} catch (Exception ex) {
		
	}
	
%>

<t:login-wrapper title="LBL_SELECT_POS_TITLE">
<jsp:body>

<body class="hold-transition login-page" style="background-image: url(bks/${bk}); background-repeat: no-repeat; background-size: cover; background-position: center;">
    <div class="center-box">
		<div class="login-box-body" style="background: rgba(255,255,255,0.7);">
			<div style="text-align: center; padding-bottom: 10px;">
				<img src="images/barbiny50px.png" />
			</div>
			<h3 id="head">${if (user != null) context.gs("LBL_START")}</h3>
			<div id="main">
				<a href="#" class="btn btn-primary pull-right" id="nextBtn">${context.gs("BTN_NEXT")}</a>
				<div class="row">
				</div>
			</div>
		</div>
	</div>
</body>

</jsp:body>
</t:login-wrapper>