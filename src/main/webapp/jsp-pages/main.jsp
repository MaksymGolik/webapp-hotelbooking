<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <link href="bootstrap-5.0.2-dist/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="css/air-datepicker.css" rel="stylesheet" type="text/css">
    <link href="css/my.css" rel="stylesheet" type="text/css">
    <link href="css/table.css" rel="stylesheet"  type="text/css">
    <link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css' rel='stylesheet'  type="text/css">
    <link href="/css/navbar.css" rel="stylesheet" type="text/css">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="/lightbox/css/lightbox.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="/jsp-pages/navbar.jspf" %>
<script type="text/javascript" src="js/air-datepicker.js"></script>
<script type="text/javascript" src="js/table.js"></script>

<div class="container">
    <div class="row">
        <div class="col">
            <form method="get" id="form" action="${pageContext.request.contextPath}/searchRooms">
                <div class="row">
                <div class="input-group mb-3">
                    <div class="col-xs-3">
                        <input type="text" required pattern="^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01]):\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$" id="airdatepicker" name="period" class="form-control" placeholder="Период посещения" >
                    </div>
                    <div class="col-xs-4">
                        <input type="number" name="numberOfPeople" class="form-control" placeholder="Человек" min="2" max="10">
                    </div>
                    <button type="submit" class="btn btn-secondary"><i class='bi bi-search'></i></button>
                </div>
                    <div class="row">
                        <div class="col-xs-4">
                        <select class="form-select" id="inputGroupSelect04" name="roomType">
                            <option selected value="notSelected">Тип кімнати</option>
                            <option value="APARTMENT">Апартаменти</option>
                            <option value="STD">Стандартний однокімнатний номер</option>
                            <option value="SUITE">Номер с вітальнею та спальнею</option>
                            <option value="STUDIO">Однокімнатний номер з кухнею</option>
                        </select>
                        </div>
                    </div>
                </div>
            </form>
            <div id="error"></div>
        </div>

        <c:if test="${requestScope.type=='list'}">
            <c:set var="roomList" value="${requestScope.roomList}" scope="session"/>
        </c:if>
        <c:if test="${requestScope.type=='listMap'}">
            <c:set var="listRoomMap" value="${requestScope.listRoomMap}" scope="session"/>
        </c:if>

        <div class="col">
            <form method="get" action="${pageContext.request.contextPath}/sortRooms">
                <c:if test="${requestScope.availableForBooking!=null}">
                    <input hidden type="text" name="availableForBooking" value="+">
                </c:if>
                <input type="text" hidden name="type" value="${requestScope.type}">
                <input type="text" hidden name="typeSort" value="capacitySort">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" name="capacitySort" id="flexSwitchCheckDefault">
                    <label class="form-check-label" for="flexSwitchCheckDefault">За спаданням/зростанням місткості</label>
                </div>
                <button type="submit" class="btn btn-secondary"><i class='bi bi-search'></i></button>
            </form>
        </div>

        <div class="col">
            <form method="get" action="${pageContext.request.contextPath}/sortRooms">
                <c:if test="${requestScope.availableForBooking!=null}">
                    <input hidden type="text" name="availableForBooking" value="+">
                </c:if>
                <input type="text" hidden name="type" value="${requestScope.type}">
                <input type="text" hidden name="typeSort" value="priceSort">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" name="priceSort" id="flexSwitchCheckDefault1">
                    <label class="form-check-label" for="flexSwitchCheckDefault1">За спаданням/зростанням ціни</label>
                </div>
                    <button type="submit" class="btn btn-secondary"><i class='bi bi-search'></i></button>
            </form>
        </div>
    </div>
