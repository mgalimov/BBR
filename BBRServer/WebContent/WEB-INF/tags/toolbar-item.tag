<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="accent" %>
<%@ attribute name="condition" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:if test="${accent == null}">
	<c:set var="accent" value="btn-default"/>
</c:if>
<c:set var="buttonDef" value="<button type='button' class='btn ${accent}' id='${id}'>\n<span class='glyphicon ${icon}' aria-hidden='true'></span>\n${context.gs(label)}\n</button>\n"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat(buttonDef)}"/>

<c:if test="${condition != null}">
	<c:set var="cond" value="       if (!("/>
	<c:set var="cond" value="${cond.concat(condition).concat('))')}"/>
	<c:set var="condPerf" value=" $('#${id}').attr('disabled', 'disabled');"/>
	<c:set var="itemToolbarCondition" scope="request" value="${itemToolbarCondition.concat('
').concat(cond).concat(condPerf)}"/>
</c:if>