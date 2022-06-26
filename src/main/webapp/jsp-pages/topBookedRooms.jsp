<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>TOP</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <link href="bootstrap-5.0.2-dist/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="css/my.css" rel="stylesheet" type="text/css">
    <link href="css/table.css" rel="stylesheet"  type="text/css">
    <link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css' rel='stylesheet'  type="text/css">
    <link href="/css/navbar.css" rel="stylesheet" type="text/css">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="/lightbox/css/lightbox.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="/jsp-pages/navbar.jspf" %>
<script type="text/javascript" src="js/table.js"></script>

<section>
    <div class="tbl-header">
        <table>
            <thead>
            <tr>
                <th>Кількість бронювань</th>
                <th class="img-th"></th>
                <th>Тип номеру</th>
                <th>Місткість</th>
                <th>Ціна за добу</th>
                <th>Зручності</th>
            </tr>
            </thead>
        </table>
    </div>
    <div class="tbl-content">
        <table>
            <tbody>
            <c:forEach var="top" items="${requestScope.top}">
                <tr>
                    <td>${top.value}</td>
                    <td class="img-td"><a href="${pageContext.request.contextPath}${top.key.imageUrl}" data-lightbox="test"><img src="${pageContext.request.contextPath}${top.key.imageUrl}" class="room-img" alt=""></a></td>
                    <td>${top.key.type.description}</td>
                    <td>${top.key.capacity}</td>
                    <td>${top.key.price}</td>
                    <td>
                    <c:forEach var="descStr" items="${top.key.description.split('%')}">
                    <p><i class="fa fa-check" aria-hidden="true"></i>${descStr}<p>
                    </c:forEach>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <script type="text/javascript" src="lightbox/js/lightbox-plus-jquery.js"></script>
</section>

</body>
</html>
