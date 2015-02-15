<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"
        import="BBRClientApp.BBRAdminApplication" %>
<%@ attribute name="title"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% BBRAdminApplication app = BBRAdminApplication.getApp(request); %> 
<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
   <div class="container-fluid">
     <div class="navbar-header">
       <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </button>
       <a class="navbar-brand" href="#">Control Panel. <c:out value="${title}"/></a>
     </div>
     <div class="navbar-collapse collapse">
       <ul class="nav navbar-nav navbar-right">
         <li class="dropdown">
                 	<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false" role="button">
		         		<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
		         		<% if (app.user != null) out.println(app.user.getFirstName() + " " + app.user.getLastName()); %>
		         		<span class="caret"></span>
		         	</a>
	         		<ul class="dropdown-menu" role="menu">
					    <li><a href="#">Profile</a></li>
					    <li><a href="#">Settings</a></li>
					    <li class="divider"></li>
					    <li><a href="#" id="signOutLink">Sign Out</a></li>
					</ul>
		 </li>
         <li><a href="#">Help</a></li>
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