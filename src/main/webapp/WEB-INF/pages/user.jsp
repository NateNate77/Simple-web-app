<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 16.11.2018
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <title>$Title$</title>
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
      <th>Имя сотрудника</th>
      <th>Организация</th>
      <th>Руководитель</th>
    </tr>

    <c:forEach var="user" items="${userList}">
      <tr>
        <td>${user.id}</td>
        <td>${user.name}</td>
        <td>${user.companyId}</td>
        <td>${user.bossId}</td>
      </tr>
    </c:forEach>
  </table>
  <a href="/add-new-user">Add new user</a>
  <a href="/company">Company</a>
  </body>
</html>
