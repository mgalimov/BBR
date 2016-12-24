<%@ tag language="java" pageEncoding="UTF-8" description="Navigation"%>

<aside class="main-sidebar">
	<section class="sidebar">
		<ul class="sidebar-menu">
	   		<jsp:doBody/>
		</ul>
	</section>>
</aside>

<script>
	$(document).ready(function () {
		$("li.active").parents("li.treeview").addClass("active");
	});
</script>