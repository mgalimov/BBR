<%@ tag language="java" pageEncoding="UTF-8" description="Admin Navigation Menu"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:nav>
	<t:nav-group title="LBL_DASHBOARD_MENU" level="manager" faicon="dashboard">
		<t:nav-item href="manager-dashboard.jsp" title="LBL_DASHBOARD_MANAGER_MENU" />
		<t:nav-item href="system-dashboard.jsp" title="LBL_DASHBOARD_SYSTEM_MENU" />
		<t:nav-item href="admin-dashboard.jsp" title="LBL_DASHBOARD_ADMIN_MENU"/>
	</t:nav-group>
	<t:nav-group title="LBL_SYSTEM_ADMINISTRATION_MENU" level="system" faicon="cogs">
		<t:nav-item href="system-user-list.jsp" title="LBL_USERS_MENU" />
		<t:nav-item href="system-shop-list.jsp" title="LBL_SHOPS_MENU" />
		<t:nav-item href="system-service-list.jsp" title="LBL_SERVICES_MENU" />
		<t:nav-item href="system-job-list.jsp" title="LBL_JOBS_MENU" />
	</t:nav-group>
	<t:nav-group title="LBL_GENERAL_ADMINISTRATION_MENU" level="admin" faicon="laptop">
		<t:nav-item href="admin-pos-list.jsp" title="LBL_POSES_MENU" />
		<t:nav-item href="admin-user-list.jsp" title="LBL_SHOP_USERS_MENU" />
		<t:nav-item href="admin-subscription-list.jsp" title="LBL_SUBSCR_MENU" />
	</t:nav-group>
	<t:nav-group title="LBL_MAIN_REFS_MENU" level="admin" faicon="book">
		<t:nav-item href="admin-stock-group-list.jsp" title="LBL_STOCK_GROUPS_MENU" />
		<t:nav-item href="admin-stock-item-list.jsp" title="LBL_STOCK_ITEMS_MENU" />
	</t:nav-group>
	<t:nav-group title="LBL_POS_MANAGEMENT_MENU" level="manager" faicon="globe">
		<t:nav-item href="manager-spec-list.jsp" title="LBL_SPECS_MENU" />
		<t:nav-item href="manager-turn-list.jsp" title="LBL_TURNS_MENU" />
		<t:nav-item href="manager-proc-group-list.jsp" title="LBL_PROC_GROUPS_MENU" />
		<t:nav-item href="manager-proc-list.jsp" title="LBL_PROCEDURES_MENU" />
		<t:nav-item href="manager-promo-list.jsp" title="LBL_PROMOS_MENU" />
		<t:nav-item href="manager-task-list.jsp" title="LBL_TASKS_MENU" badge="true" badgeMethod="BBRTasks"/>
		<t:nav-item href="manager-visitor-list.jsp" title="LBL_VISITORS_LIST_MENU"/>
		<t:nav-item href="manager-spec-schedule-list.jsp" title="LBL_SPEC_SCHEDULE_MENU" badge="true" badgeMethod="BBRVisits"/>
		<t:nav-item href="manager-visit-create.jsp" title="LBL_CREATE_VISIT_MENU"/>
	</t:nav-group>
	<t:nav-group title="LBL_USER_MENU" level="general" faicon="user">
		<t:nav-item href="general-plan-visit.jsp" title="LBL_PLAN_YOUR_VISIT_MENU" />
	</t:nav-group>
</t:nav>

