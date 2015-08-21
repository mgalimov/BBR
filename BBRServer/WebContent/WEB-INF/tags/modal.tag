<%@ tag language="java" pageEncoding="UTF-8" description="Modal dialog"%>
<%@ attribute name="title" required="true" %>
<%@ attribute name="id" required="true" %>
<%@ attribute name="cancelButtonLabel" required="true" %>
<%@ attribute name="processButtonLabel" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

	<div class="modal fade" id="${id}" tabindex="-1" role="dialog" aria-labelledby="${id}title" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="X" id="${id}close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="${id}title">${context.gs(title)}</h4>
	      </div>
	      <div class="modal-body">
	     	 <jsp:doBody/> 
	  	  </div>
		  <div class="modal-footer">
		      <button type="button" class="btn btn-default" data-dismiss="modal" id="${id}cancel">${context.gs(cancelButtonLabel)}</button>
		      <button type="button" class="btn btn-warning" id="${id}process">${context.gs(processButtonLabel)}</button>
		  </div>
	    </div>
	  </div>
	</div>
