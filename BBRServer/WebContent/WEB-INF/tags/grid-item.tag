<%@ tag language="java" pageEncoding="UTF-8" description="Grid Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="sort" %>
<%@ attribute name="type" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="items" scope="request" value="${items.concat(',{field:\"').concat(field).concat('\",').concat('header:\"').concat(label).concat('\",visible: \"yes\"}')}"/>

<c:if test="${sort > ''}">
	<c:set var="sorting" scope="request" value="${sorting.concat('{sortingName:\"').concat(label).concat('\",').concat('field:\"').concat(field).concat('\",order: \"').concat(sort).concat('\"},')}"/>
</c:if>