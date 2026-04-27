package controller.task;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Task;
import service.TaskService;
import util.DBConnection;

@WebServlet("/searchTask")
public class SearchTaskServlet extends HttpServlet{
	private TaskService taskService = new TaskService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		String column = req.getParameter("column");
        String value = req.getParameter("value");

        List<Task> tasks = taskService.searchTasks(column, value);

        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("search.jsp").forward(req, res);
    }

}
