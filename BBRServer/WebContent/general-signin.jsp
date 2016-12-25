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
	
	request.setAttribute("lastError", lastError);
%>

<t:light-wrapper title="LBL_SIGN_IN_TITLE">

<jsp:body>
    <script>
        $(document).ready(function() {                        
	        if ('${lastError}' == '') 
	        	$('#alertMessage').addClass('hide');
            
            $('#passwordCopySignUp').change(function(event) { 
            	$('#signUp').prop('disabled', !($('#passwordCopySignUp').val == $('#passwordCopy').val));
            });
        });
        
    </script>  
    <div class="row" id="signFormPanel">
   		<div class="col-md-6 col-sm-10 col-xs-12 col-lg-6 col-md-offset-3 col-sm-offset-1 col-lg-offset-3">
			<div class="alert alert-warning alert-dismissable" id="alertMessage">
			    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			    <div id="welcomeText">${lastError}</div>
			</div>
			<form role="form" action="BBRSignIn" method="get">
	   	    	<h2>${context.gs("LBL_PLEASE_SIGN_IN")}</h2>
				<div class="panel panel-default" id="singInPanel">
				  <div class="panel-body">
					  <div class="form-group">
					  	<label for="email" class="sr-only">${context.gs("LBL_EMAIL")}</label>
					  	<input type="email" id="email" name="email" class="form-control" placeholder="${context.gs('LBL_EMAIL_PLACEHOLDER')}" required autofocus>
					  </div>
					  <div class="form-group">
					  	<label for="password" class="sr-only">${context.gs("LBL_PASSWORD")}</label>
					  	<input type="password" id="password" name="password" class="form-control" placeholder="${context.gs('LBL_PASSWORD_PLACEHOLDER')}" required>
					  </div>
					  <div class="checkbox">
					  	<label> 
					  		<input type="checkbox" value="remember-me" name="rememberme"> ${context.gs("LBL_REMEMBER_ME")}
					  	</label>
				  	 </div>
				  	 <input type="text" id="tz" name="timezone" class="hide">
				  </div>
			      <div class="panel-footer clearfix">
			      	 <div class="pull-right">
					 	<button class="btn btn-primary" type="submit" id="signIn">${context.gs("LBL_SIGN_IN_BTN")}</button>
					 </div>
				  </div>
				</div>
			</form>
		
<!-- 			<div class="row"> -->
<%-- 				${context.gs("LBL_NOT_REGISTERED_YET")} --%>
				
<!-- 			    <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#signUpPanel" aria-expanded="false" aria-controls="signUpPanel"> -->
<%-- 			    	<span class="glyphicon glyphicon-user" aria-hidden="true"></span> ${context.gs("LBL_SIGN_UP")} --%>
<!-- 			    </button> -->
<!-- 		    </div> -->
<!-- 			<div class="collapse"  id="signUpPanel"> -->
<!-- 				<form role="form" method="get" action="BBRSignUp"> -->
<!-- 					<div class="panel panel-default"> -->
<!-- 					  <div class="panel-body"> -->
<!-- 						  <div class="form-group"> -->
<%-- 							 <label for="emailSignUp" class="sr-only">${context.gs("LBL_EMAIL")}</label> --%>
<%-- 				   			 <input type="email" id="emailSignUp" name="email" class="form-control" placeholder="${context.gs('LBL_EMAIL_PLACEHOLDER')}" required> --%>
<!-- 				   		  </div> -->
<!-- 						  <div class="form-group"> -->
<%-- 				  		     <label for="firstNameSignUp" class="sr-only">${context.gs("LBL_FIRST_NAME")}</label> --%>
<%-- 				   			 <input type="text" id="firstNameSignUp" name="firstName" class="form-control" placeholder="${context.gs('LBL_FIRST_NAME_PLACEHOLDER')}" required> --%>
<!-- 				   		  </div>	  -->
<!-- 						  <div class="form-group"> -->
<%-- 				  		     <label for="lastNameSignUp" class="sr-only">${context.gs("LBL_LAST_NAME")}</label> --%>
<%-- 				             <input type="text" id="lastNameSignUp" name="lastName" class="form-control" placeholder="${context.gs('LBL_LAST_NAME_PLACEHOLDER')}" required> --%>
<!-- 				          </div> -->
<!-- 						  <div class="form-group"> -->
<%-- 				   			<label for="passwordSignUp" class="sr-only">${context.gs("LBL_PASSWORD")}</label> --%>
<%-- 						    <input type="password" id="passwordSignUp" name="password" class="form-control" placeholder="${context.gs('LBL_PASSWORD_PLACEHOLDER')}" required> --%>
<!-- 						  </div> -->
<!-- 						  <div class="form-group"> -->
<%-- 				  		     <label for="passwordCopySignUp" class="sr-only">${context.gs("LBL_PASSWORD_AGAIN")}</label> --%>
<%-- 				             <input type="password" id="passwordCopySignUp" name="passwordCopy" class="form-control" placeholder="${context.gs('LBL_PASSWORD_AGAIN_PLACEHOLDER')}" required> --%>
<!-- 				          </div> -->
<!-- 					  </div> -->
<!-- 				      <div class="panel-footer clearfix"> -->
<!-- 				  			<div class="pull-right"> -->
<%-- 						   		<button class="btn btn-primary" type="submit" id="signUp">${context.gs("LBL_SIGN_UP_BTN")}</button> --%>
<!-- 							</div> -->
<!-- 					  </div> -->
<!-- 					</div> -->
<!-- 				 </form> -->
<!-- 			</div> -->
<!-- 		</div> -->
    </div>
    </div>
</jsp:body>
</t:light-wrapper>

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