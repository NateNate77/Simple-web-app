<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 17.11.2018
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Company</title>
    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #dddddd;
        }
    </style>
</head>
<body>

<table>
    <tr>
        <th>ID</th>
        <th>Название организации</th>
        <th>ID головной организации</th>
    </tr>

    <c:forEach var="company" items="${companyList}">
        <tr>
            <td>${company.id}</td>
            <td>${company.name}</td>
            <td>${company.headCompanyId}</td>
        </tr>
    </c:forEach>
</table>
<a href="/add-new-company">Add new company</a>

</body>
</html>
