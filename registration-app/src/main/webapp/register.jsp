<%@ page import="app.captcha.Captcha" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Register</title>
</head>
<body>
<h1>Register</h1>

<form action="register" method="post">
    <label>Email: <input type="email" name="email" required></label><br><br>
    <label>Full Name: <input type="text" name="name" required></label><br><br>
    <label>Password: <input type="password" name="password" required></label><br><br>

    <%
    Captcha captcha = (Captcha) session.getAttribute("captcha");
    if (captcha != null) {
    %>
    <label>
        <%= captcha.getQuestion() %>
        <input type="text" name="captchaAnswer" required>
    </label><br><br>
    <%
    } else {
    %>
    <p style="color:red;">Captcha could not be generated.</p>
    <%
    }
    %>

    <button type="submit">Register</button>
</form>

<br>
<a href="index.html">Back to Home</a>
</body>
</html>
