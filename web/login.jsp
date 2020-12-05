<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
    <title>登录</title>
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <script src="js/jquery.js"></script>
    <script src="js/pintuer.js"></script>

    <script>
        window.onload = function(){
            //1.获取图片对象
            var img = document.getElementById("checkCode");
            //2.绑定单击事件
            img.onclick = function(){
                // 加时间戳，避免浏览器缓存图片导致点击切换不了验证码
                var date = new Date().getTime();
                img.src = "<%=basePath%>/auth?method=VerifyCode&"+date;
            }
        }
    </script>
</head>
<body>
<div>
    <div class="line bouncein">
        <div class="xs6 xm4 xs3-move xm4-move">
            <div style="height:150px;"></div>
            <div class="media media-y margin-big-bottom">
            </div>
            <form action="<%=basePath%>/auth" method="post">
                <!-- 声明处理请求的方法 -->
                <input type="hidden" name="method" value="Userlogin"/>
                <div class="panel loginbox">
                    <div class="text-center margin-big padding-big-top"><h1>用户管理系统</h1></div>

                    <%
                        // 获取session中的标记
                        Object obj = session.getAttribute("flag");
                        if (obj != null) {
                            if ("not_open".equals((String) obj)) { %>
                            <div style="text-align:center;color:red" ;><h1>当前用户被禁用，请联系管理员！</h1></div>
                            <% } else
                            if ("invalid".equals((String) obj)) { %>
                            <div style="text-align:center;color:red" ;><h1>用户邮箱未验证</h1></div>
                            <% } else if ("login_false".equals((String) obj)) { %>
                            <div style="text-align:center;color:red" ;><h1>用户名或密码错误</h1></div>
                            <% } else if ("verify_code_error".equals((String) obj)) { %>
                            <div style="text-align:center;color:red" ;><h1>验证码错误</h1></div>
                            <% } else if ("reg_success".equals((String) obj)) { %>
                            <div style="text-align:center;color:red" ;><h1>新用户注册成功，请完成邮箱验证后登录</h1></div>
                            <%
                            }
                        }
                        // 销毁session
                        session.invalidate();
                        %>

                    <div style="padding:30px; padding-bottom:10px; padding-top:10px;">
                        <div>
                            <div class="field field-icon-right">
                                <input type="text" class="input input-big" name="uname" placeholder="登录账号"
                                       data-validate="required:请填写账号"/>
                                <span class="icon icon-user margin-small"></span>
                            </div>
                        </div>
                        <div>
                            <div class="field field-icon-right">
                                <input type="password" class="input input-big" name="pwd" placeholder="登录密码"
                                       data-validate="required:请填写密码"/>
                                <span class="icon icon-key margin-small"></span>
                            </div>
                        </div>
                        <div>
                            <div>
                                <input type="text" class="input input-big" name="code" placeholder="验证码"
                                       data-validate="required:请填写验证码"/>
                                <img id="checkCode" src="<%=basePath%>/auth?method=VerifyCode" />
                            </div>
                        </div>
                    </div>
                    <div style="padding:30px;"><input type="submit"
                                                      class="button button-block bg-main text-big input-big" value="登录">
                    </div>
                    <div style="font-size:20px; position:relative;left:300px;top:-20px"><a href="reg.jsp">注册</a></div>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
