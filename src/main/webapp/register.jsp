<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Registration</title>
</head>
<body>
<h2>Student Registration</h2>
<form action="student" method="post">
    <input type="hidden" name="action" value="register">
    Name: <input type="text" name="name" required><br>
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <button type="submit">Register</button>
</form>
<a href="login.jsp">Already have an account? Login</a>
</body>
</html>

<!-- login.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Student Login</title>
</head>
<body>
<h2>Student Login</h2>
<form action="student" method="post">
    <input type="hidden" name="action" value="login">
    Email: <input type="email" name="email" required><br>
    Password: <input type="password" name="password" required><br>
    <button type="submit">Login</button>
</form>
<a href="register.jsp">Don't have an account? Register</a>
</body>
</html>
