<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRContext context = BBRContext.getContext(request);
	String lastError = context.getLastSignInError();
	
	if (lastError == "")
		if (context.user == null)
			lastError = context.SignInByCookie(request);
		
	if (context.user != null) {
		response.sendRedirect(request.getContextPath() + "/" + context.getWelcomePage());
		return;
	}
	
	int picNum = (int)(Math.ceil(Math.random() * 16) + 1);
	
	request.setAttribute("lastError", lastError);
	request.setAttribute("bk", String.format("%03d", picNum) + ".jpg");
%>

<t:login-wrapper title="LBL_SIGN_IN_TITLE">
<jsp:body>

<body class="hold-transition login-page" style="background-image: url(bks/${bk}); background-repeat: no-repeat; background-size: cover;">
    <script>
        $(document).ready(function() {                        
	        if ('${lastError}' == '') 
	        	$('#alertMessage').addClass('hide');
        });
    </script>
    <div class="login-box">
		<div class="login-box-body" style="background: rgba(255,255,255,0.5);">
			<div style="text-align: center; padding-bottom: 10px;">
				<img src="images/barbiny50px.png" />
			</div>
			<div class="alert alert-warning alert-dismissable" id="alertMessage">
			    <button type="button" class="close" data-dismiss="alert"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
			    <div id="welcomeText">${lastError}</div>
			</div>
			<form role="form" action="BBRSignIn" method="get">
	   	    	<p class="login-box-message">${context.gs("LBL_PLEASE_SIGN_IN")}</p>
					  <div class="form-group has-feedback">
					  	<input type="email" id="email" name="email" class="form-control"
									placeholder="${context.gs('LBL_EMAIL_PLACEHOLDER')}" required
									autofocus>
					  </div>
					  <div class="form-group has-feedback">
					  	<input type="password" id="password" name="password"
									class="form-control"
									placeholder="${context.gs('LBL_PASSWORD_PLACEHOLDER')}"
									required>
					  </div>
					  	  <div class="col-xs-8" style="padding-left: 25px;">
							  <div class="checkbox icheck">
							  	<label> 
							  		<input type="checkbox" value="remember-me" name="rememberme"> ${context.gs("LBL_REMEMBER_ME")}
							  	</label>
						  	  </div>
					  	  </div>
					  	  <div class="col-xs-4" style="padding: 0px;">
					  	  	<input type="text" id="tz" name="timezone" class="hide">
						 	<button class="btn btn-primary pull-right" type="submit" id="signIn">${context.gs("LBL_SIGN_IN_BTN")}</button>
						  </div>
					  <div class="row">
					 </div>
			</form>
	   	</div>
	</div>
</body>	
</jsp:body>
</t:login-wrapper>

<script>
	$(document).ready(function () {
		var tzo = new Date().getTimezoneOffset() / 60;
		tzo = -tzo;
		if (tzo < 0)
			$("#tz").val("UTC-" + (-tzo));
		else
			$("#tz").val("UTC+" + tzo);
	})
</script>