<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_VISITOR_TITLE">
	<jsp:body>
		<t:card method="BBRVisitors" gridPage="manager-visitor-list.jsp" title="LBL_VISITOR_TITLE" showFooter="false" showToolbar="true">
			<t:toolbar-item label="LBL_VISITOR_SHOW_MORE_BTN" id="showMoreButton" accent="btn-info"/> 
			<t:card-item label="LBL_USER_NAME" field="userName"  isDisabled="readonly" type="text"/>
			<t:card-item label="LBL_CONTACT_INFO" field="userContacts"  isDisabled="readonly" type="text"/>
			<t:card-item label="LBL_LAST_VISIT_DATE" field="lastVisitDate" isDisabled="readonly" type="text"/>
		</t:card>
		
		<script>
			$(document).ready(function() {
				$("#showMoreButton").on("click", function() {
					idParam = getUrlParameter('id');
					window.location.href = 'manager-visit-list.jsp?t=user&query=' + idParam;
				});
			});
		</script>
	</jsp:body>
</t:wrapper>