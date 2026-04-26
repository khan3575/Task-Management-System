function confirmDelete(taskId) {
    var result = confirm("Are you sure you want to delete this task? This action cannot be undone.");
    if(result) {
        window.location.href = "DeleteTaskServlet?id=" + taskId;
    }
}