<%@ tag language="java" pageEncoding="UTF-8" description="Grid Button"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="icon" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<c:set var="itemsHF" scope="request" value="${itemsHF.concat('<th></th>')}"/>

<c:set var="buttonDef" value="<button type='button' class='btn btn-info btn-xs' data-btncolumn='${index}'><span class='glyphicon ${icon}' aria-hidden='true'></span>"/>

<c:set var="items" scope="request" value="${items.concat(', {orderable: false, render: 
	function(data, type, row, meta) 
		{return \"').concat(buttonDef).concat(label).concat('</button>\";}
	')}"/>

<c:set var="items" scope="request" value="${items.concat('}')}"/>

<c:set var="index" scope="request" value="${index+1}"/>
