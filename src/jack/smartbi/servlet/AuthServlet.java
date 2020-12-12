package jack.smartbi.servlet;

import jack.smartbi.entity.User;
import jack.smartbi.mail.MailSender;
import jack.smartbi.service.UserLoginInfoService;
import jack.smartbi.service.UserService;
import jack.smartbi.service.impl.UserLoginInfoServiceImpl;
import jack.smartbi.service.impl.UserServiceImpl;
import jack.smartbi.util.EmailUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 用于处理认证逻辑的处理器，过滤器忽略该过滤器
 */
public class AuthServlet extends BaseServlet {
    UserLoginInfoService userLoginInfoService = new UserLoginInfoServiceImpl();
    UserService userService = new UserServiceImpl();

    /**
     * 用户邮箱验证
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userActive(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uid = req.getParameter("uid");
        int i = userService.activeUser(Integer.parseInt(uid));
        // 判断是否激活成功
        if (i > 0) {
            // 激活成功重定向到登录页面
            resp.sendRedirect(req.getContextPath() + "/success.jsp");
        }
    }

    /**
     * 注册处理方法
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userReg(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uname = req.getParameter("uname");
        String pwd = req.getParameter("pwd");
        String pwdRep = req.getParameter("pwdRep");
        String email = req.getParameter("email");

        String path = req.getContextPath();

        // 参数校验
        HttpSession hs = req.getSession();
        boolean flag = true;
        if(uname == null || uname.equals("")){
            hs.setAttribute("flag", "uname_null");
            flag = false;
        }else if(pwd == null || pwd.equals("")){
            hs.setAttribute("flag", "pwd_null");
            flag = false;
        }else if(pwdRep == null || pwdRep.equals("") || !pwd.equals(pwdRep)){
            hs.setAttribute("flag", "pwdRep_null");
            flag = false;
        }else if(email == null || email.equals("")){
            hs.setAttribute("flag", "email_null");
            flag = false;
        }else if(!EmailUtil.isEmail(email)){
            hs.setAttribute("flag", "email_check_fail");
            flag = false;
        }
        if(!flag){
            // 参数校验失败，重定向到注册页面
            resp.sendRedirect(path + "/reg.jsp");
            return;
        }

        // 判断用户是否已经存在
        User u = userService.getUserInfoByName(uname);
        if(u != null){
            // 用户已经存在于数据库中
            hs.setAttribute("flag", "user_exists");
            resp.sendRedirect( path + "/reg.jsp");
            return;
        }

        //获取业务层对象
        int i = userService.regUserInfo(uname, pwd, pwd, email);

        //重定向到登陆界面
        if (i > 0) {
            //注册成功，添加标记到session
            hs.setAttribute("flag", "reg_success");

            // 发送激活邮件
            String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path;
            // i为用户id
            String url = basePath + "/auth?method=UserActive&uid=" + i;
            MailSender.sendEmail(email, uname, url);

            // 重定向到登录页
            resp.sendRedirect(path + "/login.jsp");
        } else {
            //重定向到注册页面
            resp.sendRedirect(path + "/reg.jsp");
        }
    }

    /**
     * 登陆处理思路
     * 1.需要一个表(t_user_login)负责记录用户登录的信息，不管登录成功还是失败都记录。
     * 并且登陆失败还是成功需要能够区分开来。
     * <p>
     * 2.每次登陆时，都先从表中查询最近30分钟内(这里假设密码错误次数达到5次后，禁用用户30分钟)
     * 有没有相关密码错误的记录，然后统计一下记录总条数是否达到设定的错误次数。
     * <p>
     * 3.如果在相同IP下，同一个用户，在30分钟内密码错误次数达到设定的错误次数，就不让用户登录了。
     */
    public void userlogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 获取请求信息
        String uname = req.getParameter("uname");
        String pwd = req.getParameter("pwd");
        String code = req.getParameter("code");
        String path = req.getContextPath();

        // 获取session对象
        HttpSession hs = req.getSession();
        boolean f = true;
        if(uname == null || uname.equals("")){
            hs.setAttribute("flag", "uname_null");
            f = false;
        }else if(pwd == null || pwd.equals("")){
            hs.setAttribute("flag", "pwd_null");
            f = false;
        }
        if(!f){
            // 参数校验失败，重定向到注册页面
            resp.sendRedirect(path + "/login.jsp");
            return;
        }

