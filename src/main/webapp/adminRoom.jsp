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
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/3.1.0/css/flag-icon.min.css" rel="stylesheet">
</head>

<body style="margin-bottom: 60px;">
<div class="container-fluid ">
    <jsp:include page="header.jsp"/>

    <div class="row justify-content-md-center">
        <div class="row justify-content-center" style="margin-top: 50px"><h3 class="text-center">Room
            Administration</h3>
        </div>
        <div class="row justify-content-center">
            <div class="col-6 ">
                <form id="post" action="app/admin/rooms" method="post">
                    <div class="form-group">
                        <label for="roomNameEn">English room name</label>
                        <input class="form-control" name="roomNameEn" id="roomNameEn" required="required">
                    </div>
                    <div class="form-group">
                        <label for="roomNameUa">Ukrainian room name</label>
                        <input class="form-control" name="roomNameUa" id="roomNameUa" required="required">
                    </div>
                    <div class="form-group">
                        <label for="price">Price</label>
                        <input class="form-control" type="number" name="price" id="price">
                    </div>
                    <div class="form-group">
                        <label for="capacity">Room capacity</label>
                        <select class="form-control" id="capacity" name="capacity">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="category">Room class</label>
                        <select class="form-control" id="category" name="category">
                            <option>STANDARD</option>
                            <option>DELUXE</option>
                            <option>SUITE</option>
                        </select>
                    </div>
                    <div class="form-group row">
                        <div class="col-sm-10" style="margin-top: 20px">
                            <button type="submit" class="btn btn-dark">Add new room</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <div class="row justify-content-md-center">

            <c:forEach var="room" items="${rooms}">
                <div class="col-9">
                    <div class="border border-dark rounded-3" style="margin-top: 15px">
                        <div class="row">
                            <div class="col-4"><img
                                    src="https://cf.bstatic.com/xdata/images/hotel/square600/206746877.webp?k=d74256bd5ef7331794fc0c855aca6735f9fdd1b24e1856f10ce493d2e2bbf510&o="
                                    width="200" height="200" style="border-radius: 3px; margin: 7px"></div>
                            <div class="col-6">
                                <p>Name: <span>${room.roomNameEn}</span></p>
                                <p>Room: <span>${room.category}</span></p>
                                <p>Capacity:<span>${room.capacity}</span></p>
                                <p>Cost: <span>${room.price}</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

</div>
<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>