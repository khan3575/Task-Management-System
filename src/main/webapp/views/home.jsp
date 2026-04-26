<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Task Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>


    <div class="page-wrapper">

        <main class="main-content">

            <div class="page-header">
                <h1>Welcome, <%= session.getAttribute("username") %></h1>
                <p class="page-subtitle">Task Management System — Team Overview</p>
            </div>

            <%-- Flash messages from redirects (e.g. after delete) --%>
            <%
                String successMsg = (String) session.getAttribute("successMessage");
                String errorMsg   = (String) session.getAttribute("errorMessage");
                if (successMsg != null) { session.removeAttribute("successMessage"); }
                if (errorMsg   != null) { session.removeAttribute("errorMessage"); }
            %>
            <% if (successMsg != null) { %>
                <div class="alert alert-success"><%= successMsg %></div>
            <% } %>
            <% if (errorMsg != null) { %>
                <div class="alert alert-error"><%= errorMsg %></div>
            <% } %>

            <%-- Team Members Section --%>
            <section class="team-section">
                <h2 class="section-title">Our Team</h2>
                <div class="team-grid">

                    <div class="team-card">
                        <div class="team-avatar">
                            <img src="<%= request.getContextPath() %>/images/team1.jpg"
                                 alt="Team Member"
                                 onerror="this.src='<%= request.getContextPath() %>/images/default-avatar.png'">
                        </div>
                        <div class="team-info">
                            <h3>Mehedi</h3>
                            <p class="team-role">Auth, Models, Delete</p>
                            <p class="team-files">LoginServlet · LogoutServlet · User.java · Task.java · DeleteTaskServlet</p>
                        </div>
                    </div>

                    <div class="team-card">
                        <div class="team-avatar">
                            <img src="<%= request.getContextPath() %>/images/team2.jpg"
                                 alt="Team Member"
                                 onerror="this.src='<%= request.getContextPath() %>/images/default-avatar.png'">
                        </div>
                        <div class="team-info">
                            <h3>Mahmud</h3>
                            <p class="team-role">Dashboard</p>
                            <p class="team-files">DashboardServlet · dashboard.jsp · addTask.jsp</p>
                        </div>
                    </div>

                    <div class="team-card">
                        <div class="team-avatar">
                            <img src="<%= request.getContextPath() %>/images/team1.jpg"
                                 alt="Team Member"
                                 onerror="this.src='<%= request.getContextPath() %>/images/default-avatar.png'">
                        </div>
                        <div class="team-info">
                            <h3>Fahim</h3>
                            <p class="team-role">Add &amp; Update Tasks</p>
                            <p class="team-files">AddTaskServlet · UpdateTaskServlet · editTask.jsp</p>
                        </div>
                    </div>

                    <div class="team-card">
                        <div class="team-avatar">
                            <img src="<%= request.getContextPath() %>/images/team2.jpg"
                                 alt="Team Member"
                                 onerror="this.src='<%= request.getContextPath() %>/images/default-avatar.png'">
                        </div>
                        <div class="team-info">
                            <h3>Neky</h3>
                            <p class="team-role">Search &amp; Filter</p>
                            <p class="team-files">SearchTaskServlet · AuthFilter · search.jsp</p>
                        </div>
                    </div>

                    <div class="team-card">
                        <div class="team-avatar">
                            <img src="<%= request.getContextPath() %>/images/team1.jpg"
                                 alt="Team Member"
                                 onerror="this.src='<%= request.getContextPath() %>/images/default-avatar.png'">
                        </div>
                        <div class="team-info">
                            <h3>Sakib</h3>
                            <p class="team-role">UI Components &amp; Validation</p>
                            <p class="team-files">UserValidator · navbar.jsp · sidebar.jsp</p>
                        </div>
                    </div>
                    
                    

                </div>
            </section>

        </main>
    </div>

</body>
</html>
