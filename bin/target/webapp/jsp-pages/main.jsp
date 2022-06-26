<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Main Page</title>
    <style><%@include file="/css/style.css"%>
    <%@include file="/css/air-datepicker.css"%>
    </style>
</head>
<body>
<%@include file="/jsp-pages/navbar.jspf" %>
<script type="text/javascript">
    <%@include file="/js/air-datepicker.js"%>
</script>
<div>
<h1>MAIN PAGE</h1>
    <form method="get" action="${pageContext.request.contextPath}/searchForPeriod">
        <input type="text" id="airdatepicker" name="period" class="form-control">
        <input class="button" type="submit" value="Найти">
    </form>
    <script type="text/javascript">
        <%@include file="/js/main.js"%>
    </script>
    <c:forEach var="rooms" items="${requestScope.roomList}">
        ${rooms.type.description}<br>
        ${rooms.capacity}<br>
        ${rooms.price}<br>
        ${rooms.description}<br>
        <img src="${pageContext.request.contextPath}${rooms.image_url}" width="250" alt=""><br>
    </c:forEach>
</div>
</body>
</html>
