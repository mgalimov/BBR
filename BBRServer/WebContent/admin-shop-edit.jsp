<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="Shop">
	<jsp:body>
		<t:card method="BBRShopUpdate" gridPage="admin-shop-list.jsp" title="Shop">
			<t:card-item label="Title" field="title" type="text" isRequired="required"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>