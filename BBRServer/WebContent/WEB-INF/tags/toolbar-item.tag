<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item" import="BBRClientApp.BBRContext" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="accent" %>
<%@ attribute name="condition" %>
<%@ attribute name="spaceBefore" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<% BBRContext context = BBRContext.getContext(request); %>

<c:if test="${accent == null}">
	<c:set var="accent" value="btn-default" scope="page"/>
</c:if>

<c:set var="spaceStart" value="" scope="page"/>
<c:if test="${spaceBefore.equals('true')}">
	<c:set var="spaceStart" value="&nbsp;&nbsp;" scope="page"/>
</c:if>

<c:set var="buttonDef" value="<button type='button' class='btn ${accent}' id='${id}'>\n<span class='glyphicon ${icon}' aria-hidden='true'></span>\n${context.gs(label)}\n</button>\n"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar}${spaceStart}${buttonDef}"/>

<c:if test="${condition != null}">
	<c:set var="cond" value="       if (!("/>
	<c:set var="cond" value="${cond.concat(condition).concat('))')}"/>
	<c:set var="condPerf" value=" $('#${id}').attr('disabled', 'disabled');"/>
	<c:set var="itemToolbarCondition" scope="request" value="${itemToolbarCondition.concat('
').concat(cond).concat(condPerf)}"/>
</c:if>
