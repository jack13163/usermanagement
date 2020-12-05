<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<html>
<head>
    <title>激活成功</title>
</head>
<body>
    恭喜，您的用户账号激活成功！
    <a href="<%=basePath%>/login.jsp">登录入口</a>
</body>
</html>
