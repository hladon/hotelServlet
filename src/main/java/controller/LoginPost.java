package controller;

import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginPost implements Command {
    UserService userService = UserService.getUserService();

    @Override
    public String execute(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        if (userService.validate(username, password, session)) {
            return "redirect:/";
        } else {
            request.setAttribute("message", "Wrong username or password");
            return "/login.jsp";
        }

    }
}
