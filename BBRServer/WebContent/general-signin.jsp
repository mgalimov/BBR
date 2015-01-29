<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-card-wrapper>
    <jsp:attribute name="title">
      Sign in or Sign up
    </jsp:attribute>
	<jsp:body>

    <script>
        $(document).ready(function() {                        
	         	$('#alertMessage').hide();
    	    	$('#signIn').click(function(event) {
	                var emailAddress=$('#inputEmail').val();
	                var passwordString=$('#inputPassword').val();
	             	$.get('BBRSignIn',{email:emailAddress,password:passwordString},function(responseText) { 
	             			$('#welcomeText').html(responseText);        
		         			$('#alertMessage').show();
//	             			$().alert();
	                	});
            });
            
            
            $('#signUp').click(function(event) { 
                var emailAddress=$('#inputEmailSignUp').val();
                var firstNameString=$('#inputFirstNameSignUp').val();
                var lastNameString=$('#inputLastNameSignUp').val();
                var passwordString=$('#inputPasswordSignUp').val();
                var passwordStringCopy=$('#inputCopyPasswordSignUp').val();
                if (passwordString == passwordStringCopy) {
	             	$.get('BBRSignUp',{email:emailAddress,firstName:firstNameString,lastName:lastNameString,password:passwordString},function(responseText) {
	             			$('#welcomeText').html(responseText);        
	             			$('#alertMessage').show();
//	             			$().alert();
	                	});
                	}
            });
        });
        
    </script>  
   
    <div class="container">
		<div class="alert alert-warning alert-dismissable" id="alertMessage">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			  <div id="welcomeText"></div>
		</div>
		<form>
		  <h2>Please sign in</h2>
		  <label for="inputEmail" class="sr-only">Email address</label>
		  <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
		  <label for="inputPassword" class="sr-only">Password</label>
		  <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
		  <div class="checkbox">
		    <label>
		      <input type="checkbox" value="remember-me"> Remember me
		    </label>
		  </div>
		  <button class="btn btn-lg btn-primary btn-block" type="button" id="signIn">Sign in</button>
		</form>

		<form>
		   <h2>OR Sign up</h2>
		   <label for="inputEmailSignUp" class="sr-only">Email address</label>
		   <input type="email" id="inputEmailSignUp" class="form-control" placeholder="Email address" required>
		   <label for="inputFirstNameSignUp" class="sr-only">First Name</label>
		   <input type="text" id="inputFirstNameSignUp" class="form-control" placeholder="First Name" required>
		   <label for="inputLastNameSignUp" class="sr-only">Last Name</label>
		   <input type="text" id="inputLastNameSignUp" class="form-control" placeholder="Last Name" required>
		   <label for="inputPasswordSignUp" class="sr-only">Password</label>
		   <input type="password" id="inputPasswordSignUp" class="form-control" placeholder="Password" required>
		   <label for="inputCopyPasswordSignUp" class="sr-only">Password copy</label>
		   <input type="password" id="inputCopyPasswordSignUp" class="form-control" placeholder="Type password once more to check" required>
		   <button class="btn btn-lg btn-primary btn-block" type="button" id="signUp">Sign Up</button>
		 </form>

    </div> <!-- /container -->
	</jsp:body>
</t:admin-card-wrapper>
