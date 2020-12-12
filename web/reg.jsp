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
    <title>用户注册页面</title>
    <link rel="stylesheet" href="css/pintuer.css">
    <link rel="stylesheet" href="css/admin.css">
    <link rel="stylesheet" href="css/common.css">
    <script src="js/jquery.js"></script>
    <script src="js/pintuer.js"></script>

    <script type="text/javascript">
        // 检验邮箱是否正确
        function checkEmail(str){
            var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
            if(re.test(str)){
                return true;
            }else{
                return false;
            }
        }

        // 表单检查
        function check() {
            var uname = document.getElementById("uname").value;
            var pwd = document.getElementById("pwd").value;
            var pwdRep = document.getElementById("pwdRep").value;
            var email = document.getElementById("email").value;
            var msgNode = document.getElementById("msg");

            if (uname == "") {
                msgNode.innerText = "用户名不能为空";
                return false;
            }
            if (pwd == "") {
                msgNode.innerText = "密码不能为空";
                return false;
            }
            if (pwd != pwdRep) {
                msgNode.innerText = "密码不一致，请重输入";
                return false;
            }
            if (email == "") {
                msgNode.innerText = "邮箱不能为空";
                return false;
            }
            if(!checkEmail(email)){
                msgNode.innerText = "邮箱格式不正确，请重输入";
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<div>
    <form method="post" action="<%=basePath%>/auth" onsubmit="return check();">
        <!--隐藏域，用于区分后台调用的servlet的方法-->
        <input type="hidden" name="method" value="userReg"/>
        <fieldset>
            <div>
                <label>用户名：</label>
            </div>
            <div>
                <input type="text" class="input w50" id="uname" name="uname" size="50" placeholder="请输入新用户名"
                       data-validate="required:请输入原始密码"/>
            </div>
        </fieldset>

        <fieldset>
            <div>
                <label>新密码：</label>
            </div>
            <div>
                <input type="password" class="input w50" id="pwd" name="pwd" size="50" placeholder="请输入新密码"
                       data-validate="required:请输入新密码,length#>=5:新密码不能小于5位"/>
            </div>
        </fieldset>

        <fieldset>
            <div>
                <label>确认新密码：</label>
            </div>
            <div>
                <input type="password" class="input w50" id="pwdRep" name="pwdRep" size="50" placeholder="请再次输入新密码"
                       data-validate="required:请再次输入新密码,repeat#pwd:两次输入的密码不一致"/>
            </div>
        </fieldset>

        <fieldset>
            <div>
                <label>电子邮件：</label>
            </div>
            <div>
                <input type="text" class="input w50" id="email" name="email" size="50" placeholder="请输入电子邮件地址"/>
            </div>
        </fieldset>

        <fieldset>
            <%
                // 获取session中的标记
                Object obj = session.getAttribute("flag");
                if (obj != null) {
                    if ("user_exists".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>用户已存在，请更换用户名！</h1></div>
            <% } else if ("uname_null".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>用户名不能为空！</h1></div>
            <% } else if ("pwd_null".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>密码不能为空！</h1></div>
            <% } else if ("pwdRep_null".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>两次输入的密码不一致！</h1></div>
            <% } else if ("email_null".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>邮箱不能为空！</h1></div>
            <% } else if ("email_check_fail".equals((String) obj)) { %>
            <div style="text-align:center;color:red" ;><h1>邮箱格式不正确！</h1></div>
            <% }
            }
                // 销毁session
                session.invalidate();
            %>
            <div id="msg" style="text-align:center;color:red" ;></div>
        </fieldset>

        <fieldset>
            <div>
                <label></label>
            </div>
            <div>
                <button class="button bg-main icon-check-square-o" type="submit"> 提交</button>
            </div>
        </fieldset>
    </form>
</div>
</body>
</html>
