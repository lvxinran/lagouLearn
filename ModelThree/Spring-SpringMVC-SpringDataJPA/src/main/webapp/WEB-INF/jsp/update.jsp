<%@ page import="com.lxr.pojo.Resume" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/17
  Time: 14:59
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
<%
    Resume currResume = (Resume) request.getAttribute("currResume");
    if(currResume==null){
        currResume = new Resume();
        currResume.setId(0L);
    }
%>
<form action="/resume/updateOne" method="post">
        <input type="hidden" value="<%=currResume.getId()%>" name="id" ><br>
    姓 名:<input type="text" value="<%=currResume.getName() %>" name="name"><br>
    联 系:<input type="text" value="<%=currResume.getPhone() %>" name="phone"><br>
    地 址:<input type="text" value="<%=currResume.getAddress() %>" name="address"><br>
    <input type="submit" value="提交">
    <input type="reset" value="取消">
</form>
</body>
</html>
