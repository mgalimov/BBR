<%@ tag language="java" pageEncoding="UTF-8" description="Grid Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="sort" %>
<%@ attribute name="type" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="items" scope="request" value="${items.concat(',{data:\"').concat(field).concat('\"}')}"/>
<c:set var="itemsHF" scope="request" value="${itemsHF.concat('<th>').concat(label).concat('</th>')}"/>

<c:if test="${sort > ''}">
	<c:set var="sorting" scope="request" value="${sorting.concat('[').concat(index).concat(',\"').concat(sort).concat('\"],')}"/>
</c:if>

<c:set var="index" scope="request" value="${index+1}"/>
