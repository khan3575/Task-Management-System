package controller.task;

import java.io.IOException;
import java.util.List;

import dto.TaskDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import service.TaskService;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet("/searchTask")
public class SearchTaskServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    
    private static final Logger logger = LogManager.getLogger(SearchTaskServlet.class);

    private TaskService taskService = new TaskService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String column = req.getParameter("column");
        String value = req.getParameter("value");

        logger.debug("Search request received - column: {}, value: {}", column, value);

        
        if (column == null || column.trim().isEmpty() ||
            value == null || value.trim().isEmpty()) {
            req.setAttribute("error", "Invalid search input");
            req.getRequestDispatcher("/views/search.jsp").forward(req, res);
            return;
        }

        try {
            List<TaskDTO> tasks = taskService.searchTasks(column, value);

            logger.info("Search completed - column: {}, value: {}, results: {}",
                    column, value, tasks.size());

            req.setAttribute("tasks", tasks);
            req.getRequestDispatcher("/views/search.jsp").forward(req, res);

        } catch (Exception e) {
            logger.error("Error occurred while searching tasks", e);

            req.setAttribute("error", "Something went wrong while searching");
            req.getRequestDispatcher("/views/search.jsp").forward(req, res);
        }
    }
}