<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

</head>
<body>

<div class="form">

    <h1>Регистрация в системе</h1><br>
    <form method="post" action="${pageContext.request.contextPath}/register">

        <input type ="text" required placeholder="name" name="name"><br>
        <input type="text" required placeholder="email" name="email"><br>
        <input type="password" required placeholder="password" name="password"><br><br>
        <input class="button" type="submit" value="Зарегистрироваться">

    </form>
</div>
</body>
</html>