package filters;

import entity.Role;
import entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SecurityAdminFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpSession session = httpReq.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && user.getRole() == Role.ADMIN) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            RequestDispatcher rd = servletRequest.getRequestDispatcher("/login");
            rd.include(servletRequest, servletResponse);
        }
    }

    public void destroy() {
    }
}
