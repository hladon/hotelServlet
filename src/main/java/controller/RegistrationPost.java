package controller;

import service.UserService;

import javax.servlet.http.HttpServletRequest;

public class RegistrationPost implements Command {

    UserService userService = UserService.getUserService();

    @Override
    public String execute(HttpServletRequest request) {

        String username = request.getParameter("userName");
        String password = request.getParameter("password");

        if (userService.createUser(username, password)) {
            return "redirect:";
        } else {
            request.setAttribute("message", "Such user already exists!");
            return "/login.jsp";
        }

    }
}
