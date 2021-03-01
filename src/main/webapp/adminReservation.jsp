<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>

<html>

<head>
    <meta charset="UTF-8">
    <title>Hotel rooms</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link rel="stylesheet" href="main.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
</head>

<body style="margin-bottom: 60px;">
<div class="container-fluid ">
    <jsp:include page="header.jsp"/>

    <div class="row justify-content-md-center">
        <h4 class="h4 text-center">Orders </h4>
        <div class="col-9">
            <c:forEach var="reservation" items="${reservations}">
                <c:choose>
                    <c:when test="${reservation.roomId ==0}">

                        <div class="border border-dark rounded-3" style="margin-top: 15px">
                            <div class="row">
                                    <div class="col-6">
                                        <p>Order: <span>${reservation.reservationId}</span></p>
                                        <p>Rent period:<span>${reservation.startRent} </span> -
                                            <span>${reservation.endRent}</span></p>
                                        <p>Capacity: <span>${reservation.capacity}</span></p>
                                        <p>Room ID: <span>${reservation.roomId}</span></p>

                                    </div>
                                    <div class="col-3 py-5">
                                        <a class="btn btn-dark"
                                           href="/app/admin/remove?id=${reservation.reservationId}">Cancel
                                        </a>
                                        <form action="setRoom" method="post">
                                            <input type="hidden" name="reservation"
                                                   value="${reservation.reservationId}">
                                            <input type="hidden" name="start"
                                                   value="${reservation.startRent}">
                                            <input type="hidden" name="end"
                                                   value="${reservation.endRent}">
                                            <select class="form-select" aria-label="Default select example"
                                                    name="roomId">
                                                <c:forEach var="room" items="${rooms}">
                                                    <c:choose>
                                                    <c:when test="${reservation.capacity <=room.capacity}">
                                                    <option value="${room.roomId}">${room.roomNameEn}</option>
                                                    </c:when>
                                                    </c:choose>
                                                </c:forEach>
                                            </select>
                                                <input class="btn btn-dark" type="submit" value="Add Room"/>

                                        </form>
                                    </div>
                                    </div>
                            </div>
                    </c:when>
                </c:choose>
            </c:forEach>

                <h4 class="h4 text-center">Reservation </h4>
                <c:forEach var="reservation" items="${reservations}">
                    <c:choose>
                        <c:when test="${reservation.roomId !=0}">
                            <div class="border border-dark rounded-3" style="margin-top: 15px">
                                    <div class="col-6">
                                        <p>Order: <span>${reservation.reservationId}</span></p>
                                        <p>Rent period:<span>${reservation.startRent} </span> -
                                            <span>${reservation.endRent}</span></p>
                                        <p>Capacity: <span>${reservation.capacity}</span></p>
                                        <p>Room ID: <span>${reservation.roomId}</span></p>
                                    </div>
                                    <div class="col-2 py-5">
                                        <a class="btn btn-dark"
                                           href="/app/admin/remove?id=${reservation.reservationId}">Cancel
                                        </a>
                                    </div>
                                </div>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </div>
        </div>


</div>
<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>