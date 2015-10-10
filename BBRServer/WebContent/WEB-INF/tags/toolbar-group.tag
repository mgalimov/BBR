<%@ tag language="java" pageEncoding="UTF-8" description="Toolbar Item"%>
<%@ attribute name="id" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="groupDef" value="<div class='btn-group' role='group'>&nbsp;\n"/>
<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat(groupDef)}"/>

<jsp:doBody/>

<c:set var="itemToolbar" scope="request" value="${itemToolbar.concat('</div>')}"></c:set>
