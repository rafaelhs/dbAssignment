<%@ page import="java.util.List" %>
<%@ page import="model.*" %><%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 8/29/18
  Time: 11:30 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <title>Price</title>
    <%@include file="/view/include/head.jsp"%>

</head>
<body>
<%@include file="/view/include/navbar.jsp"%>


<% Card card = (Card)request.getAttribute("card");
    String name = "Name";
    String cost = "Cost";
    String description = "Description";
    String type = "Type";

    if(!card.getName().equals("")) name = card.getName();
    if(!card.getCost().equals("")) cost = card.getCost();
    if(!card.getDescription().equals("")) description = card.getDescription();
    if(!card.getType().equals("")) type = card.getType();

%>



<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Card Information</h3>
                    <ul class="list-group">
                        <li class="list-group-item"><% if(!card.getName().equals("")){out.print(card.getName());}else{out.print("-");} %></li>
                        <li class="list-group-item"><% if(!card.getCost().equals("")){out.print(card.getCost());}else{out.print("-");} %></li>
                        <li class="list-group-item"><% if(!card.getDescription().equals("")){out.print(card.getDescription());}else{out.print("-");} %></li>
                        <li class="list-group-item"><% if(!card.getType().equals("")){out.print(card.getType());}else{out.print("-");} %></li>
                        <li class="list-group-item"><% if(card.getRarity()!=Character.MIN_VALUE){out.print(card.getRarity());}else{out.print("-");} %></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Update card</h3>
                    <form id="insertCard"
                          action="${pageContext.servletContext.contextPath}/card/update"
                          method="POST">
                        <input type="hidden" id="custId" name="idCard" value="<%out.print(card.getId());%>">
                        <div class="form-group row">
                            <input type="text" name="name" class="form-control" placeholder="<%out.print(name);%>" required autofocus/>
                        </div>
                        <div class="form-group row">
                            <input type="text" name="cost" class="form-control" placeholder="<%out.print(cost);%>"/>
                        </div>
                            <div class="form-group row">
                            <input type="text" name="description" class="form-control" placeholder="<%out.print(description);%>"/>
                        </div>
                        <div class="form-group row">
                            <input type="text" name="type" class="form-control" placeholder="<%out.print(type);%>"/>
                        </div>
                        <div class="form-group row">
                            <select name="rarity" form="insertCard" class="custom-select">
                                <option value='c'>Common</option>
                                <option value='u'>Uncommon</option>
                                <option value='r'>Rare</option>
                                <option value='m'>Mythic Rare</option>
                            </select>
                        </div>
                        <div class="form-group row">
                            <input class="btn btn-primary"  type = "Submit" value = "Submit" />
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-sm-4">
            <div class="card">
                <div class="card-body">
                    <h3>Insert price</h3>
                    <form id="insertPrice"
                          action="${pageContext.servletContext.contextPath}/price/create"
                          method="POST">
                        <div class="form-group">
                            <input id="date-input" class="form-control datepicker" type="text" name="date"
                                   placeholder="yyyy-mm-dd"
                                   required/>
                        </div>
                        <input type="hidden" id="idCard" name="idCard" value="${requestScope.idCard}">
                        <div class="form-group">
                            <input type="text" name="min" class="form-control" placeholder="Lowest price" required/>
                        </div>
                        <div class="form-group">
                            <input type="text" name="med" class="form-control" placeholder="Medium price" required/>
                        </div>
                        <div class="form-group">
                            <input type="text" name="max" class="form-control" placeholder="Highest price" required/>
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





<% //buscando os dados que serão utilizados no grafico

    List<String> dateList = (List<String>)request.getAttribute("dateList");
    List<Championship> championshipList = (List<Championship>)request.getAttribute("championshipList");
    List<Ban> banList = (List<Ban>)request.getAttribute("banList");
    List<MagicSet> setList = (List<MagicSet>)request.getAttribute("setList");
    List<Frequency> frequencyList = (List<Frequency>)request.getAttribute("frequencyList");
    List<Price> priceList = (List<Price>)request.getAttribute("priceList");

    int size = dateList.size();
    int j = 0;



%>
<div class="container">
    <div class="row justify-content-md-center">
        <div style="width: 800px; height: 800px">
            <canvas id="price-chart" width="800px" height="500px"></canvas>
        </div>
    </div>
</div>

