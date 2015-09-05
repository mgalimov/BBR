<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Group" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% BBRContext context = BBRContext.getContext(request); %>

<div class="panel panel-default">
  <div class="panel-heading">
  	<h3 class="panel-title">${context.gs(title)}</h3>
  </div>
  <div class="panel-body">
  	<jsp:doBody/>
  </div>
</div>
