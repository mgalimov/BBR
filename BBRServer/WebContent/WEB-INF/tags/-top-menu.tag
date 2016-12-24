<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"
        import="BBRClientApp.BBRContext" %>
<%@ tag import="BBRAcc.BBRUser.BBRUserRole" %>
<%@ attribute name="title"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% BBRContext context = BBRContext.getContext(request); %> 

<header class="header">
  <a href="${context.getWelcomePage()}" class="logo">
    ${context.gs('LBL_APPLICATION_NAME')}
  </a>
  <nav class="navbar navbar-static-top" role="navigation">
    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
        <li class="dropdown tasks-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
            <i class="fa fa-flag-o"></i>
            <span class="label label-danger">9</span>
          </a>
        </li>
     	<li class="dropdown user user-menu">
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
        
        <li class="dropdown user user-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
      		<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
       		<% if (context.user != null)
       				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %>
       		<span class="caret"></span>
          </a>
          <ul class="dropdown-menu">
            <li class="user-header">
              <img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
              <p>
                Alexander Pierce - Web Developer
                <small>Member since Nov. 2012</small>
              </p>
            </li>
            <!-- Menu Body -->
            <li class="user-body">
            </li>
            <!-- Menu Footer-->
            <li class="user-footer">
				    <li><a href="general-user-profile.jsp">
				    	<% out.println(context.gs("LBL_USER_PROFILE_BTN"));%></a></li>
				    <li class="divider"></li>
				    <li><a href="#" id="signOutLink">
	         		<% if (context.user != null) 
	         				out.println(context.gs("LBL_SIGN_OUT_BTN"));
	         		   else 
	         		   		out.println(context.gs("LBL_SIGN_IN_BTN"));%></a></li>
            </li>
          </ul>
		</li>
	  </ul>
    </div>
  </nav>
</header>
  
  
<!-- <div class="navbar navbar-default navbar-fixed-top" role="navigation"> -->
<!--    <div class="container-fluid"> -->
<!--    	  <button type="button" class="navbar-toggle" data-toggle="offcanvas" data-target="#navmenu" data-canvas="body"> -->
<!-- 	    <span class="icon-bar"></span> -->
<!-- 	    <span class="icon-bar"></span> -->
<!-- 	    <span class="icon-bar"></span> -->
<!-- 	  </button> -->
   
<!--      <div class="navbar-header"> -->
<%--        <a class="navbar-brand" href="${context.getWelcomePage()}"> --%>
<%--        		<span class="glyphicon glyphicon-home" aria-hidden="true"></span> <c:out value="${context.gs('LBL_APPLICATION_NAME')}"/> --%>
<!--        </a>        -->
<!--      </div> -->
      
<!--        <ul class="nav navbar-nav navbar-right"> -->
<!--        	 <li class="dropdown"> -->
<!-- 		 	<a href="#"> -->
<!-- 	 	   		<span class="glyphicon glyphicon-globe hidden-xs" aria-hidden="true"></span> -->
<!-- 	 	   		<span class="hidden-xs"> -->
<%--          		<%  --%>
//          			if (context.user != null) {
//          		   		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
//          		   			if (context.user.getShop() != null)
//     	     		   			out.println(context.user.getShop().getTitle());
         		
// 	         			if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
//          		   			if (context.user.getPos() != null)
// 	        	 				out.println(context.user.getPos().getShop().getTitle() + " / " + context.user.getPos().getTitle());
//          			}
<%-- 	        	 %> --%>
<!-- 	        	 </span> -->
<!-- 	        </a> -->
<!-- 		 </li> -->
<!--          <li class="dropdown"> -->
<!-- 	            <a href="#" class="dropdown-toggle hidden-xs" data-toggle="dropdown" aria-expanded="false" role="button"> -->
<!-- 	         		<span class="glyphicon glyphicon-user" aria-hidden="true"></span> -->
<%-- 		         		<% if (context.user != null) --%>
<%-- 		         				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %> --%>
<!-- 		         		<span class="caret"></span> -->
<!-- 	         	</a> -->
<!-- 	       		<ul class="dropdown-menu" role="menu"> -->
<!-- 				    <li><a href="general-user-profile.jsp"> -->
<%-- 				    	<% out.println(context.gs("LBL_USER_PROFILE_BTN"));%></a></li> --%>
<!-- 				    <li class="divider"></li> -->
<!-- 				    <li><a href="#" id="signOutLink"> -->
<%-- 	         		<% if (context.user != null)  --%>
// 	         				out.println(context.gs("LBL_SIGN_OUT_BTN"));
// 	         		   else 
<%-- 	         		   		out.println(context.gs("LBL_SIGN_IN_BTN"));%></a></li> --%>
<!-- 				</ul> -->
<!-- 		 </li> -->
<!--        </ul> -->
<!--    </div> -->
<!-- </div>  -->

<form method="post" action="BBRSignIn" id="userMenuForm"></form>

<script>
	$("#signOutLink").click(function(event) {
		$("#userMenuForm").submit();
	});
</script>