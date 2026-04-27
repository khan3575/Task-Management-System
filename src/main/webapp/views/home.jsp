<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home - Task Management System</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>


	<jsp:include page="components/navbar.jsp" />
	<jsp:include page="components/sidebar.jsp" />


    <div class="main-content">

        <h2>Welcome, <%= session.getAttribute("username") %></h2>
        
        <p class="home-subtitle">Task Management System</p>

        <div class="team-section">
            <h3>Our Team</h3>
                    
                <div class="team-grid">     

                <div class="team-card">
                    <img src="<%= request.getContextPath() %>/images/member-fahim.jpeg" alt="Fahim">
                    <p class="member-name">Fahim</p>
                    <p class="member-role">Intern</p>
                </div>

                <div class="team-card">
                    <img src="<%= request.getContextPath() %>/images/member2.jpg" alt="Mahmud">
                    <p class="member-name">Mahmud</p>
                    <p class="member-role">Product Engineer</p>
                </div>

                <div class="team-card">
                    <img src="<%= request.getContextPath() %>/images/member-mehedi.jpg" alt="Mehedi">
                    <p class="member-name">Mehedi</p>
                    <p class="member-role">Intern</p>
                </div>

                <div class="team-card">
                    <img src="<%= request.getContextPath() %>/images/member-neky.jpg" alt="Neky">
                    <p class="member-name">Neky</p>
                    <p class="member-role">Intern</p>
                </div>

                <div class="team-card">
                    <img src="<%= request.getContextPath() %>/images/member-sakib.jpg" alt="Sakib">
                    <p class="member-name">Sakib</p>
                    <p class="member-role">Intern</p>
                </div>

            </div>
        </div>

    </div>

</body>
</html>
