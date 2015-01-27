<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-wrapper>
    <jsp:attribute name="title">
      Users
    </jsp:attribute>
	<jsp:body>
		<!-- Button trigger modal -->
		<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
		  Edit user
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
		        ...
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
