<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"
        import="BBRClientApp.BBRContext" %>
<%@ tag import="BBRAcc.BBRUser.BBRUserRole" %>
<%@ attribute name="title"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%  BBRContext context = BBRContext.getContext(request);
	String shopposTitle = "";
	String shopposLink = "";
	if (context.user != null) {
   		if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN)
   			if (context.user.getShop() != null) {
	   			shopposTitle = context.user.getShop().getTitle();
	   			shopposLink = "admin-pos-list.jsp";
   			}

		if (context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN)
   			if (context.user.getPos() != null) {
				shopposTitle = context.user.getPos().getShop().getTitle() + " / " + context.user.getPos().getTitle();
				shopposLink = "manager-pos-edit.jsp?id="+context.user.getPos().getId().toString();
   			}
	}
%> 

<header class="main-header">
  <a href="${context.getWelcomePage()}" class="logo">
    ${context.gs('LBL_APPLICATION_NAME')}
  </a>
  <nav class="navbar navbar-static-top" role="navigation">
  	<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
        <span class="sr-only">Toggle navigation</span>
    </a>
    <div class="navbar-custom-menu">
      <ul class="nav navbar-nav">
      	<% if (context.user.getRole() == BBRUserRole.ROLE_SHOP_ADMIN ||
      		   context.user.getRole() == BBRUserRole.ROLE_POS_ADMIN ||
      		   context.user.getRole() == BBRUserRole.ROLE_POS_SPECIALIST) { %>
        <li class="dropdown tasks-menu">
          <a href="manager-visit-create.jsp" >
            <i class="fa fa-plus-circle"></i>
          </a>
        </li>
        <li class="dropdown tasks-menu">
          <a href="manager-spec-schedule-list.jsp" >
            <i class="fa fa-calendar-o"></i>
            <span class="label label-danger" id="BBRVisits_badge_top"></span>
          </a>
        </li>
        <li class="dropdown tasks-menu">
          <a href="manager-task-list.jsp">
            <i class="fa fa-flag-o"></i>
            <span class="label label-danger" id="BBRTasks_badge_top"></span>
          </a>
        </li>
        <% } %>
     	<li class="dropdown user user-menu">
		 	<a href="<%=shopposLink%>">
	 	   		<span class="glyphicon glyphicon-globe hidden-xs" aria-hidden="true"></span>
	 	   		<span class="hidden-xs">
					<%=shopposTitle%>
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
            <!-- Menu Footer-->
            <li class="user-footer">
                <div class="pull-left">
					<a href="general-user-profile.jsp" class="btn btn-default btn-flat">
				    	<% out.println(context.gs("LBL_USER_PROFILE_BTN"));%></a>
				</div>
				<div class="pull-left">
					<a href="#" id="signOutLink" class="btn btn-default btn-flat">
	         		<% if (context.user != null) 
	         				out.println(context.gs("LBL_SIGN_OUT_BTN"));
	         		   else 
	         		   		out.println(context.gs("LBL_SIGN_IN_BTN"));%></a>
	         	</div>
            </li>
          </ul>
		</li>
	  </ul>
    </div>
  </nav>
</header>
  
<form method="post" action="BBRSignIn" id="userMenuForm"></form>

<script>
	$("#signOutLink").click(function(event) {
		$("#userMenuForm").submit();
	});
</script>