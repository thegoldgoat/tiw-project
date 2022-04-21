package it.polimi.tiw.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserLoggedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession s = req.getSession();
        if (s.isNew() || s.getAttribute("UserPk") == null) {
            String loginPath = req.getServletContext().getContextPath() + "/index.html";
            res.sendRedirect(loginPath);
            return;
        }

        chain.doFilter(request, response);
    }
}
