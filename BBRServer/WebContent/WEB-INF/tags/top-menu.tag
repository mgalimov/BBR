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
	 	   		<span class="glyphicon glyphicon-globe" aria-hidden="true"></span>
	 	   		<span class="hidden-xs">
					<%=shopposTitle%>
	         	</span>
	        </a>
		 </li>
        
        <li class="dropdown user user-menu">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
      		<span class="glyphicon glyphicon-user" aria-hidden="true"></span>
      		<span class="hidden-xs">
	       		<% if (context.user != null)
	       				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %>
	       		<span class="caret"></span>
       		</span>
          </a>
          <ul class="dropdown-menu">
          	<li class="user-header">
          		<img src="BBRUsers?operation=pic&fld=photo&id=<%=context.user.getId()%>" class="img-circle"/>
          		<p>
	       		<% if (context.user != null)
	       				out.println(context.user.getFirstName() + " " + context.user.getLastName()); %>
          		</p>
          	</li>
          	<li class="user-body hide">
          		<div class="row">
	          		<div class="col-xs-4 text-center small"><a href="#">Test</a></div>
	          		<div class="col-xs-4 text-center small"><a href="#">Test</a></div>
	          		<div class="col-xs-4 text-center small"><a href="#">Test</a></div>
          		</div>
          	</li>
            <li class="user-footer">
                <div class="pull-left">
					<a href="general-user-profile.jsp" class="btn btn-default btn-flat">
				    	<small><% out.println(context.gs("LBL_USER_PROFILE_BTN"));%></small></a>
				</div>
				<div class="pull-right">
					<a href="#" id="signOutLink" class="btn btn-default btn-flat">
	         		<small>
	         		<% if (context.user != null) 
	         				out.println(context.gs("LBL_SIGN_OUT_BTN"));
	         		   else 
	         		   		out.println(context.gs("LBL_SIGN_IN_BTN"));%></small></a>
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