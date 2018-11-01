<%@ page import="model.Card" %><%--
  Created by IntelliJ IDEA.
  User: rafael
  Date: 9/2/18
  Time: 9:11 PM
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
    <div class="row justify-content-start">

        <div class="col-sm-4">
            <h3>Update Card</h3>
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


</body>
</html>
