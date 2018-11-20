<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 17.11.2018
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add new user</title>
</head>
<body>
<form action="/add-new-user" method="POST">
    <label>Name</label>
    <input type="text" name="name">
    <label>Company</label>
    <input type="text" name="companyId">
    <label>Boss</label>
    <input type="text" name="bossId">
    <input type="submit" value="Add new user">
</form>
</body>
</html>
