<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Make Booking</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <link href="/css/styleMakeBooking.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="background">
    <div class="container">
        <div class="screen">
            <div class="screen-header">
                <div class="screen-header-left">
                    <div class="screen-header-button close"></div>
                    <div class="screen-header-button maximize"></div>
                    <div class="screen-header-button minimize"></div>
                </div>
                <div class="screen-header-right">
                    <div class="screen-header-ellipsis"></div>
                    <div class="screen-header-ellipsis"></div>
                    <div class="screen-header-ellipsis"></div>
                </div>
            </div>
            <div class="screen-body">
                <div class="screen-body-item left">
                    <div class="app-title">
                        <span>ЗРОБИ</span>
                        <span>БРОНЮВАННЯ</span>
                        <span>ЗАРАЗ</span>
                    </div>
                </div>
                <div class="screen-body-item">
                    <div class="app-form">
                        <form method="POST" action="${pageContext.request.contextPath}/makeBooking">
                        <div class="app-form-group">
                            <input class="app-form-control" minlength="2" maxlength="40" required pattern="^[a-zA-Za-яА-Я]+$" name="surname" placeholder="ПРІЗВИЩЕ" >
                        </div>
                        <div class="app-form-group">
                            <input class="app-form-control" minlength="2" maxlength="40" pattern="^[a-zA-Za-яА-Я]+$" required name="name" placeholder="ІМ'Я'">
                        </div>
                        <div class="app-form-group">
                            <input class="app-form-control" pattern="^\+{1}3?8?(0\d{9})$" required name="phoneNumber" placeholder="ТЕЛЕФОН">
                        </div>
                        <div class="app-form-group buttons">
                            <a class="app-form-button" href="home" role="button">Скасувати</a>
                            <button class="app-form-button">Відправити</button>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>