package controller.task;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import dto.TaskDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Task;
import service.TaskService;
import util.DBConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/searchTask")
public class SearchTaskServlet extends HttpServlet{
	
	private TaskService taskService = new TaskService();
	private static final Logger logger = LoggerFactory.getLogger(SearchTaskServlet.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("username") == null) {
			logger.warn("Unauthorized GET access to /searchTask redirecting to login");
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		
        String username = (String) session.getAttribute("username") ;
        
		String column = request.getParameter("column");
        String value = request.getParameter("value");

        List<TaskDTO> tasks = taskService.searchTasks(column, value);
        logger.debug("list size : "+ tasks.size());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("/views/search.jsp").forward(request, response);
    }

}
