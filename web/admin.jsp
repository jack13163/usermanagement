<%@page import="jack.smartbi.entity.User" pageEncoding="UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
    <link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/bootstrap/4.0.0-beta.2/css/bootstrap.min.css" rel="external nofollow" >
    <script src="js/jquery.js"></script>

    <script type="text/javascript">
        //给退出登陆添加单击事件，通过id选择器
        $(function () {
            $("#out").click(function () {
                return window.confirm("你需要退出登陆吗?")
            });

            $.ajax({
                url:'<%=basePath%>user?method=userListJson',
                type:'get',
                dataType:'json',
                success:function(data){
                    //方法中传入的参数data为后台获取的数据
                    console.log(data);
                    for(var i = 0; i < data.length; i++){
                        var tr = '<td>'+data[i].uid+'</td>'+'<td>'+data[i].uname+'</td>'
                            +'<td>'+data[i].pwd+'</td>'+'<td>'+data[i].salt+'</td>'
                            +'<td>'+data[i].email+'</td>'
                            +'<td>'+ (data[i].status == -2 ? '锁定' : data[i].status == 0 ? '正常' : '未通过邮箱验证')+'</td>'
                            +'<td>';
                        if(data[i].status == -2){
                            tr += '<a href="javascript:void(0);" onclick="enableUser(' + data[i].uid + ')">启用</a>&nbsp;&nbsp;';
                        }else{
                            tr += '<a href="javascript:void(0);" onclick="disableUser(' + data[i].uid + ')">禁用</a>&nbsp;&nbsp;';
                        }
                        tr += '<a href="javascript:void(0);" onclick="delUser(' + data[i].uid + ')">删除</a>'+ '</td>'
                        $("#tabletest").append('<tr>'+tr+'</tr>');
                    }
                }
            });
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
                    method: 'userEnable',
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
                    method: 'userDisable',
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
                    method: 'userDelete',
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
    <table class="table table-bordered" id='tabletest'>
        <tr>
            <td>用户编号</td>
            <td>用户名</td>
            <td>密码</td>
            <td>盐值</td>
            <td>邮箱</td>
            <td>状态</td>
            <td>操作</td>
        </tr>
    </table>
</fieldset>
</body>
</html>
