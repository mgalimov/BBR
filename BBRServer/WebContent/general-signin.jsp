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

<t:general-wrapper title="Sign in">

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
   
    <div class="container-fluid col-md-6" id="signFormPanel">	
		<div class="alert alert-warning alert-dismissable" id="alertMessage">
		    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		    <div id="welcomeText">${lastError}</div>
		</div>
		
		<form role="form" action="BBRSignIn" method="get">
   	    	<h2>Please sign in</h2>
			<div class="panel panel-default" id="singInPanel">
			  <div class="panel-body">
				  <div class="form-group">
				  	<label for="email" class="sr-only">Email address</label>
				  	<input type="email" id="email" name="email" class="form-control" placeholder="Email address" required autofocus>
				  </div>
				  <div class="form-group">
				  	<label for="password" class="sr-only">Password</label>
				  	<input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
				  </div>
				  <div class="checkbox">
				  	<label> 
				  		<input type="checkbox" value="remember-me" name="rememberme"> Remember me
				  	</label>
			  	 </div>
			  </div>
		      <div class="panel-footer">
				 <button class="btn btn-primary" type="submit" id="signIn">Sign in</button>
			  </div>
			</div>
		</form>
		
		<div class="row">
			Not registered yet?
			
		    <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#signUpPanel" aria-expanded="false" aria-controls="signUpPanel">
		    	<span class="glyphicon glyphicon-user" aria-hidden="true"></span> Sign Up
		    </button>
	    </div>

		<div class="collapse"  id="signUpPanel">
		<form role="form" method="get" action="BBRSignUp">
			<div class="panel panel-default">
			  <div class="panel-body">
				  <div class="form-group">
					 <label for="emailSignUp" class="sr-only">Email address</label>
		   			 <input type="email" id="emailSignUp" name="email" class="form-control" placeholder="Email address" required>
		   		  </div>
				  <div class="form-group">
		  		     <label for="firstNameSignUp" class="sr-only">First Name</label>
		   			 <input type="text" id="firstNameSignUp" name="firstName" class="form-control" placeholder="First Name" required>
		   		  </div>	 
				  <div class="form-group">
		  		     <label for="lastNameSignUp" class="sr-only">Last Name</label>
		             <input type="text" id="lastNameSignUp" name="lastName" class="form-control" placeholder="Last Name" required>
		          </div>
				  <div class="form-group">
		   			<label for="passwordSignUp" class="sr-only">Password</label>
				    <input type="password" id="passwordSignUp" name="password" class="form-control" placeholder="Password" required>
				  </div>
				  <div class="form-group">
		  		     <label for="passwordCopySignUp" class="sr-only">Password copy</label>
		             <input type="password" id="passwordCopySignUp" name="passwordCopy" class="form-control" placeholder="Password verification" required>
		          </div>
			  </div>
		      <div class="panel-footer">
				   <button class="btn btn-primary" type="submit" id="signUp">Sign Up</button>
			  </div>
			</div>
		 </form>
		</div>
    </div>
</jsp:body>
</t:general-wrapper>
