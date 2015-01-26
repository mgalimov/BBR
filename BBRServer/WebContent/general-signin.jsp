<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign In or Sign Up</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-1.11.2.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
  
    <script>
        $(document).ready(function() {                        
            $('#signIn').click(function(event) {
                var emailAddress=$('#inputEmail').val();
                var passwordString=$('#inputPassword').val();
             	$.get('BBRSignIn',{email:emailAddress,password:passwordString},function(responseText) { 
	            	    alert(responseText);
             			$('#welcomeText').text(responseText);        
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
		            	    alert(responseText);
	             			$('#welcomeText').text(responseText);        
	                	});
                	}
            });
        });
        
    </script>  
   
    <div class="container">

      <form class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <div id="welcomeText"></div>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> Remember me
          </label>
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit" id="signIn">Sign in</button>
      </form>

     <form class="form-signup">
        <h2 class="form-signop-heading">OR Sign up</h2>
        <div id="welcomeSignUpText"></div>
        <label for="inputEmailSignUp" class="sr-only">Email address</label>
        <input type="email" id="inputEmailSignUp" class="form-control" placeholder="Email address" required>
        <label for="inputFirstNameSignUp" class="sr-only">Email address</label>
        <input type="text" id="inputFirstNameSignUp" class="form-control" placeholder="First Name" required>
        <label for="inputLastNameSignUp" class="sr-only">Email address</label>
        <input type="text" id="inputLastNameSignUp" class="form-control" placeholder="Last Name" required>
        <label for="inputPasswordSignUp" class="sr-only">Password</label>
        <input type="password" id="inputPasswordSignUp" class="form-control" placeholder="Password" required>
        <label for="inputCopyPasswordSignUp" class="sr-only">Password copy</label>
        <input type="password" id="inputCopyPasswordSignUp" class="form-control" placeholder="Type password once more to check" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit" id="signUp">Sign Up</button>
      </form>

    </div> <!-- /container -->
    
  </body>
</html>