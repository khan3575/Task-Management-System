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
	
<script src="${pageContext.request.contextPath}/js/showMessage.js"></script>
<script src="${pageContext.request.contextPath}/js/pagination.js"></script>
<script> var contextPath = "<%=request.getContextPath()%>"; </script>

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

// Show message in the messageBox container
function showMessage(msg, isSuccess) {
    var box = document.getElementById("messageBox");
    box.textContent = msg;
    box.className = isSuccess ? "success-message" : "error-message";
    box.style.display = "block";
    
    // Auto hide after 3 seconds
    setTimeout(function() { 
        box.style.display = "none"; 
    }, 3000);
}

// Check URL for status messages (for task update, delete, add)
function checkUrlForMessages() {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    const error = urlParams.get('error');
    const updated = urlParams.get('updated');
    const deleted = urlParams.get('deleted');
    const added = urlParams.get('added');
    
    if (success) {
        showMessage(success, true);
        // Remove the parameter without refreshing
        const newUrl = window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    } else if (error) {
        showMessage(error, false);
        window.history.replaceState({}, document.title, window.location.pathname);
    } else if (updated) {
        showMessage('Task has been updated successfully!', true);
        window.history.replaceState({}, document.title, window.location.pathname);
    } else if (deleted) {
        showMessage('Task has been deleted successfully!', true);
        window.history.replaceState({}, document.title, window.location.pathname);
    } else if (added) {
        showMessage('New task has been added successfully!', true);
        window.history.replaceState({}, document.title, window.location.pathname);
    }
}

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


async function deleteTask(id) {
    if (!confirm("Delete this task?")) return;
    try {
        var res = await fetch(contextPath + "/deleteTask?id=" + id + "&ajax=true", {
            method: "DELETE"
        });
        if (res.ok) {
            showMessage("Task deleted successfully!", true);
            fetchTasks(state.currentPage);
        } else {
            showMessage("Failed to delete task. Please try again.", false);
        }
    } catch (e) {
        showMessage("Network Error, Delete Failed", false);
    }
}

function updateTask(id) {
    if (!confirm("Do you want to update this task?")) return;

    window.location.href = contextPath + "/updateTask?id=" + id;
<<<<<<< Updated upstream
        //showMessage("Network error occurred. Please try again.", false);
    
}

// Function to show update success (call this from edit page or after redirect)
function showUpdateSuccess() {
    showMessage("Task has been updated successfully!", true);
}

function showUpdateError(message) {
    showMessage(message || "Failed to update task. Please try again.", false);

}

=======
}

>>>>>>> Stashed changes
// For initial load
document.addEventListener("DOMContentLoaded", function() {
    fetchTasks(1);
    checkUrlForMessages(); // Check for any status messages in URL
});

// Expose functions globally
window.deleteTask = deleteTask;
window.fetchTasks = fetchTasks;
window.showUpdateSuccess = showUpdateSuccess;
window.showUpdateError = showUpdateError;
</script>

</body>
</html>