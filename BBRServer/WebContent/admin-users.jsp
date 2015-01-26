<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:admin-wrapper>
    <jsp:attribute name="title">
      Users
    </jsp:attribute>
	<jsp:body>
	   	<div id="userGrid"> </div>
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
