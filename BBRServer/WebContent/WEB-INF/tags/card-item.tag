<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="referenceMethod" %>
<%@ attribute name="referenceFieldTitle" %>
<%@ attribute name="isRequired" %>
<%@ attribute name="isDisabled" %>
<%@ attribute name="options" %>
<%@ attribute name="defaultValue" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
	
<div class="form-group">
	<label for="${field.concat('input')}">${label}</label>
		<c:set var="itemids" scope="request" value="${itemids.concat('
		      ').concat(field).concat('input,')}"/>
		<c:set var="itemReq" scope="request" value="${itemReq.concat('
		      ').concat(field).concat(':').concat(field).concat('String,')}"/>
		<c:set var="itemVal" scope="request" value="${itemVal.concat('
	            var ').concat(field).concat('String = $(\"#').concat(field).concat('input\").val();')}"/>
		<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(field).concat('input\")')}"/>
		<c:if test="${defaultValue != null}">
			<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
			        $(\"#').concat(field).concat('input\")')}"/>
		</c:if>
		<c:choose>
			<c:when test="${type.equals('info')}">
				<p class="form-control-static" id="${field.concat('input')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.text(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.text(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control" id="${field.concat('input')}" placeholder="${label}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			<c:when test="${type.equals('password')}">
				<input type="password" class="form-control" id="${field.concat('input')}" placeholder="${label}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			<c:when test="${type.equals('select')}">
				<select class="form-control" id="${field.concat('input')}" ${isRequired} ${isDisabled}>
					<c:forTokens items="${options}" delims="," var="option">
						<option value="${option.split(':')[0]}">${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat('.toString());')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			<c:when test="${type.equals('reference')}">
				<select class="selectized" style="display: none" id="${field.concat('input')}" ${isRequired}  ${isDisabled}>
				</select>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.addOption({id: obj.').concat(field).concat('.id').concat(', ')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat(referenceFieldTitle).concat(': obj.').concat(field).concat('.').concat(referenceFieldTitle).concat('});')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(field).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.addItem(obj.').concat(field).concat('.id').concat(');')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(field).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize.refreshOptions(false);')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(field).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize.refreshItems();')}"/>


				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
				$(\"#').concat(field).concat('input\")')}"/>
				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.load(function(callback){$.ajax({url:\"').concat(referenceMethod).concat('\",type:\"GET\",dataType:\"json\",data:{q:\"\",operation:\"reference\"},error:function(){callback();},success:function(res){callback(res.page_data);}})});')}"/>

				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.addOption({id: \"').concat(defaultValue).concat('\", ')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat(referenceFieldTitle).concat(': \"').concat(defaultValue).concat('\"});')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(field).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.addItem(\"').concat(defaultValue).concat('\");')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(field).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize.refreshOptions(false);')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(field).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize.refreshItems();')}"/>
				</c:if>

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