<%@ page import="model.*" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 9/3/18
  Time: 8:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Frequency</title>
    <%@include file="/view/include/head.jsp"%>
</head>
<body>
<%@include file="/view/include/navbar.jsp"%>


<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Insert a card</h3>
                    <form id="insertCard"
                          action="${pageContext.servletContext.contextPath}/frequency/create"
                          method="POST">

                        <div class="form-group">
                            <input type="text" name="cardName" class="form-control" placeholder="Card name" required autofocus/>
                        </div>
                        <input type="hidden" id="championshipId" name="championshipId" value="${requestScope.championshipId}">
                        <div class="form-group">
                            <input type="text" name="quantity" class="form-control" placeholder="Quantity"/>
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
        <div style="width: 800px; height: auto">
            <canvas id="freq-chart" width="600" height="400"></canvas>
        </div>
    </div>
</div>


<% //buscando os dados que serão utilizados no grafico
    List<Frequency> frequencyList = (List<Frequency>) request.getAttribute("frequencyList");

%>

<script>

    var labels = [
        <%
                for(int i = 0; i < frequencyList.size(); i++){
                    out.print("\""+frequencyList.get(i).getName() + "\", ");
                }
            %>
    ];

    var dataset = [{
        data: [
            <%
                for(int i = 0; i < frequencyList.size(); i++){
                    out.print(frequencyList.get(i).getQuantity() + ", ");
                }
            %>
        ],
        backgroundColor: [
            <%
                for(int i = 0; i < frequencyList.size(); i++){
                    out.print("getRandomColor()" + ", ");
                }
            %>
        ]
    }];



    var birdsCanvas = document.getElementById("freq-chart");

    var birdsData = {
        labels: labels,
        datasets: dataset
    };

    var polarAreaChart = new Chart(birdsCanvas, {
        type: 'polarArea',
        data: birdsData
    });


    function getRandomColor() {
        var letters = '0123456789ABCDEF'.split('');
        var color = '#';
        for (var i = 0; i < 6; i++ ) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }

</script>




<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-6">
            <div class="card bg-light mb-3">
                <div class="card-body">
                    <table class="table table-striped table-dark" id="cardTable">
                        <thread>
                            <tr><th scope="col">Card id</th><th scope="col">Quantity</th><th scope="col"></th></tr>
                        </thread>
                        <c:forEach var="frequency" items="${requestScope.frequencyList}">
                            <tr>
                                <td>
                                    <a href="${pageContext.servletContext.contextPath}/price?idCard=${frequency.idCard}"><c:out value="${frequency.name}"/></a>
                                </td>
                                <td>
                                        ${frequency.quantity}
                                </td>
                                <td>
                                    <a href="${pageContext.servletContext.contextPath}/frequency/delete?idCard=${frequency.idCard}&championshipId=${requestScope.championshipId}">
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
