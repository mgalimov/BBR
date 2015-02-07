<%@ tag language="java" pageEncoding="UTF-8" description="Admin Top Menu"%>
<%@ attribute name="title"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
         <li><a href="#">User profile</a></li>
         <li><a href="#">Help</a></li>
       </ul>
       <form class="navbar-form navbar-right">
         <input type="text" class="form-control" placeholder="Search...">
       </form>
     </div>
   </div>
</div>
