<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tm" %>
<%@ taglib prefix = "ex" uri = "/WEB-INF/custom.tld"%>
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
        <div class="col-9">

            <div class="row">

                <div class="col-3 ">
                    <form action="/reservation" method="post">
                        <div class="mb-3">
                            <label for="start" class="form-label"><span><fmt:message key="CheckInDate"/></span></label>
                            <input type="date" class="form-control" id="start" name="startRent"
                                   value=<tm:CurrentDay/>
                                   min=<tm:CurrentDay/>
                                   max=<tm:EndYear/> >
                        </div>
                        <div class="mb-3">
                            <label for="end" class="form-label"><span> <fmt:message key="CheckOut"/></span></label>
                            <input type="date" class="form-control" id="end" name="endRent"
                                   value=<tm:NextDay/>
                                   min=<tm:NextDay/>
                                   max=<ex:MaxReservation/>>
                        </div>
                        <select class="form-select" aria-label="Default select example" name="capacity" id="capacity">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                        </select>
                        <div class="d-grid gap-2" style="margin-top: 20px">
                            <c:if test="${ user!=null&&role.equals(\"USER\")}">
                                <button class="btn btn-dark" type="submit"><span><fmt:message key="Order"/></span>
                                </button>
                            </c:if>
                            <button class="btn btn-dark" type="button" onclick="sendFilter()"><span><fmt:message
                                    key="Search"/></span></button>
                        </div>
                    </form>
                </div>

                <div class="col-9">
                    <div class="btn-group d-flex data-sticky-header" role="group"
                         aria-label="Basic radio toggle button group">
                        <button value="price"
                                <c:choose>
                                    <c:when test="${sortType.equals('price')}">
                                        class="btn btn-outline-dark active"
                                    </c:when>
                                    <c:otherwise>
                                        class="btn btn-outline-dark"
                                    </c:otherwise>
                                </c:choose> onclick="sendSortMethod('price')">
                            <span><fmt:message key="price"/> </span></button>
                        <button value="capacity"
                                <c:choose>
                                    <c:when test="${sortType.equals('capacity')}">
                                        class="btn btn-outline-dark active"
                                    </c:when>
                                    <c:otherwise>
                                        class="btn btn-outline-dark"
                                    </c:otherwise>
                                </c:choose>
                                onclick="sendSortMethod('capacity')">
                            <span><fmt:message key="capacity"/></span></button>
                        <button value="roomClass"
                                <c:choose>
                                    <c:when test="${sortType.equals('category')}">
                                        class="btn btn-outline-dark active"
                                    </c:when>
                                    <c:otherwise>
                                        class="btn btn-outline-dark"
                                    </c:otherwise>
                                </c:choose>
                                onclick="sendSortMethod('category')">
                            <span><fmt:message key="room.type"/></span></button>
                        <button value="status"
                                <c:choose>
                                    <c:when test="${sortType.equals('status')}">
                                        class="btn btn-outline-dark active"
                                    </c:when>
                                    <c:otherwise>
                                        class="btn btn-outline-dark"
                                    </c:otherwise>
                                </c:choose>
                                onclick="sendSortMethod('status')">
                            <span><fmt:message key="status"/></span></button>
                    </div>
                    <c:forEach var="room" items="${rooms}">
                        <div class="border border-dark rounded-3" style="margin-top: 15px">
                            <div class="row">
                                <div class="col-3"><img
                                        src="https://cf.bstatic.com/xdata/images/hotel/square600/206746877.webp?k=d74256bd5ef7331794fc0c855aca6735f9fdd1b24e1856f10ce493d2e2bbf510&o="
                                        width="200" height="200" style="border-radius: 3px; margin: 7px"></div>
                                <div class="col-6">
                                    <h5 class="h5 text-center"><span>

                                        <c:if test="${ sessionScope.lang.equals('en')}">
                                            ${room.roomNameEn}
                                        </c:if>
                                        <c:if test="${ sessionScope.lang.equals('ua')}">
                                            ${room.roomNameUa}
                                        </c:if>
                                </span>
                                    </h5>
                                    <p><span><fmt:message key="Room"/></span> : <span ><fmt:message key="${room.category}"/></span></p>
                                    <p><span><fmt:message key="Capacity"/></span> : <span >${room.capacity}</span></p>
                                    <p><span><fmt:message key="Price"/></span> : <span >${room.price}</span></p>


                                </div>
                                <div class="col-3 py-5">
                                    <p><span><fmt:message key="From"/></span> : <span>${startRent}</span></p>
                                    <p><span><fmt:message key="To"/></span>: <span>${endRent}</span></p>
                                    <p><span>
                                        <c:choose>
                                            <c:when test="${room.status ==null}">
                                                <fmt:message key="AVAILABLE"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="${room.status}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </span></p>
                                    <c:if test="${ user!=null&&role.equals(\"USER\")&&room.status ==null}">
                                    <a class="btn btn-dark "
                                       href="/app/reservation?room=${room.roomId}&start=${startRent}&end=${endRent}">
                                        <span><fmt:message key="bookIn"/></span></a>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                                        <nav aria-label="Page navigation example">
                                            <ul class="pagination justify-content-center">
                                                <c:forEach begin="1" end="${pageNumbers}" var="i">
                                                    <li class="page-item">
                                                        <a class="page-link" href="/app/?page=${i}">${i}</a>
                                                    </li>
                                                </c:forEach>
                                            </ul>
                                        </nav>
                </div>
            </div>

        </div>
    </div>

