<%@page import="jack.smartbi.entity.User" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="jack.smartbi.service.UserService" %>
<%@ page import="jack.smartbi.service.impl.UserServiceImpl" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    //查询用户列表
    UserService userService = new UserServiceImpl();
    List<User> userList = userService.getUserList();
%>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <meta name="renderer" content="webkit">
    <title>管理员后台页面</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/jquery.js"></script>

    <script type="text/javascript">
        //给退出登陆添加单击事件，通过id选择器
        $(function () {
            $("#out").click(function () {
                return window.confirm("你需要退出登陆吗?")
            })
        });

        // 虚拟表单的形式提交post请求
        function post(URL, PARAMS) {
            var temp = document.createElement("form");
            temp.action = URL;
            temp.method = "post";
            temp.style.display = "none";
            for (var x in PARAMS) {
                var opt = document.createElement("textarea");
                opt.name = x;
                opt.value = PARAMS[x];
                // alert(opt.name)
                temp.appendChild(opt);
            }
            document.body.appendChild(temp);
            temp.submit();
            return temp;
        }

        // 启用用户
        function enableUser(uid) {
            var flag = window.confirm("你确定启用id为" + uid + "的用户吗?");
            if (flag) {
                var url = "<%=basePath%>" + "user";
                var params = {
                    method: 'UserEnable',
                    uid: uid
                };
                post(url, params);
            }
        }

        // 禁用用户
        function disableUser(uid) {
            var flag = window.confirm("你确定禁用id为" + uid + "的用户吗?");
            if (flag) {
                var url = "<%=basePath%>" + "user";
                var params = {
                    method: 'UserDisable',
                    uid: uid
                };
                post(url, params);
            }
        }

        // 删除用户
        function delUser(uid) {
            var flag = window.confirm("你确定删除id为" + uid + "的用户吗?");
            if (flag) {
                var url = "<%=basePath%>" + "user";
                var params = {
                    method: 'UserDelete',
                    uid: uid
                };
                post(url, params);
            }
        }
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

<fieldset>
    <legend>用户列表</legend>
    <table width="100%" frame="border" style="text-align:center;">
        <tr>
            <td>用户编号</td>
            <td>用户名</td>
            <td>密码</td>
            <td>盐值</td>
            <td>邮箱</td>
            <td>状态</td>
            <td>操作</td>
        </tr>

        <c:forEach var="c" items="<%=userList%>">
            <tr>
                <td><c:out value="${c.uid }" escapeXml="true"/></td>
                <td><c:out value="${c.uname }" escapeXml="true"/></td>
                <td><c:out value="${c.pwd }" escapeXml="true"/></td>
                <td><c:out value="${c.salt }" escapeXml="true"/></td>
                <td><c:out value="${c.email }" escapeXml="true"/></td>
                <td>${c.status == -2 ? '锁定' : c.status == 0 ? '正常' : '未通过邮箱验证' }</td>

                <td>
                    <c:choose>
                        <c:when test="${c.status eq -2}">
                            <a href="javascript:void(0);" onclick="enableUser(${c.uid })">启用</a>
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:void(0);" onclick="disableUser(${c.uid })">禁用</a>
                        </c:otherwise>
                    </c:choose>
                    <a href="javascript:void(0);" onclick="delUser(${c.uid })">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</fieldset>
</body>
</html>
