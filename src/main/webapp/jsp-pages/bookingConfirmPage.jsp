<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Success</title>

    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700,900" rel="stylesheet">

    <link type="text/css" rel="stylesheet" href="/css/error.css"/>

</head>

<body>
<div id="notfound">
    <div class="notfound">
        <div class="notfound-404">
            <h1>Success!</h1>
        </div>
        <p>Ви успішно створили нове бронювання. Його номер: №${requestScope.bookingId}</p>
        <a href="myBookings">До моїх бронювань</a>
    </div>
</div>
</body>

</html>
