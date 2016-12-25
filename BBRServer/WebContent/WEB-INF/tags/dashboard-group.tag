<%@ tag language="java" pageEncoding="UTF-8" description="Dashboard Group" import="BBRClientApp.BBRContext"%>
<%@ attribute name="title" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% BBRContext context = BBRContext.getContext(request); %>
<h4>${context.gs(title)}</h4>
<jsp:doBody/>