<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="icon" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="buttonDef" value="<button type='button' class='btn btn-info' id='${id}'><span class='glyphicon ${icon}' aria-hidden='true'></span>${label}</button>"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat('\"').concat(buttonDef).concat('\"')}"/>
