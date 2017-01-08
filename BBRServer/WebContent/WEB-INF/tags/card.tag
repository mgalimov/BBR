<%@ tag language="java" pageEncoding="UTF-8" description="Card"%>
<%@ attribute name="title" %>
<%@ attribute name="method"%>
<%@ attribute name="gridPage" %>
<%@ attribute name="buttonSave" %>
<%@ attribute name="buttonCancel" %>
<%@ attribute name="showFooter" %>
<%@ attribute name="showToolbar" %>
<%@ attribute name="showTabs" %>

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
<c:set var="itemTabs" scope="request" value="${''}"/>
<c:set var="imageItemIds" scope="request" value="${''}"/>
<c:set var="cardMethod" scope="request" value="${method}"/>

<div class="container-fluid"  id="editForm">
<h3>${context.gs(title)}</h3>
<t:modal  cancelButtonLabel="LBL_CANCEL_CHANGES_KEEP_EDITING_BTN" 
		  processButtonLabel="LBL_CANCEL_CHANGES_CANCEL_BTN" 
		  title="LBL_CANCEL_CHANGES_TITLE" 
		  id="sureToCancelChanges">
</t:modal>

<div class="alert alert-warning hide" id="alertMessage">
    <button type="button" class="close" aria-label="Close" id="alertCloseBtn"><span aria-hidden="true">&times;</span></button>
    <div id="alertText"></div>
</div>
<form role="form">
	<div class="panel panel-default">
	  <c:if test="${showTabs == true}">
		  <ul class="nav nav-tabs" role="tablist" id="tablist"></ul>
	  </c:if>
	  <c:if test="${showToolbar == true || showToolbar == null}">
		  <div class="panel-heading" id="toolbar">
	  		<c:if test="${showFooter == true || showFooter == null}">
	  			<button type="button" class="btn btn-default" id="saveChangesTop"><c:if test="${buttonSave == null}"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span></c:if><c:if test="${buttonSave != null}"><span class="hidden-xs">${context.gs(buttonSave)}</span></c:if></button>
	  		</c:if>
		    <span id="toolbarpanel"></span>		  	
	  		<c:if test="${showFooter == true || showFooter == null}">
		  		<button type="button" class="btn btn-link pull-right" id="cancelChangesTop"><c:if test="${buttonCancel == null}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></c:if><c:if test="${buttonCancel != null}"><span class="hidden-xs">${context.gs(buttonCancel)}</span></c:if></button>
	  		</c:if>
		  </div>
	  </c:if>
	  <div class="panel-body">
	  	<div class="tab-content" id="tabcontent">
	  		<jsp:doBody/>
	  	</div>
	  </div>
	  <c:if test="${showFooter == true || showFooter == null}">
		  <div class="panel-footer clearfix">
		  	<div class="pull-right">
		  		<button type="button" class="btn btn-default" id="cancelChanges"><c:if test="${buttonCancel == null}">${context.gs('LBL_CANCEL_CHANGES_CANCEL_BTN')}</c:if><c:if test="${buttonCancel != null}">${context.gs(buttonCancel)}</c:if></button>
		  		<button type="button" class="btn btn-primary" id="saveChanges"><c:if test="${buttonSave == null}">${context.gs('LBL_CANCEL_CHANGES_SAVE_BTN')}</c:if><c:if test="${buttonSave != null}">${context.gs(buttonSave)}</c:if></button>
		  	</div>
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
<c:if test="${showTabs == true}">
<script>
	$(document).ready(function() {
		$('#tablist').html("${itemTabs}");
	});
</script>
</c:if>

<script>
	var saved = false;
	var needToGoToGrid = true;
	var obj;

	function saveChanges() {
		needToGoToGrid = false;
		$('#saveChanges').click();
	}

	function saveFailed(data) {
		saved = false;
		$('#alertText').text(data.responseText);
		$("#saveChanges").prop("disabled", false);
		$("#cancelChanges").prop("disabled", false);
		$('#alertMessage').removeClass('hide');
    	$('html body').animate({
        	scrollTop: 0 
    	}, 200);
    	needToGoToGrid = true;			
	}
	
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
		
	
		function saveChangesBtnClick(event) { 
		 		idParam = getUrlParameter('id');
	            ${itemVal}
	    		if (!idParam || idParam == 'new')
	    			op = 'create';
	    		else
	    			op = 'update';
	
	    		$("div.form-group.has-error").each(function (i) {
	   				$(this).removeClass("has-error");
	    		});
	
	    		$("#saveChanges").attr("disabled", "disabled");
	    		$("#saveChangesTop").attr("disabled", "disabled");
	    		$("#cancelChanges").attr("disabled", "disabled");
	    		$("#cancelChangesTop").attr("disabled", "disabled");
	    		
	    		var hasErrors = false; 
	    		$("*[required]").each(function (i) {
	    			if ($(this).val() == "") {
	    				$(this).parents("div.form-group").addClass("has-error");
	    				if ($(this).parents("div.tab-pane")) {
		    				var errTabId = $(this).parents("div.tab-pane").attr("id");
		    				$("a[aria-controls='" + errTabId + "']").addClass("has-error");
	    				}
	    				hasErrors = true;
	    			}
	    		});
	   		
	    		if (hasErrors) {
	     
	    			$("#saveChanges").prop("disabled", false);
	    			$("#saveChangesTop").prop("disabled", false);
		    		$("#cancelChanges").prop("disabled", false);
		    		$("#cancelChangesTop").prop("disabled", false);
		    		$('#alertMessage').text('${context.gs("ERR_FILL_REQUIRED_FIELDS")}');
					$('#alertMessage').removeClass('hide');
				    $('html body').animate({
				        scrollTop: 0 
				    }, 200);    			
	    		} else
		            $.get('${method}', 
		            	{
		            		id:idParam,${itemReq}
		              		operation: op
		            	}, 
		              	function(responseText) { 
		            		var fdata = new FormData();
		            		var fids = '${imageItemIds}';
		            		$.each(fids.split(','), function(i, fid) {
		            			if (fid != "") {
		            				file = $('#' + fid).prop('files')[0]; 
		            		    	fdata.append(idParam + "#" + fid, file);
		            			}
		            		});
		            		$.ajax('${method}',
		            			{
		            			   data: fdata,
		            			   dataType: 'text',
		            			   cache: false,
		                           contentType: false,
		                           processData: false,
		                           type: 'post',
		            			   success: function (responseText) {
		        						saved = true; 
		        						if (needToGoToGrid)
		        							goToGrid('${gridPage}');
		        						needToGoToGrid = true;
		            				},
		            			   error: saveFailed
		            			});
		              	}).fail(saveFailed);
	    }
	
		function cancelChangesBtnClick() {
			saved = true;
			$.get('${method}', {id : idParam, operation : 'cancel'});
			goToGrid('${gridPage}');
		}
		
		$('#saveChanges').click(saveChangesBtnClick);
		$('#saveChangesTop').click(saveChangesBtnClick);
		$('#cancelChanges').click(cancelChangesBtnClick);
		$('#cancelChangesTop').click(cancelChangesBtnClick);
		$('#alertCloseBtn').click(function () {
			$('#alertMessage').addClass("hide");			
		});
 	});
 	
 	$(window).bind('beforeunload', function () {
 		return;
 		//if (saved) return;
 		//return "Are you sure to cancel changes? All your changes will be lost.";
 	});
</script>			      
