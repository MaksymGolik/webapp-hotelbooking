<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>CABINET</title>
</head>
<style><%@include file="/WEB-INF/css/style.css"%></style>
<body>
<%@include file="/WEB-INF/jsp-pages/navbar.jspf" %>
<div>
<h1>Hello <c:out value="${requestScope.userInfo.name}"/>!</h1>
    Your role is <c:out value="${requestScope.userInfo.role.name}"/>
</div>
</body>
</html>