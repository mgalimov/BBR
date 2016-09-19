<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="isActive" %>
<%@ attribute name="combined" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="active" value="" />
<c:if test="${isActive.equals('true')}">
	<c:set var="active" value="active" />
</c:if>

<c:set var="combinedCss" value="" />
<c:if test="${combined.equals('true')}">
	<c:set var="combinedCss" value="combined" />
</c:if>

<c:set var="itemLi" value="<li role='presentation' class='${active} ${combinedCss}'><a href='#${id}' aria-controls='${id}' role='tab' data-toggle='tab' id='#${id}link'>${context.gs(label)}</a></li>"/>

<c:set var="itemTabs" scope="request" value="${itemTabs.concat(itemLi)}"/>

<div role="tabpanel" class="tab-pane ${active} ${combinedCss}" id="${id}">
	<jsp:doBody/>
</div>