        // 校验验证码
        Object verifyCode = hs.getAttribute("verifyCode");
        if (verifyCode == null || !verifyCode.toString().equalsIgnoreCase(code)) {
            // 验证码校验不通过
            hs.setAttribute("flag", "verify_code_error");
            // 重定向到login.jsp
            resp.sendRedirect(path + "/login.jsp");
            return;
        }

        // 判断用户的状态是否正常:-1:未通过邮箱认证 0:未锁定状态; -2:代表锁定状态
        User u = userService.getUserInfoByName(uname);
        if (u == null) {
            // 用户不存在
            hs.setAttribute("flag", "login_false");
            resp.sendRedirect(path + "/login.jsp");
        } else {
            // 检查用户的状态是否正常
            if (!checkUserStatus(u, hs)) {
                // 用户状态异常
                resp.sendRedirect(path + "/login.jsp");
                return;
            }

            // 用户名密码校验
            if (!u.getPwd().equals(pwd)) {
                // 用户名密码校验失败，判断是否需要禁用用户
                boolean flag = userLoginInfoService.checkPassWrongTime(u.getUid(), 30, 3);
                if (flag) {
                    userService.disableUser(u.getUid());
                }
                // 记录登陆日志
                userLoginInfoService.recordLoginLog(u.getUid(), false, req.getRemoteAddr());
                // 添加登陆标记
                hs.setAttribute("flag", "login_false");
                // 重定向到login.jsp
                resp.sendRedirect(path + "/login.jsp");
            } else {
                // 登陆成功，将用户信息存储到session
                hs.setAttribute("user", u);
                // 记录登陆日志
                userLoginInfoService.recordLoginLog(u.getUid(), true, req.getRemoteAddr());
                // 重定向到首页
                resp.sendRedirect(path + "/index.jsp");
            }
        }
    }

    /**
     * 检查用户是否具有权限
     *
     * @param u
     * @param hs
     * @return
     */
    private boolean checkUserStatus(User u, HttpSession hs) {
        if (u.getStatus() == -2) {
            // 管理员没有开启用户
            hs.setAttribute("flag", "not_open");
            return false;
        } else if (u.getStatus() == -1) {
            // 用户邮箱未验证
            hs.setAttribute("flag", "invalid");
            return false;
        }
        return true;
    }

    /**
     * 退出处理方法
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userOut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 获取session、销毁session
        HttpSession hs = req.getSession();
        hs.invalidate();
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    /**
     * 获取验证码功能实现
     *
     * @param req
     * @param resp
     */
    public void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession hs = req.getSession();

        //定义需要显示的图片的宽度和高度
        int width = 100;
        int height = 50;

        //1.创建一对象，在内存中图片(验证码图片对象)
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //2.美化图片
        //2.1 填充背景色
        Graphics g = image.getGraphics();
        g.setColor(Color.PINK);
        //图片坐标起始点是左上角
        g.fillRect(0, 0, width, height);

        //2.2画边框
        g.setColor(Color.BLUE);
        //width和height分别-1，为了边框右半部分有显示
        g.drawRect(0, 0, width - 1, height - 1);
        //定义可以在页面上显示字符
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghigklmnopqrstuvwxyz0123456789";
        //生成随机角标
        Random ran = new Random();
        String verifyCode = "";
        //验证码显示字符个数为4
        for (int i = 1; i <= 4; i++) {
            int index = ran.nextInt(str.length());
            //获取字符
            char ch = str.charAt(index);
            verifyCode += ch;
            //2.3写验证码
            //具体验证码显示位置定义
            g.drawString(ch + "", width / 5 * i, height / 2);
        }

        //2.4画干扰线
        g.setColor(Color.GREEN);

        //随机生成坐标点
        for (int i = 0; i < 10; i++) {
            int x1 = ran.nextInt(width);
            int x2 = ran.nextInt(width);

            int y1 = ran.nextInt(height);
            int y2 = ran.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        //3.将图片输出到页面展示
        ImageIO.write(image, "jpg", resp.getOutputStream());

        // 验证码生成成功，将验证码存储到session中
        hs.setAttribute("verifyCode", verifyCode);
    }

}
