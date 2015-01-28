<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-wrapper>
    <jsp:attribute name="title">
      Users
    </jsp:attribute>
	<jsp:body>
		<!-- Button trigger modal -->
		<button type="button" class="btn btn-default" id="editUser">
		  <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span> Edit
		</button>
		<button type="button" class="btn btn-warning" id="deleteUser">
		  <span class="glyphicon glyphicon-trash" aria-hidden="true"></span> Delete
		</button>
		
		<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="myModalLabel">Edit user</h4>
		      </div>
		      <div class="modal-body">
				<form class="form-horizontal">
		        	<div class="form-group">
						<label for="inputEmailSignUp" class="col-sm-4 control-label">Email address</label>
						<p class="col-sm-6" id="inputEmail">
							<!--input type="email" id="inputEmail" class="form-control-static" placeholder="Email address" required>-->
						</p>
					</div>
					<div class="form-group">	
						<label for="inputFirstName" class="col-sm-4 control-label">First Name</label>
						<div class="col-sm-6">
							<input type="text" id="inputFirstName" class="form-control" placeholder="First Name" required>
						</div>
					</div>
					<div class="form-group">
						<label for="inputLastName" class="col-sm-4 control-label">Last Name</label>
						<div class="col-sm-6">
							<input type="text" id="inputLastName" class="form-control" placeholder="Last Name" required>
						</div>
					</div>
					<div class="form-group">
						<label for="inputApprovedStatus" class="col-sm-4 control-label">Status</label>
						<div class="col-sm-6">
							<select class="form-control" id="inputApproved">
							  <option value="true">Approved</option>
							  <option value="false">Not yet approved</option>
							</select>
						</div>
					</div>
				</form>		      
			</div>
		    <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
		        <button type="button" class="btn btn-primary">Save changes</button>
		    </div>
		    </div>
		  </div>
		</div>

		<div id="userGrid"></div>
		
		
	    <script>
		    $('#editUser').click(function(event) {
			    	var row = $("#userGrid").bs_grid('selectedRows', 'get_ids');
	             	$.get('BBRUserUpdate',{id:row[0],operation:'getdata'},function(responseText) {
		             		obj = $.parseJSON(responseText);
		             		if (obj) {
		             			$('#inputEmail').text(obj.email);
		             			$('#inputFirstName').val(obj.firstName);
		             			$('#inputLastName').val(obj.lastName);
		             			$('#inputApproved').val(obj.approved.toString());
		             		}
		             		$('#myModal').modal('show');
	                	});
	            	});

		    $('#deleteUser').click(function(event) {
			    	var row = $("#userGrid").bs_grid('selectedRows', 'get_ids');
	             	$.get('BBRUserUpdate',{id:row[0],operation:'delete'},function(responseText) {
	                	});
	            	});

	   		$("#userGrid").bs_grid({
	   			ajaxFetchDataURL: "BBRShowUsers",
	   	        row_primary_key: "id",
	   	     	columns: [
	   	            {field: "id", header: "ID", visible: "yes"},
	   	            {field: "email", header: "Email", visible: "yes"},
	   	            {field: "firstName", header: "First Name", visible: "yes"},
	   	            {field: "lastName", header: "Last Name", visible: "yes"},
	   	            {field: "approved", header: "Approved", visible: "yes"}
	   	        ],
	   	 
	   	        sorting: [
	   	            {sortingName: "By email", field: "email", order: "ascending"},
	   	        ],
	   	        useFilters: false,
	   	     	bootstrap_version: "3",
	   	     	pageNum: 1,
	   	  		rowsPerPage: 10,
	   	  		maxRowsPerPage: 100,
	   	  		row_primary_key: "id",
	   	  		rowSelectionMode: "single",
	   	  		debug_mode: "no",
		   	  	onDatagridError: function(event, data) {
		   	      alert(data["err_description"] + ' (' + data["err_code"] + ')');
		   	    }
	    	});
	   		
	   		//$.post('BBRShowShops', {}, function(responseText) {alert("Hi!"); alert(responseText);});
	   		
	    </script>
	</jsp:body>
</t:admin-wrapper>
