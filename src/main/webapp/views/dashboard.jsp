<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dto.TaskDTO"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Dashboard - Task Management System</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/style.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/dashboard.css">
<script>
    var contextPath = "<%=request.getContextPath()%>";
</script>
</head>
<body>

	<%@ include file="components/navbar.jsp"%>
	<%@ include file="components/sidebar.jsp"%>

	<div class="main-content">
		<h1>Task Dashboard</h1>

		<div id="messageBox" class="success-message" style="display: none;"></div>

		<table>
			<thead>
				<tr>
					<th>ID</th>
					<th>Title</th>
					<th>Description</th>
					<th>Priority</th>
					<th>Status</th>
					<th>Due Date</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody id="taskTableBody">
				<%-- Empty on load — fetchTasks(1) fills this immediately --%>
			</tbody>
		</table>

		<div id="pagination" class="pagination"></div>
	</div>



	<script>
const API_URL = contextPath + "/dashboard";

let state = {
    currentPage: 1,
    totalPages: 1
};

function fetchTasks(page) {
    page = page || 1;
    fetch(API_URL + "?page=" + page, {
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
    .then(function(res) { return res.text(); })
    .then(function(html) {
        document.getElementById("taskTableBody").innerHTML = html;

        var meta = document.getElementById("meta");
        if (meta) {
            state.currentPage = parseInt(meta.dataset.page);
            state.totalPages  = parseInt(meta.dataset.total);
        }
        renderPagination();
    })
    .catch(function(err) { console.error(err); });
}

function renderPagination() {
    var container = document.getElementById("pagination");
    container.innerHTML = "";

    if (state.totalPages <= 1) return;

    var start = Math.max(1, state.currentPage - 2);
    var end   = Math.min(state.totalPages, state.currentPage + 2);

    if (state.currentPage > 1) {
        container.innerHTML += '<a onclick="fetchTasks(' + (state.currentPage - 1) + ')">&laquo;</a>';
    }

    if (start > 1) {
        container.innerHTML += '<a onclick="fetchTasks(1)">1</a><span>...</span>';
    }

    for (var i = start; i <= end; i++) {
        var cls = (i === state.currentPage) ? ' class="active"' : '';
        container.innerHTML += '<a onclick="fetchTasks(' + i + ')"' + cls + '>' + i + '</a>';
    }

    if (end < state.totalPages) {
        container.innerHTML += '<span>...</span><a onclick="fetchTasks(' + state.totalPages + ')">' + state.totalPages + '</a>';
    }

    if (state.currentPage < state.totalPages) {
        container.innerHTML += '<a onclick="fetchTasks(' + (state.currentPage + 1) + ')">&raquo;</a>';
    }
}

async function deleteTask(id) {
    if (!confirm("Delete this task?")) return;
    try {
        var res = await fetch(contextPath + "/deleteTask?id=" + id + "&ajax=true", {
            method: "DELETE"
        });
        if (res.ok) {
            showMessage("Task deleted", true);
            fetchTasks(state.currentPage);
        } else {
            showMessage("Delete failed", false);
        }
    } catch (e) {
        showMessage("Network error", false);
    }
}

function showMessage(msg, success) {
    var box = document.getElementById("messageBox");
    box.textContent = msg;
    box.className   = success ? "success-message" : "error-message";
    box.style.display = "block";
    setTimeout(function() { box.style.display = "none"; }, 3000);
}

// Single entry point — same path for initial load and every page change
document.addEventListener("DOMContentLoaded", function() {
    fetchTasks(1);
});
</script>

</body>
</html>