<%@ tag language="java" pageEncoding="UTF-8" description="Card"%>
<%@ attribute name="title" %>
<%@ attribute name="method"%>
<%@ attribute name="gridPage" %>
<%@ attribute name="buttonSave" %>
<%@ attribute name="buttonCancel" %>
<%@ attribute name="showFooter" %>
<%@ attribute name="showToolbar" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="itemids" scope="request" value="${''}"/>
<c:set var="itemReq" scope="request" value="${''}"/>
<c:set var="itemVal" scope="request" value="${''}"/>
<c:set var="itemSet" scope="request" value="${''}"/>
<c:set var="itemPreload" scope="request" value="${''}"/>
<c:set var="itemToolbar" scope="request" value="${''}"/>

<!-- http://www.onjava.com/pub/a/onjava/excerpt/jserverpages3_ch11/ -->

<!-- Modal -->
<div class="modal fade" id="sureToCancelChanges" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Cancel changes confirmation</h4>
      </div>
      <div class="modal-body">
      	 Are you sure to cancel changes? All your changes will be lost. 
  	  </div>
	  <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">Keep editing</button>
	      <button type="button" class="btn btn-warning" id="cancelChanges">Cancel changes</button>
	  </div>
    </div>
  </div>
</div>

<div class="alert alert-warning alert-dismissable hide" id="alertMessage">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <div id="welcomeText"></div>
</div>

<div class="container-fluid"  id="editForm">
<h3>${title}</h3>
<form role="form">
	<div class="panel panel-default">
	  <c:if test="${showToolbar == true || showToolbar == null}">
		  <div class="panel-heading" id="toolbar">
		  </div>
	  </c:if>
	  <div class="panel-body">
	  	<jsp:doBody/>
	  </div>
	  <c:if test="${showFooter == true || showFooter == null}">
		  <div class="panel-footer">
		  		<button type="button" class="btn btn-default" data-toggle="modal" data-target="#sureToCancelChanges"><c:if test="${buttonCancel == null}">Cancel changes</c:if><c:if test="${buttonCancel != null}">${buttonCancel}</c:if></button>
		  		<button type="button" class="btn btn-primary" id="saveChanges"><c:if test="${buttonSave == null}">Save changes</c:if><c:if test="${buttonSave != null}">${buttonSave}</c:if></button>
		  </div>
	  </c:if>
	</div>
</form>
</div>

<script>
	var saved = false;

 	$(document).ready(function () {
 		idParam = getUrlParameter('id');
		${itemPreload}
		if (idParam && idParam != 'new') 
		{
			$.get('${method}', {id : idParam, operation : 'getdata'}, function(responseText) {
				obj = $.parseJSON(responseText);
				if (obj) {
					${itemSet}
				};
			});
 		}

		$('#toolbar').html("${itemToolbar}");
		
		$('#saveChanges').click(function(event) { 
	 		idParam = getUrlParameter('id');
            ${itemVal}
    		if (!idParam || idParam == 'new')
    			op = 'create';
    		else
    			op = 'update';
    			
            $.get('${method}',
            		{id:idParam,${itemReq}operation: op}, 
            		function(responseText) { 
						saved = true; 
            			window.location.href = '${gridPage}'; 
            		}).fail(function(data) {
            					saved = false;
            					$('#welcomeText').text(data.responseText);
            					$('#alertMessage').removeClass('hide');
            				});
           });			 		

		$('#cancelChanges').click(function(event) {
			saved = true;
			$.get('${method}', {id : idParam, operation : 'cancel'});
			goBackOrTo('${gridPage}');
		});
 	});
 	
 	$(window).bind('beforeunload', function () {
 		return;
 		//if (saved) return;
 		//return "Are you sure to cancel changes? All your changes will be lost.";
 	});
</script>			      
