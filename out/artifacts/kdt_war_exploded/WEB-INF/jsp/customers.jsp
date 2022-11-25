<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
    <title>Home</title>
</head>
<body class="container-fluid">
<H1>KDT Spring App</H1>
<img src="<c:url value="/resources/img.png" />" class="img-fluid">
<p>The time on the server is ${serverTime}</p>
<h2>Customer Table</h2>
<table class="table table-striped table-hover">
    <thead>
    <tr>
        <th scope="col">ID</th>
        <th scope="col">Name</th>
        <th scope="col">Email</th>
        <th scope="col">CreatedAt</th>
        <th scope="col">LastLoginAt</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="customer" items="${customers}">
        <tr>
            <th scope="row">${customer.customerId}</th>
            <td>${customer.name}</td>
            <td>${customer.email}</td>
            <td>${customer.createdAt}</td>
            <td>${customer.lastLoginAt}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