<script>
    var dataset1 = {
        data: [
            <%
                j = 0;
                for(int i = 0; i < size; i++){
                    if(j >= priceList.size()){
                        out.print("null, ");
                    }
                    else if(priceList.get(j).getDate().equals(dateList.get(i))){
                        out.print((double)priceList.get(j).getMax()/100 + ",");
                        j++;
                    }else{
                        out.print("null, ");
                    }
                }
            %>
        ],
        label: "Max",
        borderColor: "#ea464c",
        fill: false
    }

    var dataset2 = {
        data: [
            <%
                j = 0;
                for(int i = 0; i < size; i++){
                    if(j >= priceList.size()){
                        out.print("null, ");
                    }
                    else if(priceList.get(j).getDate().equals(dateList.get(i))){
                        out.print((double)priceList.get(j).getMed()/100 + ",");
                        j++;
                    }else{
                        out.print("null, ");
                    }
                }
            %>
        ],
        label: "Med",
        borderColor: "#eaa985",
        fill: false
    }

    var dataset3 = {
        data: [
            <%
                j = 0;
                for(int i = 0; i < size; i++){
                    if(j >= priceList.size()){
                        out.print("null, ");
                    }
                    else if(priceList.get(j).getDate().equals(dateList.get(i))){
                        out.print((double)priceList.get(j).getMin()/100 + ",");
                        j++;
                    }else{
                        out.print("null, ");
                    }
                }
            %>
        ],
        label: "Min",
        borderColor: "#59be76",
        fill: false
    }

    var dataset4 = {
        data: [<%
                        for(int i = 0; i < dateList.size(); i++){
                            out.print( "null,");
                        }
                    %>],
        label: "Freq",
        borderColor: "#4687ea",
        fill: false
    }

    var annotations1 = [
        <%
            for(int i = 0; i < championshipList.size(); i++){
        %>
                {
                        drawTime: "afterDatasetsDraw",
                        type: "line",
                        borderDash: [2, 2],
                        mode: "vertical",
                        scaleID: "x-axis-0",
                        <%
                            out.print("value: moment(\"" + championshipList.get(i).getDate() + "\").format('DD-MM-YYYY'),");
                        %>
                        borderWidth: 1,
                        borderColor: "blue"
                    },
        <%
            }
        %>
    ]

    var annotations2 = [
        <%
            for(int i = 0; i < banList.size(); i++){
        %>
        {
            drawTime: "afterDatasetsDraw",
            type: "line",
            mode: "vertical",
            scaleID: "x-axis-0",
            <%
                out.print("value: moment(\"" + banList.get(i).getDate() + "\").format('DD-MM-YYYY'),");
            %>
            borderWidth: 1,
            borderColor: "red"
        },
        <%
            }
        %>
    ]

    var annotations3 = [
        <%
            for(int i = 0; i < setList.size(); i++){
        %>
        {
            drawTime: "afterDatasetsDraw",
            type: "line",
            borderDash: [2, 2],
            mode: "vertical",
            scaleID: "x-axis-0",
            <%
                out.print("value: moment(\"" + setList.get(i).getDate() + "\").format('DD-MM-YYYY'),");
            %>
            borderWidth: 1,
            borderColor: "green"
        },
        <%
            }
        %>
    ]


    var annotationsN = annotations1.concat(annotations2).concat(annotations3)


    var data = {
        labels: [
            <%
                j = 0;
                for(int i = 0; i < size; i++){
                    out.print("moment(\""+dateList.get(i) + "\").format(\'DD-MM-YYYY\'),");
                }
            %>
        ],

        datasets: [dataset1, dataset2, dataset3, dataset4]
    }

    var chart = new Chart(document.getElementById("price-chart"), {

        type: 'line',
        data: data,
        options: {
            spanGaps: true,
            annotation: {annotations: annotationsN},
            title: {
                display: true,
                text: <% out.print("\"" + card.getName() + "\"");%>
            },
            scales: {
                yAxes: [{
                    ticks: {
                        callback: function(value, index, values) {
                            return value.toLocaleString("pt-BR",{style:"currency", currency:"BRL"});
                        }
                    }
                }],
                xAxes: [{
                    position: "left",
                    valueFormatString: "YYYY-MM-DD",
                    ticks: {
                        callback: function(value, index, values) {
                            return value;
                        }
                    }
                }]
            }
        }
    });
</script>



<div class="container">
    <div class="row justify-content-md-center">
        <div class="col-sm-6">
            <h3>Prices</h3>
            <div class="card bg-light mb-3">
                <div class="card-body">
                    <table class="table table-sm table-dark">
                        <th scope="col">Date</th><th scope="col">Min.</th><th scope="col">Med.</th><th scope="col">Max</th><th scope="col"></th><th scope="col">

                    </th>
                        <% priceList = (List<Price>)request.getAttribute("priceList");
                            for(int i = 0; i < priceList.size(); i++){
                        %>
                        <tr>
                            <td><% out.print(priceList.get(i).getDate()); %></td>
                            <td>R$<% out.print(" "+((double)priceList.get(i).getMin()/100)); %></td>
                            <td>R$<% out.print(" "+((double)priceList.get(i).getMed()/100)); %></td>
                            <td>R$<% out.print(" "+((double)priceList.get(i).getMax()/100)); %></td>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}/price/delete?idCard=<% out.print(priceList.get(i).getIdCard() + "&date=" + priceList.get(i).getDate()); %>">
                                    Delete
                                </a>
                            </td>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}/price/update?idCard=<% out.print(priceList.get(i).getIdCard() + "&date=" + priceList.get(i).getDate()); %>">
                                    Update
                                </a>
                            </td>
                        </tr>
                        <% } %>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-sm-6">
            <h3>Championships</h3>
            <div class="card bg-light mb-3">
                <div class="card-body">
                    <table class="table table-sm table-dark">
                        <tr><th scope="col">Championship</th><th scope="col">Quantity</th></tr>

                        <%frequencyList = (List<Frequency>)request.getAttribute("frequencyList");
                            for(int i = 0; i < frequencyList.size(); i++){
                        %>

                        <tr>
                            <td>
                                <a href="${pageContext.servletContext.contextPath}frequency?championshipId=<% out.print(frequencyList.get(i).getIdChampionship());%>">
                                    <% out.print(frequencyList.get(i).getName()); %>
                                </a>

                            </td>
                            <td><% out.print(frequencyList.get(i).getQuantity()); %></td>
                        </tr>
                        <% } %>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


</body>
</html>
