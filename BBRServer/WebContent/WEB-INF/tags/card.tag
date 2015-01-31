<%@ tag language="java" pageEncoding="UTF-8" description="Card Top"%>
<%@ attribute name="title" %>
<%@ attribute name="method"%>
<%@ attribute name="pageBack" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="itemids" scope="request" value="${''}"/>
<c:set var="itemReq" scope="request" value="${''}"/>
<c:set var="itemVal" scope="request" value="${''}"/>
<c:set var="itemSet" scope="request" value="${''}"/>

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

<h1>${title}</h1>	
<div class="container"  id="editForm">
  	<form class="form-horizontal">

  		<jsp:doBody/>

		<div class="form-group">
			<div class="col-sm-6">
				<button type="button" class="btn btn-default" data-toggle="modal" data-target="#sureToCancelChanges">Cancel changes</button>
				<button type="submit" class="btn btn-primary" id="saveChanges">Save changes</button>
			</div>
		</div>
	</form>
</div>

<script>
 	$(document).ready(function () {
 		idParam = getUrlParameter('id');
		if (!idParam) {
			goBackOrTo('${pageBack}');
		} else
		{
			$.get('${method}', {id : idParam, operation : 'getdata'}, function(responseText) {
				obj = $.parseJSON(responseText);
				if (obj) {
					${itemSet}
				};
			});
 		}

		$('#saveChanges').click(function(event) { 
	 		idParam = getUrlParameter('id');
            ${itemVal}
            $.get('${method}',{id:idParam,${itemReq}operation:'update'},function(responseText) { });
			goBackOrTo('${pageBack}');
           });			 		

		$('#cancelChanges').click(function(event) {
			goBackOrTo('${pageBack}');
		});
 	});
</script>			      
