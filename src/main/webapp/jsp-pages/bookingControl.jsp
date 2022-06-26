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
                        <span>ДЕТАЛІ</span>
                        <span>БРОНЮВАННЯ №${requestScope.booking.id}</span>
                    </div>
                </div>
                <div class="screen-body-item">
                    <p>Номер аккаунта користувача, що зробив бронювання: ${requestScope.user.id}</p>
                    <p>Пошта користувача, що зробив бронювання: ${requestScope.user.email}</p>
                    <p>Прізвище гостя: ${requestScope.booking.guestSurname}</p>
                    <p>Ім'я гостя: ${requestScope.booking.guestName}</p>
                    <p>Телефон гостя: ${requestScope.booking.guestPhoneNumber}</p>
                    <p>Загальна вартість: ${requestScope.booking.price}</p>
                    <p>Вміст бронювання:</p>
                    <c:forEach var="item" items="${requestScope.bookingItems}">
                        <ul>
                            <li class="first"><p>${item.room.info.type.description} на ${item.room.info.capacity} особи</p></li>
                            <li><p>Поселення: ${item.checkInDate}</p></li>
                            <li><p>Виселення: ${item.checkOutDate}</p></li>
                            <li><p>Вартість на заброньовані дати: ${item.price}</p></li>
                        </ul>
                    </c:forEach>
                    <div class="app-form">
                        <div class="app-form-group buttons">
                            <c:if test="${requestScope.booking.status.name=='CREATED'}">
                                <form method="POST" action="${pageContext.request.contextPath}/changeBookingStatus">
                                    <input type="number" hidden name="bookingId" value="${requestScope.booking.id}">
                                    <input type="text" hidden name="statusName" value="CONFIRMED">
                                    <button class="app-form-button" type="submit">Схвалити</button>
                                </form>
                                <form method="POST" action="${pageContext.request.contextPath}/changeBookingStatus">
                                    <input type="number" hidden name="bookingId" value="${requestScope.booking.id}">
                                    <input type="text" hidden name="statusName" value="DENIED">
                                    <button class="app-form-button" type="submit">Відхилити</button>
                                </form>
                            </c:if>
                            <c:if test="${requestScope.booking.status.name=='PAID'}">
                                <form method="POST" action="${pageContext.request.contextPath}/changeBookingStatus">
                                    <input type="number" hidden name="bookingId" value="${requestScope.booking.id}">
                                    <input type="text" hidden name="statusName" value="COMPLETED">
                                    <button class="app-form-button" type="submit">Завершити</button>
                                </form>
                                <form method="POST" action="${pageContext.request.contextPath}/changeBookingStatus">
                                    <input type="number" hidden name="bookingId" value="${requestScope.booking.id}">
                                    <input type="text" hidden name="statusName" value="DENIED">
                                    <button class="app-form-button" type="submit">Відхилити (НП)</button>
                                </form>
                            </c:if>
                            <c:if test="${requestScope.booking.status.name=='CANCELLATION_REQUESTED'}">
                                <form method="POST" action="${pageContext.request.contextPath}/changeBookingStatus">
                                    <input type="number" hidden name="bookingId" value="${requestScope.booking.id}">
                                    <input type="text" hidden name="statusName" value="CANCELED">
                                    <button class="app-form-button" type="submit">Погодити скасування</button>
                                </form>
                            </c:if>
                            <a href="cabinet" class="app-form-button">До кабінету</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>