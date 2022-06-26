<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>CABINET</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
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
        <%@include file="/jsp-pages/cabinetAdminSection.jspf" %>
    </div>
</div>
</body>
</html>