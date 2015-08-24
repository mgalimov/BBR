<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"
        import="BBRClientApp.BBRContext" %>
<%@ tag import="BBRAcc.BBRUser.BBRUserRole" %>
<%@ attribute name="title"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% BBRContext context = BBRContext.getContext(request); %> 

<div class="navbar navbar-default navbar-fixed-top" role="navigation">
   <div class="container-fluid">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar">
        <span class="sr-only">${context.gs('LBL_TOP_MENU_TOGGLE_NAV')}</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>

     <div class="navbar-header">
       <a class="navbar-brand" href="${context.getWelcomePage()}">
       		<span class="glyphicon glyphicon-home" aria-hidden="true"></span> <c:out value="${context.gs('LBL_APPLICATION_NAME')}"/>
       </a>       
     </div>
      
       <ul class="nav navbar-nav navbar-right">
       	 <li class="dropdown">
		 	<a href="#">
	 	   		<span class="glyphicon glyphicon-globe hidden-xs" aria-hidden="true"></span>
	 	   		<span class="hidden-xs">
         		<% 
         			if (context.user != null) {
         		   		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
         		   			if (context.user.getShop() != null)
    	     		   			out.println(context.user.getShop().getTitle());
         		
	         			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
         		   			if (context.user.getPos() != null)
	        	 				out.println(context.user.getPos().getShop().getTitle() + " / " + context.user.getPos().getTitle());
         			}
	        	 %>
	        	 </span>
	        </a>
		 </li>
         <li class="dropdown">
	            <a href="#" class="dropdown-toggle hidden-xs" data-toggle="dropdown" aria-expanded="false" role="button">
	         		<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
		         		<% if (context.user != null)
		         				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %>
		         		<span class="caret"></span>
	         	</a>
	       		<ul class="dropdown-menu" role="menu">
				    <li><a href="general-user-profile.jsp">
				    	<% out.println(context.gs("LBL_USER_PROFILE_BTN"));%></a></li>
				    <li class="divider"></li>
				    <li><a href="#" id="signOutLink">
	         		<% if (context.user != null) 
	         				out.println(context.gs("LBL_SIGN_OUT_BTN"));
	         		   else 
	         		   		out.println(context.gs("LBL_SIGN_IN_BTN"));%></a></li>
				</ul>
		 </li>
       </ul>
   </div>
</div> 

<form method="post" action="BBRSignIn" id="userMenuForm"></form>

<script>
	$("#signOutLink").click(function(event) {
		$("#userMenuForm").submit();
	});
</script>