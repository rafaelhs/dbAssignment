<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 8/29/18
  Time: 12:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Card</title>
    <%@include file="/view/include/head.jsp"%>
</head>
<body>
<%@include file="/view/include/navbar.jsp"%>


<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Insert a card:</h3>
                    <form id="insertCard"
                          action="${pageContext.servletContext.contextPath}/card/create"
                          method="POST">

                        <div class="form-group">
                            <input type="text" name="name" class="form-control" placeholder="Name" required autofocus/>
                        </div>
                        <div class="form-group">
                            <input type="text" name="cost" class="form-control" placeholder="Cost"/>
                        </div>
                        <div class="form-group">
                            <input type="text" name="description" class="form-control" placeholder="Description"/>
                            </div>
                        <div class="form-group">
                            <input type="text" name="type" class="form-control" placeholder="Type"/>
                        </div>
                        <div class="form-group">
                            <select name="rarity" form="insertCard" class="custom-select">
                                <option value='c'>Common</option>
                                <option value='u'>Uncommon</option>
                                <option value='r'>Rare</option>
                                <option value='m'>Mythic Rare</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <input class="btn btn-primary"  type = "Submit" value = "Submit" />
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Insert price from .json</h3>
                    <form action = "price/json" method = "POST" enctype="multipart/form-data">
                        <div class="form-group">
                            <div class="custom-file">
                                <input type="file" class="custom-file-input" id="customFile" name = "json" accept=".json" />
                                <label class="custom-file-label" for="customFile">Choose file</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <input class="btn btn-primary"  type = "Submit" value = "Submit" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="card" id="forms2">
                <div class="card-body">
                    <h3>Insert frequency from .json</h3>
                    <form action = "frequency/json" method = "POST" enctype="multipart/form-data">
                        <div class="form-group">
                            <div class="custom-file">
                                <input type="file" class="custom-file-input" id="customFile2" name = "json" accept=".json" />
                                <label class="custom-file-label" for="customFile2">Choose file</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <input class="btn btn-primary"  type = "Submit" value = "Submit" />
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
            <h3>Cards</h3>
            <div class="card bg-light mb-3">
                <div class="card-body">
                    <table class="table table-striped table-dark" id="cardTable">
                        <thread>
                            <tr><th scope="col">Name</th><th scope="col"></th></tr>
                        </thread>
                        <c:forEach var="card" items="${requestScope.cardList}">
                            <tr>
                                <td>
                                    <a href="${pageContext.servletContext.contextPath}/price?idCard=${card.id}"><c:out value="${card.name}"/></a>
                                </td>

                                <td>
                                    <a href="${pageContext.servletContext.contextPath}/card/delete?idCard=${card.id}">
                                        Delete
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>








</body>
</html>
