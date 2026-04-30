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
