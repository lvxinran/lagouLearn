<%@ page import="java.util.List" %>
<%@ page import="com.lxr.pojo.Resume" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/17
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html>
<head>
    <title>Resume</title>
</head>
<body>
${msg}
<table border=1>
    <tr><td>姓名</td><td>地址</td><td>联系方式</td><td colspan="2" align="center">数据操作</td></tr>
    <% List<Resume> allResume = (List<Resume>)request.getAttribute("allResume");

        for(Resume resume:allResume) {%>
    <tr><td><%=resume.getName() %></td><td><%=resume.getAddress() %></td><td><%=resume.getPhone() %></td><td><a href="/resume/deleteOne?id=<%=resume.getId()%>" >删除操作</a></td><td><a href="toUpdate?id=<%=resume.getId()%>">更新操作</a></td></tr>
    <%} %>
</table>
<form action="/resume/toUpdate" method="get">
    <input type="hidden" name="id" value="-1">
    <input type="submit" value="添加用户">
</form>
</body>
</html>
