<%@ tag language="java" pageEncoding="UTF-8" description="Actions"
		import="BBRClientApp.BBRContext"%>

<% BBRContext context = BBRContext.getContext(request); %>

<div class="dropdown">
	<div class="btn-group" role="group">
	    <button type="button" class="btn btn-default dropdown-toggle" 
	    		data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" id="actionBtnDropdown">
	      <%=context.gs("LBL_ACTION_CREATE_BTN") %>
	      <span class="caret"></span>
	    </button>
	    <ul class="dropdown-menu" aria-labbeledby="actionBtnDropdown">
	   		<jsp:doBody/>
	    </ul>
	</div>
</div>