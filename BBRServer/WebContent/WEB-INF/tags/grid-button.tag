<%@ tag language="java" pageEncoding="UTF-8" description="Grid Button"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="icon" %>
<%@ attribute name="condition" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="itemsHF" scope="request" value="${itemsHF.concat('<th></th>')}"/>

<c:set var="ifterm" value="" />
<c:set var="elseterm" value="" />

<c:if test="${!condition.equals('')}">
	<c:set var="ifterm" value="if (row." />
	<c:set var="ifterm" value="${ifterm.concat(condition).concat(')')}"/>
	<c:set var="elseterm" value="else return '';" />
</c:if>

<c:set var="buttonDef" value="<button type='button' class='btn btn-info btn-xs' data-btncolumn='${index}'><span class='glyphicon ${icon}' aria-hidden='true'></span>"/>

<c:set var="items" scope="request" value="${items.concat(', {orderable: false, render: 
	function(data, type, row, meta)	{
		').concat(ifterm).concat('
		  return \"').concat(buttonDef).concat(context.gs(label)).concat('</button>\";
		').concat(elseterm).concat('
	}
')}"/>

<c:set var="items" scope="request" value="${items.concat('}')}"/>

<c:set var="index" scope="request" value="${index+1}"/>
