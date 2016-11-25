<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<t:wrapper title="LBL_SHOP_TITLE">
	<jsp:body>
		<t:card method="BBRShops" gridPage="system-shop-list.jsp" title="LBL_SHOP_TITLE">
			<t:card-item label="LBL_TITLE" field="title" type="text" isRequired="required"/>
			<t:card-item label="LBL_COUNTRY" field="country" type="text" isRequired="required"/>
			<t:card-item label="LBL_TIMEZONE" field="timeZone" type="select" options="OPT_TIMEZONES"/>
		</t:card>
	</jsp:body>
</t:wrapper>

<script>
	$("#shopinput").on("change", function () {
		shopId = $("#shopinput").val();
		$.get("BBRShops",
				{	
					id: shopId,
					operation: "getdata"
				}
			).done(function (data) {
				shop = $.parseJSON(data);
				el = $("#timeZoneinput")[0].selectize;
				el.addItem(shop.timeZone);
			});
	});
</script>