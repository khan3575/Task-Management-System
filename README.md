# Task Management System

TaskManagementSystem/

│
├── src/
│
│   ├── controller/
│   │   ├── auth/ (Mehedi)
│   │   │   ├── LoginServlet.java
│   │   │   └── LogoutServlet.java
│   │   │
│   │   ├── task/
│   │   │   ├── DashboardServlet.java (Mahmud)
│   │   │   ├── AddTaskServlet.java (Fahim)
│   │   │   ├── UpdateTaskServlet.java (Fahim)
│   │   │   ├── DeleteTaskServlet.java (Mehedi)
│   │   │   └── SearchTaskServlet.java (Neky)
│
│   ├── dto/
│   │   ├── UserDTO.java (Sakib)
│   │   └── TaskDTO.java (Team)
│   │        ├── forDashboard() (Mahmud)
│   │        ├── forAddTask() (Fahim)
│   │        ├── forUpdateTask() (Fahim)
│   │        ├── forDeleteTask() (Mehedi)
│   │        └── forSearchTask() (Neky)
│
│   ├── model/
│   │   ├── User.java (Mehedi)
│   │   └── Task.java (Mehedi)
│
│   ├── dao/
│   │   ├── UserDAO.java (Team)
│   │   └── TaskDAO.java (Team)
│   │        ├── getDashboardTasks() (Mahmud)
│   │        ├── addTask() (Fahim)
│   │        ├── updateTask() (Fahim)
│   │        ├── deleteTask() (Mehedi)
│   │        └── searchTask() (Neky)
│
│   ├── service/
│   │   ├── UserService.java (Team)
│   │   └── TaskService.java (Team)
│   │        ├── getDashboard() (Mahmud)
│   │        ├── addTask() (Fahim)
│   │        ├── updateTask() (Fahim)
│   │        ├── deleteTask() (Mehedi)
│   │        └── searchTask() (Neky)
│
│   ├── validator/
│   │   ├── UserValidator.java (Sakib)
│   │   └── TaskValidator.java (Team)
│   │        ├── validateAdd() (Fahim)
│   │        ├── validateUpdate() (Fahim)
│   │        └── validateBasic() (Team)
│
│   ├── mapper/
│   │   └── TaskMapper.java (Team)
│
│   ├── util/
│   │   └── DBConnection.java (Mehedi)
│
│   ├── filter/
│   │   └── AuthFilter.java (Neky)
│
├── webapp/
│   ├── views/
│   │   ├── login.jsp (Mehedi)
│   │   ├── home.jsp (Mehedi)
│   │   ├── dashboard.jsp (Fahim)
│   │   ├── addTask.jsp (Mahmud)
│   │   ├── editTask.jsp (Fahim)
│   │   └── search.jsp (Neky)
│
│   ├── components/
│   │   ├── navbar.jsp (Sakib)
│   │   └── sidebar.jsp (Sakib)
│
│   ├── css/
│   ├── js/
│   ├── images/
│   │   ├── team1.jpg
│   │   └── team2.jpg
│
│   └── index.jsp
│
├── WEB-INF/
│   ├── web.xml
│   └── lib/
│
└── database/
    └── schema.sql