<%@ tag language="java" pageEncoding="UTF-8" description="Grid Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="sort" %>
<%@ attribute name="type" %>
<%@ attribute name="options" %>
<%@ attribute name="referenceTitleField" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="itemsHF" scope="request" value="${itemsHF.concat('<th>').concat(context.gs(label)).concat('</th>')}"/>

<c:set var="items" scope="request" value="${items.concat(',{data:\"').concat(field).concat('\", defaultContent: \"\"')}"/>

<c:if test="${sort != null && sort != ''}">
	<c:set var="sorting" scope="request" value="${sorting.concat('[').concat(index).concat(',\"').concat(sort).concat('\"],')}"/>
</c:if>

<c:if test="${type.equals('select')}">
	<c:set var="optarrK" value="" />
	<c:set var="optarrV" value="" />
	<c:forTokens items="${context.gs(options)}" delims="," var="option">
		<c:set var="optarrK" value="${optarrK.concat('\"').concat(option.split(':')[0]).concat('\",')}" />
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
			else { 
				dt = ').concat('\"\"').concat(' + data').concat(';
				index = optarrK.indexOf(dt);
				if (index != -1)
					return optarrV[index]; 
				else 
					return data;
			}
		}
	')}"/>
</c:if>

<c:if test="${type.equals('boolean')}">
	<c:set var="items" scope="request" value="${items}, render: 
		function(data, type, row, meta)	{
			if (data == true)
				return '${context.gs('OPT_BOOLEAN_TRUE_YES')}';
			else
				return '${context.gs('OPT_BOOLEAN_FALSE_NO')}';
		}
	"/>
</c:if>

<c:if test="${type.equals('multiple')}">
	<c:set var="items" scope="request" value="${items.concat(', render: 
		function(data, type, row, meta)	{
			var s = \"\";
			data.forEach(function (el) {
				s += \", \" + el.').concat(referenceTitleField).concat('; 
			});
			return s.substring(2);
		}
	')}"/>
</c:if>

<c:set var="items" scope="request" value="${items.concat('}')}"/>

<c:set var="index" scope="request" value="${index+1}"/>
