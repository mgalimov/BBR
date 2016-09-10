<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
		 import="BBRClientApp.BBRContext"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:wrapper title="LBL_ERROR_TITLE">
<jsp:body>
		<div class="row">
			${context.gs("ERR_SYSTEM")}
	    </div>
</jsp:body>
</t:wrapper>