</div>
    <script type="text/javascript" src="js/validate.js"></script>
   <script type="text/javascript" charset="UTF-8">
       new AirDatepicker('#airdatepicker',{
           range: true,
           multipleDatesSeparator: ':',
           buttons: ['today', 'clear'],
           minDate: new Date(),
           locale: {
               days: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'],
               daysShort: ['Вос','Пон','Вто','Сре','Чет','Пят','Суб'],
               daysMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
               months: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
               monthsShort: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
               today: 'Сегодня',
               clear: 'Очистить',
               dateFormat: 'yyyy-MM-dd',
               timeFormat: 'hh:mm aa',
               firstDay: 1}
       });
   </script>


<section>
    <div class="tbl-header">
        <table>
            <thead>
            <tr>
                <th class="img-th"></th>
                <th>Тип номеру</th>
                <th>Місткість</th>
                <th>Ціна за добу</th>
                <th>Зручності</th>
                <c:if test="${requestScope.listRoomMap!=null}">
                    <th>Кількість номерів</th>
                </c:if>
                <c:if test="${requestScope.availableForBooking!=null}">
                    <th>Забронювати</th>
                </c:if>
            </tr>
            </thead>
        </table>
    </div>
    <div class="tbl-content">
        <table>
            <tbody>
<c:choose>
    <c:when test="${requestScope.listRoomMap == null}">
            <c:forEach var="room" items="${requestScope.roomList}">
                <tr class="first-row">
                    <td class="img-td"><a href="${pageContext.request.contextPath}${room.info.imageUrl}" data-lightbox="test"><img src="${pageContext.request.contextPath}${room.info.imageUrl}" class="room-img" alt=""></a></td>
                    <td>${room.info.type.description}</td>
                    <td>${room.info.capacity}</td>
                    <td>${room.info.price}</td>
                    <td>
                    <c:forEach var="descStr" items="${room.info.description.split('%')}">
                        <p><i class="fa fa-check" aria-hidden="true"></i>${descStr}<p>
                    </c:forEach>
                    </td>
                    <c:if test="${requestScope.availableForBooking!=null}">
                        <td>
                        <form method="POST" action="${pageContext.request.contextPath}/bookingInfoBridge">
                            <input type="text" hidden name="period" value="${requestScope.period}">
                            <input type="number" hidden name="rooms" value="${room.id}">
                            <button type="submit" class="btn btn-primary">Забронювати</button>
                        </form>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
    </c:when>
    <c:otherwise>
        <c:forEach var="map" items="${requestScope.listRoomMap}" >
            <c:forEach var="entry" items="${map}">
                <c:choose>
                    <c:when test="${map.entrySet().stream().findFirst().get().getKey().equals(entry.key)}">
                        <tr class="first-row">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>

                <td class="img-td"><a href="${pageContext.request.contextPath}${entry.key.imageUrl}" data-lightbox="test"><img src="${pageContext.request.contextPath}${entry.key.imageUrl}" class="room-img" alt=""></a></td>
                <td>${entry.key.type.description}</td>
                <td>${entry.key.capacity}</td>
                <td>${entry.key.price}</td>
                <td>
                    <c:forEach var="descStr" items="${entry.key.description.split('%')}">
                    <p><i class="fa fa-check" aria-hidden="true"></i>${descStr}<p>
                    </c:forEach>
                </td>
                <td>${entry.value}</td>
                <c:if test="${map.entrySet().stream().findFirst().get().getKey().equals(entry.key)}">
                    <td>
                        <form method="POST" action="${pageContext.request.contextPath}/bookingInfoBridge">
                            <input type="text" hidden name="period" value="${requestScope.period}">
                            <input type="text" hidden name="rooms" value="${requestScope.listId.get(listRoomMap.indexOf(map))}">
                            <button type="submit" class="btn btn-primary">Забронювати</button>
                        </form>
                    </td>
                </c:if>
            </tr>
            </c:forEach>
        </c:forEach>
    </c:otherwise>
</c:choose>
            </tbody>
        </table>
    </div>
    <script type="text/javascript" src="lightbox/js/lightbox-plus-jquery.js"></script>
</section>
</body>
</html>
