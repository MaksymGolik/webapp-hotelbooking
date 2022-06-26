<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>CABINET</title>
</head>
<style><%@include file="/css/style.css"%></style>
<body>
<%@include file="/jsp-pages/navbar.jspf" %>
<div>
<h1>Hello <c:out value="${sessionScope.currentUser.name}"/>!</h1>
    Your role is <c:out value="${sessionScope.currentUser.role.name}"/>
</div>
</body>
</html>