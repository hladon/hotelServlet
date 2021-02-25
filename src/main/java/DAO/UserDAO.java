package DAO;

import entity.Role;
import entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Optional;

public class UserDAO {
    private final static Logger LOGGER = Logger.getLogger(UserDAO.class);

    private static UserDAO userDAO;
    private static final String GET_USER = "SELECT * FROM userDB WHERE user_name = ? ";
    private static final String SAVE_USER = "INSERT INTO userDB (user_name,password,role,active) VALUES (?,?,?,?) ";

    private UserDAO() {
    }

    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public Optional<User> findByUserName(String name) {
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_USER)) {
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUserName(name);
                user.setPassword(rs.getString("password"));
                user.setRole(Role.valueOf(rs.getString("role")));
                user.setActive(rs.getBoolean("active"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
        }
        return Optional.empty();
    }

    public Optional<User> save(User user) {
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole().getRole());
            ps.setBoolean(4, user.isActive());
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return Optional.of(user);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
        }
        return Optional.empty();
    }

}
