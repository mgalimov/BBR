<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-grid-wrapper title="Shops">
	<jsp:body>
		<t:grid methodFetch="BBRShowShops" methodDelete="BBRShopUpdate" 
				editPage="admin-shop-edit.jsp" createPage="admin-shop-create.jsp" title="Shops">
			<t:grid-item label="Title" field="title" sort="ascending"/>
		</t:grid>
	</jsp:body>
</t:admin-grid-wrapper>