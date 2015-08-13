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
	        ').concat(ft).concat('String = $(\"#').concat(ft).concat('input\").val();')}"/>
		
		<c:choose>
		
			<c:when test="${type.equals('info')}">
				<p class="form-control-static" id="${ft.concat('input')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.text(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.text(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control ${isHidden}" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('textarea')}">
				<textarea class="form-control ${isHidden}" rows="3" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}></textarea>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>			
			
			<c:when test="${type.equals('password')}">
				<input type="password" class="form-control ${isHidden}" id="${ft.concat('input')}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('time') || type.equals('datetime') || type.equals('datetime')}">
				<c:if test="${type.equals('time')}">
					<c:set var="glyphicon" value="glyphicon-time"></c:set>
					<c:set var="format" value="HH:mm"></c:set>
				</c:if>
				<c:if test="${type.equals('date')}">
					<c:set var="glyphicon" value="glyphicon-calendar"></c:set>
					<c:set var="format" value="YYYY-MM-DD"></c:set>
				</c:if>
				<c:if test="${type.equals('datetime')}">
					<c:set var="glyphicon" value="glyphicon-calendar"></c:set>
					<c:set var="format" value="YYYY-MM-DD HH:mm"></c:set>
				</c:if>

		        <div class="input-group date col-md-3" id="${ft.concat('inputdiv')}" >
           			<input id="${ft.concat('input')}" type="text" class="form-control ${isHidden}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled} />
					<span class="input-group-addon ${isHidden}">
						<span class="glyphicon ${glyphicon} ${isHidden}"></span>
					</span>
				</div>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('.val(obj.').concat(field).concat(');')}"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
					        $(\"#').concat(ft).concat('input\")')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('.val(\"').concat(defaultValue).concat('\");')}"/>
				</c:if>

				<script>
					$("#${ft.concat('inputdiv')}").datetimepicker({
						format: "${format}",
						stepping: 30,
						locale: "${context.getLocaleString()}"
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
				
				<c:set var="itemSet" scope="request" value="${itemSet.concat('
		            $(\"#').concat(ft).concat('input\")')}"/>
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

				<c:set var="itemSet" scope="request" value="${itemSet.concat('
				    el = $(\"#').concat(ft).concat('input\")[0].selectize;')}"/>
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					obj.').concat(field).concat('.forEach(function (objItem) {')}"/>
				</c:if>
				<c:if test="${!multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					objItem = obj.').concat(field).concat(';')}"/>
				</c:if>
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
						el.addOption(objItem?{id: objItem.id, ')}"/>
					<c:set var="itemSet" scope="request" value="${itemSet.concat(referenceFieldTitle).concat(': objItem.').concat(referenceFieldTitle).concat('}:{});
					')}"/>
					<c:set var="itemSet" scope="request" value="${itemSet.concat('	el.addItem(objItem?objItem.id:null);')}"/>
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet.concat('
					});
					')}"/>
				</c:if>

				<c:set var="itemSet" scope="request" value="${itemSet.concat('el.load(').concat(ft).concat('LoadInitialData);
				')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('	el.refreshOptions(false);
				')}"/>
				<c:set var="itemSet" scope="request" value="${itemSet.concat('	el.refreshItems();')}"/>


				<c:set var="itemAfterLoad" scope="request" value="${itemAfterLoad.concat('
		$(\"#').concat(ft).concat('input\")')}"/>
				<c:set var="itemAfterLoad" scope="request" value="${itemAfterLoad.concat('[0].selectize')}"/>
				<c:set var="itemAfterLoad" scope="request" value="${itemAfterLoad.concat('.load(').concat(ft).concat('LoadInitialData);')}"/>

				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('
				    	el = $(\"#').concat(ft).concat('input\")[0].selectize;
				    	')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('el.addOption({id: \"').concat(defaultValue).concat('\", ')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat(referenceFieldTitle).concat(': \"').concat(defaultDisplay).concat('\"});
					')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('el.addItem(\"').concat(defaultValue).concat('\");
					')}"/>
   					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('el.refreshOptions(false);
   					')}"/>
					<c:set var="itemPreload" scope="request" value="${itemPreload.concat('el.refreshItems();
					')}"/>
				</c:if>

				<script>
				var ${ft}SetConstrains = function () {};
				
				var ${ft}LoadInitialData = function (callback) {
			        $.ajax({
			            url: '${referenceMethod}',
			            type: 'GET',
			            dataType: 'json',
			            data: {
			                constrains: ${ft}SetConstrains,
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

				var ${ft}LoadData = function (query, callback) {
			        $.ajax({
			            url: '${referenceMethod}',
			            type: 'GET',
			            dataType: 'json',
			            data: {
			                q: query,
			                constrains: ${ft}SetConstrains,
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
				
				$("#${field.concat('input')}").selectize({
				    valueField: 'id',
				    labelField: '${referenceFieldTitle}',
				    searchField: '${referenceFieldTitle}',
				    create: false,
				    maxOptions: 10,
				    maxItems: ${mult},
				    openOnFocus: true,
				    load: ${ft}LoadData 
				});
				</script>
			</c:when>
		</c:choose>
</div>