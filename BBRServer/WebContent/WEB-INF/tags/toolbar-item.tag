<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="accent" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:if test="${accent == null}">
	<c:set var="accent" value="btn-default"/>
</c:if>
<c:set var="buttonDef" value="<button type='button' class='btn ${accent}' id='${id}'>\n<span class='glyphicon ${icon}' aria-hidden='true'></span>\n${label}\n</button>\n"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat(buttonDef)}"/>
