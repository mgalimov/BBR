<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRApplication"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%
	BBRApplication app = BBRApplication.getApp(request);
	String respText = "";
	
	if (app.user == null)
		respText = app.SignInByCookie(request);
		
	if (app.user != null) {
		response.sendRedirect(request.getContextPath() + "/" + app.getWelcomePage());
		return;
	}
%>

<t:general-wrapper title="Sign in">

<jsp:body>
    <script>
        $(document).ready(function() {                        
	         	$('#alertMessage').addClass('hide');
    	    /*	$('#signIn').click(function(event) {
	                var emailAddress=$('#inputEmail').val();
	                var passwordString=$('#inputPassword').val();
	                $("#signFormPanel").addClass('hide');
	             	$.get('BBRSignIn',{email:emailAddress,password:passwordString},function(responseText) { 
		         			$("#signFormPanel").removeClass('hide');
	             			$('#welcomeText').html(responseText);        
		         			$('#alertMessage').removeClass('hide');
	                	});
	        */
            });
            
            
            $('#signUp').click(function(event) { 
                var emailAddress=$('#inputEmailSignUp').val();
                var firstNameString=$('#inputFirstNameSignUp').val();
                var lastNameString=$('#inputLastNameSignUp').val();
                var passwordString=$('#inputPasswordSignUp').val();
                var passwordStringCopy=$('#inputCopyPasswordSignUp').val();
                if (passwordString == passwordStringCopy) {
                	$("#signFormPanel").addClass('hide');
	             	$.get('BBRSignUp',{email:emailAddress,firstName:firstNameString,lastName:lastNameString,password:passwordString},function(responseText) {
		         			$("#signFormPanel").removeClass('hide');
            				$('#welcomeText').html(responseText);        
             				$('#alertMessage').removeClass('hide');
	                	});
                	}
            });
        });
        
    </script>  
   
    <div class="container-fluid col-md-6" id="signFormPanel">	
		<div class="alert alert-warning alert-dismissable" id="alertMessage">
		    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		    <div id="welcomeText"></div>
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
		<form role="form">
			<div class="panel panel-default">
			  <div class="panel-body">
				  <div class="form-group">
					 <label for="inputEmailSignUp" class="sr-only">Email address</label>
		   			 <input type="email" id="inputEmailSignUp" class="form-control" placeholder="Email address" required>
		   		  </div>
				  <div class="form-group">
		  		     <label for="inputFirstNameSignUp" class="sr-only">First Name</label>
		   			 <input type="text" id="inputFirstNameSignUp" class="form-control" placeholder="First Name" required>
		   		  </div>	 
				  <div class="form-group">
		  		     <label for="inputLastNameSignUp" class="sr-only">Last Name</label>
		             <input type="text" id="inputLastNameSignUp" class="form-control" placeholder="Last Name" required>
		          </div>
				  <div class="form-group">
		   			<label for="inputPasswordSignUp" class="sr-only">Password</label>
				    <input type="password" id="inputPasswordSignUp" class="form-control" placeholder="Password" required>
				  </div>
				  <div class="form-group">
		  		     <label for="inputCopyPasswordSignUp" class="sr-only">Password copy</label>
		             <input type="password" id="inputCopyPasswordSignUp" class="form-control" placeholder="Type password once more to check" required>
		          </div>
			  </div>
		      <div class="panel-footer">
				   <button class="btn btn-primary" type="button" id="signUp">Sign Up</button>
			  </div>
			</div>
		 </form>
		</div>

    </div> <!-- /container -->
</jsp:body>
</t:general-wrapper>
