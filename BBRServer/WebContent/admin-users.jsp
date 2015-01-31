<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-grid-wrapper title="Users">
	<jsp:body>
		<!-- Button trigger modal -->
		<button type="button" class="btn btn-default" id="editUser">
		  <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Edit
		</button>
		<button type="button" class="btn btn-warning" id="deleteUser" data-toggle="modal" data-target="#sureToDelete">
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
		      	 Are you sure to delete user? You cannot restore record. 
	   		  </div>
		    <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
		        <button type="button" class="btn btn-warning" id="deletionConfirmed">Delete</button>
		    </div>
		    </div>
		  </div>
		</div>

		<div id="userGrid"></div>
		
	    <script>
			$('#editUser').click(
					function(event) {
						var row = $("#userGrid").bs_grid('selectedRows', 'get_ids');
						if (row.length >0)
							window.location.href = 'admin-user.jsp?id=' + row;
					});
	
			$('#deletionConfirmed').click(
					function(event) {
						var row = $("#userGrid").bs_grid('selectedRows', 'get_ids');
						if (row.length >0) {
							$.get('BBRUserUpdate', {id : row[0], operation : 'delete'}, function(responseText) {});
							$('#sureToDelete').modal('hide');
							$("#userGrid").bs_grid('displayGrid', true);
						}
					});

			$('#userGrid').bs_grid({
	   			ajaxFetchDataURL: 'BBRShowUsers',
	   	        row_primary_key: 'id',
	   	     	columns: [
	   	            {field: 'id', header: 'ID', visible: 'yes'},
	   	            {field: 'email', header: 'Email', visible: 'yes'},
	   	            {field: 'firstName', header: 'First Name', visible: 'yes'},
	   	            {field: 'lastName', header: 'Last Name', visible: 'yes'},
	   	            {field: 'approved', header: 'Approved', visible: 'yes'}
	   	        ],
	   	 
	   	        sorting: [
	   	            {sortingName: 'By email', field: 'email', order: 'ascending'},
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
	</jsp:body>
</t:admin-grid-wrapper>
