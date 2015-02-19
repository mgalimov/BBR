<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="referenceMethod" %>
<%@ attribute name="referenceFieldTitle" %>
<%@ attribute name="isRequired" %>
<%@ attribute name="options" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<div class="form-group">
	<label for="${field.concat('input')}">${label}</label>
		<c:set var="itemids" scope="request" value="${itemids.concat(field).concat('input,')}"/>
		<c:set var="itemReq" scope="request" value="${itemReq.concat(field).concat(':').concat(field).concat('String,')}"/>
		<c:set var="itemVal" scope="request" value="${itemVal.concat('var ').concat(field).concat('String = $(\"#').concat(field).concat('input\").val();')}"/>
		<c:set var="itemSet" scope="request" value="${itemSet.concat('$(\"#').concat(field).concat('input\").')}"/>
		<c:choose>
			<c:when test="${type.equals('info')}">
				<p class="form-control-static" id="${field.concat('input')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('text(obj.').concat(field).concat(');')}"/>
			</c:when>
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control" id="${field.concat('input')}" placeholder="${label}" ${isRequired}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('val(obj.').concat(field).concat(');')}"/>
			</c:when>
			<c:when test="${type.equals('password')}">
				<input type="password" class="form-control" id="${field.concat('input')}" placeholder="${label}" ${isRequired}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('val(obj.').concat(field).concat(');')}"/>
			</c:when>
			<c:when test="${type.equals('select')}">
				<select class="form-control" id="${field.concat('input')}" ${isRequired}>
					<c:forTokens items="${options}" delims="," var="option">
						<option value="${option.split(':')[0]}">${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('val(obj.').concat(field).concat('.toString());')}"/>
			</c:when>
			<c:when test="${type.equals('reference')}">
				<select class="selectized" style="display: none" id="${field.concat('input')}" ${isRequired}>
				</select>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('options = [{value: obj.').concat(field).concat('.id').concat(', ')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('text: obj.').concat(field).concat('.').concat(referenceFieldTitle).concat('}];')}"/>
				<script>
				$("#${field.concat('input')}").selectize({
				    valueField: 'id',
				    labelField: '${referenceFieldTitle}',
				    searchField: '${referenceFieldTitle}',
				    create: false,
				    maxOptions: 10,
				    openOnFocus: true,
				    load: function(query, callback) {
				        $.ajax({
				            url: '${referenceMethod}',
				            type: 'GET',
				            dataType: 'json',
				            data: {
				                q: query,
				                operation: 'reference'
				            },
				            error: function() {
				                callback();
				            },
				            success: function(res) {
				                callback(res.page_data);
				            }
				        });
				    }
				});
				</script>
			</c:when>
		</c:choose>
</div>