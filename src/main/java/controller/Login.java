package controller;

import service.UserService;

import javax.servlet.http.HttpServletRequest;

public class Login implements Command {
    UserService userService = UserService.getUserService();

    @Override
    public String execute(HttpServletRequest request) {
        return "/login.jsp";
    }
}
