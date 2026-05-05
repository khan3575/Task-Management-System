package controller.task;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import dto.TaskDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Task;
import service.TaskService;
import util.AppLogger;
import util.DBConnection;

@WebServlet("/searchTask")
public class SearchTaskServlet extends HttpServlet{
	private TaskService taskService = new TaskService();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        
		String column = req.getParameter("column");
        String value = req.getParameter("value");

        List<TaskDTO> tasks = taskService.searchTasks(column, value);

        req.setAttribute("tasks", tasks);
        
        String path = req.getServletContext().getRealPath("/logs/app.log");
        AppLogger.log(
                    path,
                    "TASK SEARCHED",
                    "Search operation is done by a user",
                    (String)session.getAttribute("username"),
                    "title= search by: "+ column + " " + value
        );
        req.getRequestDispatcher("/views/search.jsp").forward(req, res);
    }

}
