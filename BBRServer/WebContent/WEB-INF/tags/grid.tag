<%@ tag language="java" pageEncoding="UTF-8" description="Grid"%>
<%@ attribute name="title" required="true"%>
<%@ attribute name="methodFetch" required="true"%>
<%@ attribute name="methodDelete" required="true"%>
<%@ attribute name="editPage" required="true"%>

<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<c:set var="items" scope="request" value="{field: \"id\", header: \"ID\", visible: \"no\"}"/>
<c:set var="sorting" scope="request" value=""/>

<!-- http://www.onjava.com/pub/a/onjava/excerpt/jserverpages3_ch11/ -->

<h1>${title}</h1>	

<!-- Toolbar -->
<button type="button" class="btn btn-default" id="edit">
  <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Edit
</button>
<button type="button" class="btn btn-warning" id="delete" data-toggle="modal" data-target="#sureToDelete">
  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Delete
</button>
		
<!-- Modal -->
<div class="modal fade" id="sureToDelete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Confirm deletion</h4>
      </div>
      <div class="modal-body">
      	 Are you sure to delete record? You cannot restore record. 
	  </div>
	  <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-warning" id="deletionConfirmed">Delete</button>
      </div>
    </div>
  </div>
</div>

<div id="grid"></div>

<jsp:doBody/>
		
<script>
$('#edit').click(
		function(event) {
			var row = $("#grid").bs_grid('selectedRows', 'get_ids');
			if (row.length >0)
				window.location.href = '${editPage}?id=' + row;
		});

$('#deletionConfirmed').click(
		function(event) {
			var row = $("#grid").bs_grid('selectedRows', 'get_ids');
			if (row.length >0) {
				$.get('${methodDelete}', {id:row[0],operation:'delete'}, function(responseText) {
					$('#sureToDelete').modal('hide');
					$("#grid").bs_grid('displayGrid', true);
				});
			}
		});

$('#grid').bs_grid({
 			ajaxFetchDataURL: '${methodFetch}',
 	        row_primary_key: 'id',
 	    	columns: [${items}
 	    	],
 	        sorting: [
 	            ${sorting}
 	        ],
 	        useFilters: false,
 	     	bootstrap_version: '3',
 	     	pageNum: 1,
 	  		rowsPerPage: 10,
 	  		maxRowsPerPage: 100,
 	  		row_primary_key: 'id',
 	  		rowSelectionMode: 'single',
 	  		debug_mode: 'no',
     	  	onDatagridError: function(event, data) {
  	          alert(data['err_description'] + ' (' + data['err_code'] + ')');
  	    }
  	});
 		
</script>