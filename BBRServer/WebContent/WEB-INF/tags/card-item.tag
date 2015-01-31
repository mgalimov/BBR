<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="label" %>
<%@ attribute name="field" %>
<%@ attribute name="type" %>
<%@ attribute name="isRequired" %>
<%@ attribute name="options" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<div class="form-group">
	<label for="${field.concat('input')}" class="col-sm-4 control-label">${label}</label>
	<div class="col-sm-6">
		<c:set var="itemids" scope="request" value="${itemids.concat(field).concat('input,')}"/>
		<c:set var="itemReq" scope="request" value="${itemReq.concat(field).concat(':').concat(field).concat('String,')}"/>
		<c:set var="itemVal" scope="request" value="${itemVal.concat('var ').concat(field).concat('String = $(\"#').concat(field).concat('input\").val();')}"/>
		<c:set var="itemSet" scope="request" value="${itemSet.concat('$(\"#').concat(field).concat('input\").')}"/>
		<c:choose>
			<c:when test="${type.equals('info')}">
				<p class="col-sm-6" id="${field.concat('input')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('text(obj.').concat(field).concat(');')}"/>
			</c:when>
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control" id="${field.concat('input')}" placeholder="${label}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('val(obj.').concat(field).concat(');')}"/>
			</c:when>
			<c:when test="${type.equals('select')}">
				<select class="form-control" id="${field.concat('input')}">
					<c:forTokens items="${options}" delims="," var="option">
						<option value="${option.split(':')[0]}">${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('val(obj.').concat(field).concat('.toString());')}"/>
			</c:when>
		</c:choose>
	</div>
</div>