<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>CABINET</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <link href="https://netdna.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="/css/cabinet.css" rel="stylesheet" type="text/css">
    <link href="/css/navbar.css" rel="stylesheet" type="text/css">
    <link href="/css/table.css" rel="stylesheet" type="text/css">
</head>
<body>
<%@include file="/jsp-pages/navbar.jspf" %>
<script type="text/javascript" src="/js/table.js"></script>
<div class="container bootstrap snippets bootdey">
    <div class="row">
        <div class="profile-nav col-md-3">
            <div class="panel">
                <div class="user-heading round">
                    <a href="#">
                        <img src="https://cdn.pixabay.com/photo/2013/07/13/12/07/avatar-159236_960_720.png" alt="">
                    </a>
                    <h1>${sessionScope.currentUser.name}</h1>
                    <p>${sessionScope.currentUser.email}</p>
                </div>
                <%@include file="/jsp-pages/cabinetNav.jspf" %>
            </div>
        </div>

        <div class="profile-info col-md-9">

            <div class="panel">
                <div class="bio-graph-heading">
                    The best place for you Journey
                </div>
                <div class="panel-body bio-graph-info">
                    <h1>Інформація</h1>
                    <c:if test="${requestScope.errorMessage==null}">
                    <div class="tbl-header">
                        <table>
                            <thead>
                            <tr>
                                <th>Номер бронювання</th>
                                <th>Вартість</th>
                                <th>Час створення</th>
                                <th>Остання зміна</th>
                                <th>Стан</th>
                                <th>Деталі</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                    <div class="tbl-content">
                        <table>
                            <tbody>
                            <c:forEach var="booking" items="${requestScope.bookingList}">
                                <tr class="first-row">
                                    <td>${booking.id}</td>
                                    <td>${booking.price}</td>
                                    <td>${booking.createTime}</td>
                                    <td>${booking.lastUpdateTime}</td>
                                    <td>${booking.status.description}</td>
                                    <td>
                                        <form method="get" action="${pageContext.request.contextPath}/bookingDetails">
                                            <input type="number" hidden name="bookingId" value="${booking.id}">
                                            <button type="submit" class="btn btn-primary">Деталі</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    </c:if>
                    <h2>${requestScope.errorMessage}</h2>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>