<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Task Management System</title>
</head>
<body>

    <div class="login-form">
        <h2>Login</h2>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
            <div style="color: red;">
                <%= error %>
            </div>
        <%
            }
        %>

        <form action="<%= request.getContextPath() %>/login" method="post">
            <div>
                <label for="username">Username:</label>
                <input type="text" id="username" name="username" required>
            </div>

            <div>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div>
                <input type="submit" value="Login">
            </div>
        </form>

    </div>

</body>
</html>