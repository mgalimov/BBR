<%@ tag language="java" pageEncoding="UTF-8" description="Card Item"%>
<%@ attribute name="field" required="true" %>
<%@ attribute name="referenceMethod" %>
<%@ attribute name="referenceFieldTitle" %>
<%@ attribute name="isRequired" %>
<%@ attribute name="isDisabled" %>
<%@ attribute name="isHidden" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:set var="ft" scope="request" value="${fn:replace(field, '.', '_')}" />

<div class="form-group ${isHidden}" style="width: 160px">
	<c:set var="isDis" value="${isDisabled}" />
	<c:if test="${isDisabled.equals('readonly')}">
		<c:set var="isDis" value="disabled" />
	</c:if>

	<select class="selectized" style="display: none" id="${ft}input" ${isRequired}  ${isDis}></select>
	
	<script>
	$(document).ready(function () {
		$("#${ft}input").selectize({
		    valueField: 'id',
		    labelField: 'title',
		    searchField: 'title',
		    create: false,
		    maxOptions: 10,
		    maxItems: 1,
		    openOnFocus: true,
		    load: ${ft}LoadData,
		    render: {
		    	item: ${ft}Render,
		    	option: ${ft}Render
		    }
		});
	});

				
	var ${ft}LoadData = function (callback) {
        $.ajax({
            url: 'BBRPoSes',
            type: 'GET',
            dataType: 'json',
            data: {
                operation: 'specialList'
            },
            error: function() {
            	callback();
            },
            success: function(res) {
            	callback(res);
            }
        });
    } 
	
	var ${ft}Render = function (data, escape) {
		res = escape(data.title);
		if (data.id.charAt(0) == "s")
			res = "<b>" + res + "</b>";
		else
			res = "&nbsp;&nbsp;&nbsp;" + res;
		return "<div>" + res + "</div>"
    } 


	</script>
</div>