package controller.task;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TaskService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.TaskDTO;

/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    TaskService taskService = new TaskService();

	    int page = 1;
	    int size = 5;

	    try {
	        String pageParam = request.getParameter("page");
	        if (pageParam != null) {
	            page = Integer.parseInt(pageParam);
	            if (page < 1) page = 1;
	        }
	    } catch (Exception e) {
	        page = 1;
	    }

	    int totalItems = taskService.getTaskCount();
	    int totalPages = (int) Math.ceil((double) totalItems / size);

	    if (totalPages == 0) totalPages = 1;
	    if (page > totalPages) page = totalPages;

	    List<TaskDTO> tasks = taskService.findPaginated(page, size);

	    request.setAttribute("tasks", tasks);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalPages", totalPages);

	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

	    if (isAjax) {
	        request.getRequestDispatcher("views/components/taskTable.jsp")
	               .forward(request, response);
	    } else {
	        request.getRequestDispatcher("views/dashboard.jsp")
	               .forward(request, response);
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");

		if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			request.getRequestDispatcher("deleteTask").forward(request, response);
		}
	}

}
