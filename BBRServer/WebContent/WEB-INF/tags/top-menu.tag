<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"
        import="BBRClientApp.BBRContext" %>
<%@ attribute name="title"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% BBRContext context = BBRContext.getContext(request); %> 
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
   <div class="container-fluid">
     <div class="navbar-header">
       <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </button>
       <a class="navbar-brand" href="#">BBR Service <c:out value="${title}"/></a>
     </div>
     <div class="navbar-collapse collapse">
       <ul class="nav navbar-nav navbar-right">
         <li class="dropdown">
                 	<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false" role="button">
		         		<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
		         		<% if (context.user != null) 
		         				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %>
		         		<span class="caret"></span>
		         	</a>
	         		<ul class="dropdown-menu" role="menu">
					    <li><a href="general-user-profile.jsp">Profile and Settings</a></li>
					    <li class="divider"></li>
					    <li><a href="#" id="signOutLink">
		         		<% if (context.user != null) 
		         				out.println("Sign Out");
		         		   else 
		         		   		out.println("Sign In");%></a></li>
					</ul>
		 </li>
       </ul>
       <form class="navbar-form navbar-right">
         <input type="text" class="form-control" placeholder="Search...">
       </form>
     </div>
   </div>
</div>

<form method="post" action="BBRSignIn" id="userMenuForm"></form>

<script>
	$("#signOutLink").click(function(event) {
		$("#userMenuForm").submit();
	});
</script>