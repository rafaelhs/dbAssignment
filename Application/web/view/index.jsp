<%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 8/28/18
  Time: 4:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>Index</title>

    <%@include file="/view/include/head.jsp"%>
</head>
<body>
    <%@include file="/view/include/navbar.jsp"%>



    <div style="width: 800px; height: 800px">
        <canvas id="line-chart" width="800" height="450"></canvas>
    </div>
    <script>
        new Chart(document.getElementById("line-chart"), {
            type: 'line',
            data: {
                labels: [1500,1600,1700,1750,1800,1850,1900,1950,1999,2050],
                datasets: [{
                    data: [86,114,106,106,107,111,133,221,783,2478],
                    label: "Max",
                    borderColor: "#ea464c",
                    fill: false
                }, {
                    data: [282,350,411,502,635,809,947,1402,3700,5267],
                    label: "Med",
                    borderColor: "#eaa985",
                    fill: false
                }, {
                        data: [168,170,178,190,203,276,408,547,675,734],
                    label: "Min",
                    borderColor: "#59be76",
                    fill: false
                }, {
                    data: [40,20,10,16,24,38,74,167,508,784],
                    label: "Freq",
                    borderColor: "#4687ea",
                    fill: false
                }
                ]
            },
            options: {
                title: {
                    display: true,
                    text: 'CARD-NAME'
                }
            }
        });

    </script>




    <div class="content-wrapper">

    </div>
</body>
</html>
