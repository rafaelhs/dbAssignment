<%--insertCard
  Created by IntelliJ IDEA.
  User: rafael
  Date: 8/29/18
  Time: 7:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Bans</title>

    <%@include file="/view/include/head.jsp"%>
</head>
<body>
<%@include file="/view/include/navbar.jsp"%>

<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Insert ban date</h3>
                    <form id="form-group"
                          action="${pageContext.servletContext.contextPath}/ban/create"
                          method="POST">
                        <div class="form-group">
                            <input id="date-ban" class="form-control datepicker" type="text" name="date"
                                   placeholder="yyyy-mm-dd"
                                   required/>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-6">
            <h3>Bans</h3>
            <table class="table table-striped table-dark">
                <th scope="col">Date</th><th scope="col"></th>
                <c:forEach var="ban" items="${requestScope.banList}">
                    <tr>
                        <td>
                            <c:out value="${ban.date}"/>
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/ban/delete?banId=${ban.id}">
                                Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

</body>
</html>
