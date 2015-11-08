<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:admin-card-wrapper title="LBL_SHOP_TITLE">
	<jsp:body>
		<t:card method="BBRShops" gridPage="system-shop-list.jsp" title="LBL_SHOP_TITLE">
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_COUNTRY" field="country" type="text" isRequired="required"/>
		</t:card>
	</jsp:body>
</t:admin-card-wrapper>