<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%!
	int age;
%>
<%
String str= request.getParameter("age");
age = Integer.parseInt(str);
%>

미성년자입니다. 주류구매 불가능합니다.
</body>
</html>