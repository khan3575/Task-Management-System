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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final Logger logger = LoggerFactory.getLogger(DashboardServlet.class);
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
	    	logger.warn("Invalid page number: '{}'. setting the default value to page to 1.", request.getParameter("page"));
	        page = 1;
	    }

	    int totalItems = taskService.getTaskCount();
	    int totalPages = (int) Math.ceil((double) totalItems / size);

	    if (totalPages == 0) totalPages = 1;
	    if (page > totalPages) page = totalPages;

	    logger.debug("Fetching tasks for dashboard. Page: {} / Total Pages: {}", page, totalPages);
	    List<TaskDTO> tasks = taskService.findPaginated(page, size);

	    request.setAttribute("tasks", tasks);
	    request.setAttribute("currentPage", page);
	    request.setAttribute("totalPages", totalPages);

	    boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

	    if (isAjax) {
	    	logger.debug("dashboard updated via AJAX");
	        request.getRequestDispatcher("views/components/taskTable.jsp")
	               .forward(request, response);
	    } else {
	    	logger.info("Full Dashboard page loaded and ready to access");
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
			logger.info("Dashboard Action: DELETE for Task ID: {}", request.getParameter("id"));
			int id = Integer.parseInt(request.getParameter("id"));
			request.getRequestDispatcher("deleteTask").forward(request, response);
		}
	}

}
