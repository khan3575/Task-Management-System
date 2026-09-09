String path = request.getServletContext().getRealPath("/logs/app.log");
AppLogger.log(
            path,
            "CREATE_TASK",
            "Task created",
            session.getAttribute("username"),
            "title= " + title
);

// log saved in : C:\Users\ADMIN\Downloads\apache-tomcat-10.1.36\wtpwebapps\Task Management System\logs