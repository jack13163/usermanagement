<%@page import="jack.smartbi.entity.User" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
%>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <title>用户管理系统</title>
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/jquery.js"></script>

    <script type="text/javascript">
        //给退出登陆添加单击事件，通过id选择器
        $(function () {
            $("#out").click(function () {
                return window.confirm("你需要退出登陆吗?")
            })
        })
    </script>
</head>
<body style="background-color:#f2f9fd;">
<div class="header bg-main" style="border:solid 1px">
    <div class="logo margin-big-left fadein-top">
        <h1><img src="images/y.jpg" class="radius-circle rotate-hover" height="50" alt=""/>用户管理系统</h1>
    </div>

    <div style="position:relative;left:800px"><span style="font-size:20px;color:white;">
    欢迎:<%=((User) session.getAttribute("user")).getUname() %>进入系统</span> &nbsp;&nbsp;
        <a id="out" class="button button-little bg-red" href="<%=basePath%>/auth?method=UserOut">
            <span>退出登录</span>
        </a>
    </div>
</div>

<div>
    <%if(((User) session.getAttribute("user")).getUname().equals("admin")){%>
    <a href="<%=basePath%>/user?method=UserList">
        <span>用户管理</span>
    </a>
    <%}%>
</div>
</body>
</html>
