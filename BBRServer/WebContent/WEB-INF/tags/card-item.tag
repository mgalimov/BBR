<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="label" required="true" %>
<%@ attribute name="field" required="true" %>
<%@ attribute name="type" required="true" %>
<%@ attribute name="referenceMethod" %>
<%@ attribute name="referenceFieldTitle" %>
<%@ attribute name="isRequired" %>
<%@ attribute name="isDisabled" %>
<%@ attribute name="isHidden" %>
<%@ attribute name="options" %>
<%@ attribute name="defaultValue" %>
<%@ attribute name="defaultDisplay" %>
<%@ attribute name="multiple" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ft" scope="request" value="${fn:replace(field, '.', '_')}" />
<div class="form-group">
	<label for="${ft.concat('input')}">${context.gs(label)}</label>
		<c:set var="itemids" scope="request" value="${itemids.concat('
		      ').concat(ft).concat('input,')}"/>
		<c:set var="itemReq" scope="request" value="${itemReq.concat('
		      ').concat(ft).concat(':').concat(ft).concat('String,')}"/>
		<c:set var="itemVal" scope="request" value="${itemVal.concat('
	            var ').concat(ft).concat('String = $(\"#').concat(ft).concat('input\").val();')}"/>
		<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
		<c:choose>
			<c:when test="${type.equals('info')}">
				<p class="form-control-static" id="${ft.concat('input')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.text(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.text(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control ${isHidden}" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('textarea')}">
				<textarea class="form-control ${isHidden}" rows="3" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}></textarea>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>			
			
			<c:when test="${type.equals('password')}">
				<input type="password" class="form-control ${isHidden}" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('time')}">
		        <div class="input-group bootstrap-timepicker col-md-2">
           			<input id="${ft.concat('input')}" type="text" class="form-control ${isHidden}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled} />
					<span class="input-group-addon ${isHidden}">
						<i class="glyphicon glyphicon-time ${isHidden}"></i>
					</span>
				</div>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
				<script>
					$("#${ft.concat('input')}").timepicker({
						minuteStep: 30,
						showMeridian: false
					});
				</script>
			</c:when>
			
			<c:when test="${type.equals('select')}">
				<c:set var="isDis" value="${isDisabled}" />
				<c:if test="${isDisabled.equals('readonly')}">
					<c:set var="isDis" value="disabled" />
				</c:if>

				<c:set var="mult" value="1" />
				<c:if test="${multiple.equals('true')}">
					<c:set var="mult" value="50" />
				</c:if>
				
				<c:set var="opts" scope="request" value="${context.gs(options)}" />

				<select class="selectized" style="display: none" id="${ft.concat('input')}" ${isRequired}  ${isDis}>
					<c:forTokens items="${opts}" delims="," var="option">
						<c:set var="selected" value="" />
						<c:if test="${defaultValue != null && defaultValue.equals(option.split(':')[0])}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${option.split(':')[0]}" ${selected}>${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize.setValue([obj.').concat(field).concat(']);')}"/>
				
				<script>
				$("#${field.concat('input')}").selectize({
				    openOnFocus: true,
				    maxItems: ${mult}
				 });
				</script>
			</c:when>
			
			<c:when test="${type.equals('reference')}">
				<c:set var="isDis" value="${isDisabled}" />
				<c:if test="${isDisabled.equals('readonly')}">
					<c:set var="isDis" value="disabled" />
				</c:if>

				<c:set var="mult" value="1" />
				<c:if test="${multiple.equals('true')}">
					<c:set var="mult" value="50" />
				</c:if>
				<select class="selectized" style="display: none" id="${ft.concat('input')}" ${isRequired}  ${isDis}>
				</select>

				<c:set var="itemSet" scope="request" value="${itemSet.concat(';')}"/>
				
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					obj.').concat(field).concat('.forEach(function (objItem) {')}"/>
				</c:if>
				<c:if test="${!multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					objItem = obj.').concat(field).concat(';')}"/>
				</c:if>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.addOption(objItem?{id: objItem.id, ')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat(referenceFieldTitle).concat(': objItem.').concat(referenceFieldTitle).concat('}:{});')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.addItem(objItem?objItem.id:null);')}"/>
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					});
					  ')}"/>
				</c:if>


				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize.refreshOptions(false);')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('[0].selectize.refreshItems();')}"/>


				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					$(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
				<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.load(function(callback){$.ajax({url:\"').concat(referenceMethod).concat('\",type:\"GET\",dataType:\"json\",data:{q:\"\",constrains: function() {').concat(ft).concat('SetConstrains();}, operation:\"reference\"},error:function(){callback();},success:function(res){callback(res.data);}})});')}"/>

				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
						$(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.addOption({id: \"').concat(defaultValue).concat('\", ')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat(referenceFieldTitle).concat(': \"').concat(defaultDisplay).concat('\"});')}"/>

					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.addItem(\"').concat(defaultValue).concat('\");')}"/>

					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize.refreshOptions(false);')}"/>

					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					    $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('[0].selectize.refreshItems();')}"/>
				</c:if>

				<script>
				$("#${field.concat('input')}").selectize({
				    valueField: 'id',
				    labelField: '${referenceFieldTitle}',
				    searchField: '${referenceFieldTitle}',
				    create: false,
				    maxOptions: 10,
				    maxItems: ${mult},
				    openOnFocus: true,
				    load: function(query, callback) {
				        $.ajax({
				            url: '${referenceMethod}',
				            type: 'GET',
				            dataType: 'json',
				            data: {
				                q: query,
				                constrains: function() { return ${ft}SetConstrains(); },
				                operation: 'reference'
				            },
				            error: function() {
				                callback();
				            },
				            success: function(res) {
				            	callback(res.data);
				            }
				        });
				    }
				});
				
				function ${ft}SetConstrains() {
					return "";
				}
				</script>
			</c:when>
		</c:choose>
</div>