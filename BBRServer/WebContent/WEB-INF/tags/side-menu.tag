<%@ tag language="java" pageEncoding="UTF-8" description="Admin Navigation Menu"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:nav>
	<t:nav-group title="Общее администрирование" level="admin">
		<t:nav-item href="admin-user-list.jsp" title="Пользователи" />
		<t:nav-item href="admin-shop-list.jsp" title="Салоны" />
		<t:nav-item href="admin-pos-list.jsp" title="Точки продаж" />
	</t:nav-group>
	<t:nav-group title="Управление" level="manager">
		<t:nav-item href="manager-spec-list.jsp" title="Специалисты" />
		<t:nav-item href="manager-proc-list.jsp" title="Процедуры" />
		<t:nav-item href="manager-task-list.jsp" title="Задачи" />
	</t:nav-group>
	<t:nav-group title="Пользователь" level="general">
		<t:nav-item href="general-plan-visit.jsp" title="Спланируйте ваш визит" />
		<t:nav-item href="general-my-visits.jsp" title="Мои визиты" />
	</t:nav-group>
</t:nav>

