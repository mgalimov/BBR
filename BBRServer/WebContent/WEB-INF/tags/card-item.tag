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
<%@ attribute name="modifier" %>
<%@ attribute name="currencyField" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ft" scope="request" value="${fn:replace(field, '.', '_')}" />
<c:set var="ft" scope="request" value="${ft}${modifier}" />
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="form-group ${isHidden}">
	<label for="${ft}input">${context.gs(label)}</label>
	<c:set var="itemids" scope="request" value="${itemids}
				${ft}input,"/>
	<c:set var="itemReq" scope="request" value="${itemReq}
				${ft}:${ft}String,"/>
	<c:set var="itemVal" scope="request" value="${itemVal}
				${ft}String = $('#${ft}input').val();"/>
		
		<c:choose>
			<c:when test="${type.equals('info')}">
				<p class="form-control-static" id="${ft}input"/>
				<c:set var="itemSet" scope="request" value="${itemSet}
					$('#${ft}').text(obj.${field});"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
						$('#${ft}input').text('${defaultValue}');"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('text')}">
				<input type="text" class="form-control ${isHidden}" id="${ft}input" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input').val(obj.${field});"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
   					  $('#${ft}input').val('${defaultValue}');"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('textarea')}">
				<textarea class="form-control ${isHidden}" rows="3" id="${ft}input" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}></textarea>
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input').val(obj.${field});"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
   					  $('#${ft}input').val('${defaultValue}');"/>
				</c:if>
			</c:when>	
			
			<c:when test="${type.equals('password')}">
				<input type="password" class="form-control ${isHidden}" id="${ft}input" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input').val(obj.${field});"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
   					  $('#${ft}input').val('${defaultValue}');"/>
				</c:if>
			</c:when>
			
			<c:when test="${type.equals('time') || type.equals('datetime') || type.equals('date')}">
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

		        <div class="input-group date col-md-3" id="${ft}inputdiv" >
           			<input id="${ft}input" type="text" class="form-control ${isHidden}" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled} />
					<span class="input-group-addon ${isHidden}">
						<span class="glyphicon ${glyphicon} ${isHidden}"></span>
					</span>
				</div>
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input').val(obj.${field});"/>
				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
   					  $('#${ft}input').val('${defaultValue}');"/>
				</c:if>

				<script>
					$("#${ft}inputdiv").datetimepicker({
						format: "${format}",
						stepping: 30,
						locale: "${context.getLocaleString()}"
					});
				</script>
			</c:when>

			<c:when test="${type.equals('number')}">
				<div class="row">
					<span class="col-md-6 col-lg-5 col-sm-6">
						<input type="number" class="form-control ${isHidden}" style="display: inline-block !important" id="${ft}input" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
					</span>
					<c:set var="itemSet" scope="request" value="${itemSet}
			            $('#${ft}input').val(obj.${field});"/>
					<c:if test="${defaultValue != null}">
						<c:set var="itemPreload" scope="request" value="${itemPreload}
	   					  $('#${ft}input').val('${defaultValue}');"/>
					</c:if>
				</div>
			</c:when>
			
			<c:when test="${type.equals('money')}">
				<div class="row">
					<span class="col-md-6 col-lg-5 col-sm-6">
						<input type="number" class="form-control ${isHidden}" style="display: inline-block !important" id="${ft}input" placeholder="${context.gs(label)}" ${isRequired} ${isDisabled}/>
					</span>
					<c:set var="itemSet" scope="request" value="${itemSet}
			            $('#${ft}input').val(obj.${field});"/>
					<c:if test="${defaultValue != null}">
						<c:set var="itemPreload" scope="request" value="${itemPreload}
	   					  $('#${ft}input').val('${defaultValue}');"/>
					</c:if>
	
					<c:set var="ftc" scope="request" value="${fn:replace(currencyField, '.', '_')}" />
					<c:set var="ftc" scope="request" value="${ftc}${modifier}" />
					
					<c:set var="itemids" scope="request" value="${itemids}
						${ftc}input,"/>
					<c:set var="itemReq" scope="request" value="${itemReq}
						${ftc}:${ftc}String,"/>
					<c:set var="itemVal" scope="request" value="${itemVal}
						${ftc}String = $('#${ftc}input').val();"/>
					
					<c:set var="isDis" value="${isDisabled}" />
					<c:if test="${isDisabled.equals('readonly')}">
						<c:set var="isDis" value="disabled" />
					</c:if>
	
					<c:set var="opts" scope="request" value="${context.gs('OPT_CURRENCIES')}" />
					<span class="col-md-4 col-lg-3 col-sm-4" style="padding: 0 !important; margin: 0 !important; top: -4px; position: relative">
						<select class="selectize" style="display:none" id="${ftc}input" ${isRequired}  ${isDis}>
							<c:forTokens items="${opts}" delims="," var="option">
								<c:set var="selected" value="" />
								<c:if test="${defaultValue != null && defaultValue.equals(option.split(':')[0])}">
									<c:set var="selected" value="selected" />
								</c:if>
								<option value="${option.split(':')[0]}" ${selected}>${option.split(':')[1]}</option>
							</c:forTokens>
						</select>
					</span>
				</div>
				
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ftc}input')[0].selectize.addItem([obj.${currencyField}]);"/>
				
				<script>
				$("#${ftc}input").selectize({
				    openOnFocus: true
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

				<select class="selectized" style="display: none" id="${ft}input" ${isRequired}  ${isDis}>
					<c:forTokens items="${opts}" delims="," var="option">
						<c:set var="selected" value="" />
						<c:if test="${defaultValue != null && defaultValue.equals(option.split(':')[0])}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${option.split(':')[0]}" ${selected}>${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input')[0].selectize.addItem([obj.${field}]);"/>
				
				<script>
				$("#${ft}input").selectize({
				    openOnFocus: true,
				    maxItems: ${mult}
				 });
				</script>
			</c:when>

			<c:when test="${type.equals('boolean')}">
				<c:set var="isDis" value="${isDisabled}" />
				<c:if test="${isDisabled.equals('readonly')}">
					<c:set var="isDis" value="disabled" />
				</c:if>

				<c:set var="opts" scope="request" value="true:${context.gs('OPT_BOOLEAN_TRUE_YES')},false:${context.gs('OPT_BOOLEAN_FALSE_NO')}" />

				<select class="selectized" style="display: none" id="${ft}input" ${isRequired}  ${isDis}>
					<c:forTokens items="${opts}" delims="," var="option">
						<c:set var="selected" value="" />
						<c:if test="${defaultValue != null && defaultValue.equals(option.split(':')[0])}">
							<c:set var="selected" value="selected" />
						</c:if>
						<option value="${option.split(':')[0]}" ${selected}>${option.split(':')[1]}</option>
					</c:forTokens>
				</select>
				
				<c:set var="itemSet" scope="request" value="${itemSet}
		            $('#${ft}input')[0].selectize.addItem([obj.${field}]);"/>
				
				<script>
				$("#${ft}input").selectize({
				    openOnFocus: true
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
				<select class="selectized" style="display: none" id="${ft}input" ${isRequired}  ${isDis}>
				</select>

				<c:set var="itemSet" scope="request" value="${itemSet}
				    el = $('#${ft}input')[0].selectize;"/>
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet}
					obj.${field}.forEach(function (objItem) {')}"/>
				</c:if>
				<c:if test="${!multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet}
					objItem = obj.${field};"/>
				</c:if>
					<c:set var="itemSet" scope="request" value="${itemSet}
						el.addOption(objItem?{id: objItem.id, ${referenceFieldTitle}: objItem.${referenceFieldTitle}}:{});"/>
					<c:set var="itemSet" scope="request" value="${itemSet}
						el.addItem(objItem?objItem.id:null);"/>
				<c:if test="${multiple.equals('true')}">
					<c:set var="itemSet" scope="request" value="${itemSet}
					});"/>
				</c:if>

				<c:set var="itemSet" scope="request" value="${itemSet}
				    el.load(${ft}LoadInitialData);
				    el.refreshOptions(false);
				    el.refreshItems();"/>

				<c:set var="itemAfterLoad" scope="request" value="${itemAfterLoad}
					$('#${ft}input')[0].selectize.load(${ft}LoadInitialData);"/>

				<c:if test="${defaultValue != null}">
					<c:set var="itemPreload" scope="request" value="${itemPreload}
				    	el = $('#${ft}input')[0].selectize;
						el.addOption({id: '${defaultValue}', ${referenceFieldTitle}: '${defaultDisplay}'});
						el.addItem(${defaultValue});
						el.refreshOptions(false);
						el.refreshItems();"/>
				</c:if>

				<script>
				var ${ft}SetConstrains = function () { return "";};
				
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
