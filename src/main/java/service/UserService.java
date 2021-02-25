package service;

import DAO.UserDAO;
import entity.Role;
import entity.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Optional;


public class UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    private static UserService userService;
    UserDAO userDAO = UserDAO.getUserDAO();

    private UserService() {
    }

    public static UserService getUserService() {
        if (userService == null)
            userService = new UserService();
        return userService;
    }

    public boolean createUser(String name, String password) {
        User user = new User();
        user.setUserName(name);
        user.setPassword(password);
        user.setActive(true);
        user.setRole(Role.USER);
        Optional<User> us = userDAO.save(user);
        if (!us.isPresent())
            return false;
        return true;
    }


    public boolean validate(String username, String password, HttpSession session) {
        Optional<User> user = userDAO.findByUserName(username);
        if (!user.isPresent()) {
            return false;
        }
        User userEnt = user.get();
        if (userEnt.getPassword().equals(password)) {
            session.setAttribute("user", userEnt);
            session.setAttribute("role", userEnt.getRole().getRole());
            return true;
        }

        return false;

    }
}
