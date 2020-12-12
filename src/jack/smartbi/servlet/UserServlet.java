package jack.smartbi.servlet;

import jack.smartbi.service.UserService;
import jack.smartbi.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends BaseServlet {
    private static final Logger logger = Logger.getLogger(UserServlet.class.getName());

    UserService userService = new UserServiceImpl();

    /**
     * 管理员用户列表
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        logger.info("重定向到管理员用户列表");
        resp.sendRedirect(req.getContextPath() + "/admin.jsp");
    }

    /**
     * 启用用户
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userEnable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uid = req.getParameter("uid");
        if(uid != null) {
            userService.enableUser(Integer.parseInt(uid));
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        }
    }

    /**
     * 禁用用户
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userDisable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uid = req.getParameter("uid");
        if(uid != null) {
            userService.disableUser(Integer.parseInt(uid));
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        }
    }

    /**
     * 删除用户
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void userDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uid = req.getParameter("uid");
        if(uid != null) {
            userService.deleteUser(Integer.parseInt(uid));
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        }
    }
}
