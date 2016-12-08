<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item"%>
<%@ attribute name="id" %>
<%@ attribute name="spaceBefore" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="spaceStart" value=""/>
<c:if test="${spaceBefore.equals('true')}">
	<c:set var="spaceStart" value="&nbsp;&nbsp;"/>
</c:if>

<c:set var="groupDef" value="<div class='btn-group' role='group'>&nbsp;\n"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar}${spaceStart}${groupDef}"/>

<jsp:doBody/>

<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat('</div>')}"></c:set>
