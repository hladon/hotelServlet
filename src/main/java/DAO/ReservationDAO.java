package DAO;

import entity.Reservation;
import entity.ReservationStatus;
import entity.Room;
import entity.User;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAO {
    private final static Logger LOGGER = Logger.getLogger(ReservationDAO.class);


    private static ReservationDAO reservationDAO;

    private static final String SAVE_RESERVATION = "INSERT INTO reservation " +
            "(fk_user_id,fk_room_id,start_rent,end_rent,capacity,status) VALUES (?,?,?,?,?,?) ";

    private static final String GET_RESERVATION_BY_USER = "SELECT * FROM reservation  WHERE fk_user_id=?";
    private static final String GET_RESERVATION = "SELECT * FROM reservation ";
    private static final String DELETE_RESERVATION = "DELETE FROM reservation WHERE reservation_id=? ";
    private static final String UPDATE_RESERVATION_ROOM_STATUS = "UPDATE reservation SET fk_room_id=?,status=?" +
            "WHERE reservation_id=? ";

    private ReservationDAO() {
    }

    public static ReservationDAO getReservationDAO() {
        if (reservationDAO == null) {
            reservationDAO = new ReservationDAO();
        }
        return reservationDAO;
    }

    public List<Reservation> findAll() {
        List<Reservation> res = new ArrayList<>();
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_RESERVATION)) {
            rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setCapacity(rs.getInt("capacity"));
                User user = new User();
                user.setUserId(rs.getInt("fk_user_id"));
                reservation.setUserId(user.getUserId());
                if (rs.getString("status") != null)
                    reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setStartRent(rs.getDate("start_rent").toLocalDate());
                reservation.setEndRent(rs.getDate("end_rent").toLocalDate());
                Room room = new Room();
                room.setRoomId(rs.getInt("fk_room_id"));
                reservation.setRoomId(room.getRoomId());
                res.add(reservation);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return res;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
            return res;
        }
    }

    public List<Reservation> findAllByUser(User user) {
        List<Reservation> res = new ArrayList<>();
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_RESERVATION_BY_USER)) {
            ps.setInt(1, user.getUserId());
            rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setCapacity(rs.getInt("capacity"));
                reservation.setUserId(user.getUserId());
                if (rs.getString("status") != null)
                    reservation.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                reservation.setStartRent(rs.getDate("start_rent").toLocalDate());
                reservation.setEndRent(rs.getDate("end_rent").toLocalDate());
                Room room = new Room();
                room.setRoomId(rs.getInt("fk_room_id"));
                reservation.setRoomId(room.getRoomId());
                res.add(reservation);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return res;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
            return res;
        }
    }

    public Optional<Reservation> save(Reservation reservation) {
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_RESERVATION, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getRoomId());
            ps.setDate(3, Date.valueOf(reservation.getStartRent()));
            ps.setDate(4, Date.valueOf(reservation.getEndRent()));
            ps.setInt(5, reservation.getCapacity());
            ps.setObject(6, reservation.getStatus(), java.sql.Types.OTHER);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setReservationId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating reservation failed, no ID obtained.");
                }
            }
            return Optional.of(reservation);
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

    public boolean updateRoomAndStatus(Reservation reservation) {
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_RESERVATION_ROOM_STATUS)) {

            ps.setInt(1, reservation.getRoomId());
            ps.setObject(2, reservation.getStatus(), java.sql.Types.OTHER);
            ps.setInt(3, reservation.getReservationId());

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
        }
        return true;
    }

    public boolean deleteById(int id) {
        ResultSet rs = null;
        try (Connection connection = ConnectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deleting failed, no rows affected.");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
        }
        return true;
    }
}