</div>

<!-- Modal HTML registration-->
<div id="myModalRegistry" class="modal fade">
    <div class="modal-dialog modal-login">
        <div class="modal-content">
            <form id="registration" action="/app/registration" method="post">
                <div class="modal-header">
                    <h4 class="modal-title">Registration</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label>Username</label>
                        <input type="text" name="userName" class="form-control" required="required">
                    </div>
                    <div class="form-group">
                        <div class="clearfix">
                            <label>Password</label>
                        </div>

                        <input type="password" name="password" class="form-control" required="required">
                    </div>
                </div>
                <div class="modal-footer justify-content-between">
                    <input type="submit" class="btn btn-primary" value="Registration">
                </div>
                <span ><p>${message}</p></span>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">

    function sendFilter() {
        let url = new URL(window.location.href);
        let params = url.searchParams;
        let startRent = document.getElementById('start').value;
        let endRent = document.getElementById('end').value;
        let capacity = document.getElementById('capacity').value;
        params.set('startRent', startRent);
        params.set('endRent', endRent);
        params.set('capacity', capacity);
        url.search = params.toString();
        window.location.href = url;
    }

    // async function order(val) {
    //     const url = "/reservation";
    //     let formData = new FormData();
    //     formData.append("roomId", val);
    //     const plainFormData = Object.fromEntries(formData.entries());
    //     const formDataJsonString = JSON.stringify(plainFormData);
    //     const fetchOptions = {
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/json",
    //             "Accept": "application/json"
    //         },
    //         body: formDataJsonString
    //     };
    //
    //     const response = await fetch(url, fetchOptions);
    // }

    function sendSortMethod(val) {
        let url = new URL(window.location.href);
        let params = url.searchParams;
        params.set('sort', val);
        url.search = params.toString();
        window.location.href = url;
    }

    // async function postFormDataAsJson({url, formData}) {
    //
    //     const plainFormData = Object.fromEntries(formData.entries());
    //     const formDataJsonString = JSON.stringify(plainFormData);
    //     const fetchOptions = {
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/json",
    //             "Accept": "application/json"
    //         },
    //         body: formDataJsonString,
    //     };
    //
    //     const response = await fetch(url, fetchOptions);
    //
    //     if (response.ok) {
    //         const message = await response.text();
    //         $('#messageOk').text(message);
    //     } else {
    //         const message = await response.text();
    //         $('#messageEr').text(message);
    //     }
    //
    //     return response.json();
    // }
</script>
<script src="https://code.jquery.com/jquery-3.1.1.slim.min.js" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
</body>
</html>