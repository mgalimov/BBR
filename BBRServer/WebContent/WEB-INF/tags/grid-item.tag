<%@ tag language="java" pageEncoding="UTF-8" description="Grid Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="sort" %>
<%@ attribute name="type" %>
<%@ attribute name="options" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="itemsHF" scope="request" value="${itemsHF.concat('<th>').concat(label).concat('</th>')}"/>

<c:set var="items" scope="request" value="${items.concat(',{data:\"').concat(field).concat('\", defaultContent: \"\"')}"/>

<c:if test="${sort > ''}">
	<c:set var="sorting" scope="request" value="${sorting.concat('[').concat(index).concat(',\"').concat(sort).concat('\"],')}"/>
</c:if>

<c:if test="${type.equals('time')}">
	<c:set var="items" scope="request" value="${items.concat(', render: 
		function(data, type, row, meta) 
			{if (data) return data.substring(11); else return \"\";}
		')}"/>
</c:if>

<c:if test="${type.equals('select')}">
	<c:set var="optarrK" value="" />
	<c:set var="optarrV" value="" />
	<c:forTokens items="${options}" delims="," var="option">
		<c:set var="optarrK" value="${optarrK.concat(option.split(':')[0]).concat(',')}" />
		<c:set var="optarrV" value="${optarrV.concat('\"').concat(option.split(':')[1]).concat('\",')}" />
	</c:forTokens>
	<c:set var="p1" value="" />
	<c:set var="items" scope="request" value="${items.concat(', render: 
		function(data, type, row, meta)	{
			optarrK = [').concat(optarrK).concat('];
			optarrV = [').concat(optarrV).concat('];
			index = optarrK.indexOf(data);
			if (index != -1)
				return optarrV[index]; 
			else 
				return data;
		}
	')}"/>
</c:if>

<c:set var="items" scope="request" value="${items.concat('}')}"/>

<c:set var="index" scope="request" value="${index+1}"/>
