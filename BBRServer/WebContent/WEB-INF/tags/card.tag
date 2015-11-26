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
<c:set var="itemAfterLoad" scope="request" value="${''}"/>
<c:set var="itemToolbar" scope="request" value="${''}"/>
<c:set var="itemToolbarCondition" scope="request" value="${''}"/>

<t:modal  cancelButtonLabel="LBL_CANCEL_CHANGES_KEEP_EDITING_BTN" 
		  processButtonLabel="LBL_CANCEL_CHANGES_CANCEL_BTN" 
		  title="LBL_CANCEL_CHANGES_TITLE" 
		  id="sureToCancelChanges">
</t:modal>

<div class="alert alert-warning alert-dismissable hide" id="alertMessage">
    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
    <div id="alertText"></div>
</div>

<div class="container-fluid"  id="editForm">
<h3>${context.gs(title)}</h3>
<form role="form">
	<div class="panel panel-default">
	  <c:if test="${showToolbar == true || showToolbar == null}">
		  <div class="panel-heading" id="toolbar">
		    <div class="btn-toolbar" role="toolbar" aria-label="..." id="toolbarpanel">
		  	</div>
		  </div>
	  </c:if>
	  <div class="panel-body">
	  	<div class="col-md-10 col-sm-10 col-lg-8">
	  		<jsp:doBody/>
	  	</div>
	  </div>
	  <c:if test="${showFooter == true || showFooter == null}">
		  <div class="panel-footer">
		  		<button type="button" class="btn btn-default" id="cancelChanges"><c:if test="${buttonCancel == null}">${context.gs('LBL_CANCEL_CHANGES_CANCEL_BTN')}</c:if><c:if test="${buttonCancel != null}">${context.gs(buttonCancel)}</c:if></button>
		  		<button type="button" class="btn btn-primary" id="saveChanges"><c:if test="${buttonSave == null}">${context.gs('LBL_CANCEL_CHANGES_SAVE_BTN')}</c:if><c:if test="${buttonSave != null}">${context.gs(buttonSave)}</c:if></button>
		  </div>
	  </c:if>
	</div>
</form>
</div>

<c:if test="${showToolbar == true || showToolbar == null}">
<script>
	$(document).ready(function() {
		$('#toolbarpanel').html("${itemToolbar}");
	});
</script>
</c:if>

<script>
	var saved = false;
	var obj;

 	$(document).ready(function () {
 		idParam = getUrlParameter('id');
 		if (idParam == "")
 			idParam = 0;
		${itemPreload}
		if (idParam && idParam != 'new') 
		{
			$.get('${method}', {id : idParam, operation : 'getdata'}, function(responseText) {
				obj = $.parseJSON(responseText);
				if (obj) {
					${itemSet}
					
		    		${itemToolbarCondition}
				};
			});
 		} else {
			${itemAfterLoad}
 		}
		
		$('#saveChanges').click(function(event) { 
	 		idParam = getUrlParameter('id');
            ${itemVal}
    		if (!idParam || idParam == 'new')
    			op = 'create';
    		else
    			op = 'update';
    			
            $.get('${method}',
             {id:idParam,${itemReq}
              operation: op}, 
              function(responseText) { 
				saved = true; 
				goToGrid('${gridPage}');
              }).fail(function(data) {
				saved = false;
				$('#alertText').text(data.responseText);
				$('#alertMessage').removeClass('hide');
			    $('html body').animate({
			        scrollTop: 0 
			    }, 200);
              });
           });			 		

		$('#cancelChanges').click(function(event) {
			saved = true;
			$.get('${method}', {id : idParam, operation : 'cancel'});
			goToGrid('${gridPage}');
		});
		
 	});
 	
 	$(window).bind('beforeunload', function () {
 		return;
 		//if (saved) return;
 		//return "Are you sure to cancel changes? All your changes will be lost.";
 	});
</script>			      
