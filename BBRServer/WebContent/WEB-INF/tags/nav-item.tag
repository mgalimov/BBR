<%@ tag language="java" pageEncoding="UTF-8" description="Navigation Item"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="href" required="true" %>
<%@ attribute name="active" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:if test="${active.equals('true')}">
	<li><a href="${href}" class="active"><c:out value="${title}"/></a></li>
	<c:otherwise>
		<li><a href="${href}"><c:out value="${title}"/></a></li>
	</c:otherwise>
</c:if>
<li><a href="${href}"><c:out value="${title}"/></a></li>
