<%@ tag language="java" pageEncoding="UTF-8" description="Grid"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="titleModifier" %>
<%@ attribute name="method" required="true"%>
<%@ attribute name="editPage" required="true"%>
<%@ attribute name="createPage" required="true"%>
<%@ attribute name="customToolbar" %>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="items" scope="request" value="{data: \"id\", visible: false}"/>
<c:set var="itemsHF" scope="request" value="<th>ID</th>"/>
<c:set var="sorting" scope="request" value=""/>
<c:set var="index" scope="request" value="1"/>
<c:set var="itemToolbar" scope="request" value="${''}"/>

<!-- http://www.onjava.com/pub/a/onjava/excerpt/jserverpages3_ch11/ -->

<h3>${context.gs(title).concat(titleModifier)}</h3>
		
<!-- Modal -->
<div class="modal fade" id="sureToDelete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">${context.gs('LBL_GRID_CONFIRM_DELETION_TITLE')}</h4>
      </div>
      <div class="modal-body">
      	 ${context.gs('MSG_GRID_CONFIRM_DELETION')} 
	  </div>
	  <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">${context.gs('LBL_GRID_CONFIRM_DELETION_CANCEL_BTN')}</button>
        <button type="button" class="btn btn-warning" id="deletionConfirmed">${context.gs('LBL_GRID_CONFIRM_DELETION_CONFIRM_BTN')}</button>
      </div>
    </div>
  </div>
</div>

<div class="panel">

  <div class="panel-heading" id="toolbar">
  	<div class="btn-toolbar" role="toolbar" aria-label="..." id="toolbarpanel">
  	  <c:if test="${customToolbar != true}">
  	  	<div class='btn-group' role='group' aria-label='...'>
			<button type="button" class="btn btn-default" id="create">
			  <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> ${context.gs('LBL_GRID_CREATE_RECORD_BTN')}
			</button>
			<button type="button" class="btn btn-info" id="edit">
			  <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> ${context.gs('LBL_GRID_EDIT_RECORD_BTN')}
			</button>
			<button type="button" class="btn btn-warning" id="delete" data-toggle="modal" data-target="#sureToDelete">
			  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> ${context.gs('LBL_GRID_DELETE_RECORD_BTN')}
			</button>
		</div>
	  </c:if>
	 </div>
  </div>
  <div class="panel-body">
	  <table id="grid" class="table table-stripped table-bordered no-footer noselection">
			<jsp:doBody/>
			<thead>
				<tr>
					<c:out value="${itemsHF}" escapeXml="false"/>
				</tr>
			</thead>
	  </table>
  </div>
</div>

<c:if test="${customToolbar == true}">
<script>
	$(document).ready(function() {
		$('#toolbarpanel').html("${itemToolbar}");
	});
</script>
</c:if>

<script>
	$(document).ready(function() {
		$('#create').click(
				function(event) {
					window.location.href = '${createPage}?id=new';
				});
		
		$('#edit').click(
				function(event) {
					var row = table.row('.success');
					if (row.length > 0)
						window.location.href = '${editPage}?id=' + row.data().id;
				});
		
		$('#deletionConfirmed').click(
				function(event) {
					var row = table.row('.success');
					if (row.length > 0) {
						$.get('${method}', {id:row.data().id,operation:'delete'}, function(responseText) {
							$('#sureToDelete').modal('hide');
							table.draw();
						});
					}
				});
		
		var table = $('#grid').DataTable({
		 			ajax: {
		 				url: '${method}',
		 				type: 'POST'
		 			},
		 			columns: [${items}],
		 	    	order: [${sorting}],
		 	    	serverSide: true,
		 	    	lengthChange: false,
		 	    	searching: false,
		 	    	language: {
		 	    		url: 'js/localization/grid_${context.getLocaleString()}.json'
		 	    	}
			  	});
	
	    $('#grid').on( 'click', 'tbody tr', function () {
            table.$('tr.success').removeClass('success');
            $(this).addClass('success');
	    });
	    
	    $('#grid').on( 'dblclick', 'tbody tr', function (e) {
	    	$("#edit").click();
	    	e.stopPropagation();
	    } );
	});
	</script>
