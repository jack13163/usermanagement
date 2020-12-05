package jack.smartbi.filter;

import jack.smartbi.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户登录过滤器，当用户未登录的时候，重定向到登录页面
 */
public class AuthFilter implements Filter {
    private String ignoreRegex;
    private String[] ignoreRegexArray;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ignoreRegex = filterConfig.getInitParameter("ignoreRegex");
        if (ignoreRegex != null && !ignoreRegex.equals("")) {
            // 忽略的路径以逗号分隔
            ignoreRegexArray = ignoreRegex.split(",");
        }
        return;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        // 判断是否在过滤url之外
        boolean isExcludedPage = false;
        for (String page : ignoreRegexArray) {
            if (req.getServletPath().contains(page)) {
                isExcludedPage = true;
                break;
            }
        }

        // 如果得不到URI，或者URI是后台地址，则直接返回
        if (uri.isEmpty() || isExcludedPage) {
            chain.doFilter(req, resp);
            return;
        }

        // 前面已经过滤掉需要排除的路径，因此下面只需要检查在session中是否存在用户的信息
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 前往登录页面
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            // session中有用户信息
            if (uri.endsWith("admin.jsp")) {
                // 访问受保护的管理员界面
                if (user.getUname().equals("admin")) {
                    // 当前用户为内置用户，有权限，放行
                    chain.doFilter(request, response);
                } else {
                    // 没有权限，前往401页面
                    resp.sendRedirect(req.getContextPath() + "/401.jsp");
                }
            } else {
                // 其他页面直接放行
                chain.doFilter(request, response);
            }
        }
    }
}
