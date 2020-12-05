package jack.smartbi.servlet;

import jack.smartbi.service.UserService;
import jack.smartbi.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends BaseServlet {
    UserService userService = new UserServiceImpl();

    /**
     * 管理员用户列表
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void UserList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 重定向
        resp.sendRedirect(req.getContextPath() + "/admin.jsp");
    }

    /**
     * 启用用户
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    public void UserEnable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    public void UserDisable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
    public void UserDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求信息
        String uid = req.getParameter("uid");
        if(uid != null) {
            userService.deleteUser(Integer.parseInt(uid));
            resp.sendRedirect(req.getContextPath() + "/admin.jsp");
        }
    }
}
