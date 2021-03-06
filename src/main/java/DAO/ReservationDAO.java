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

    private static ConnectorDAO connectorDAO = new ConnectorDAO();
    private static ReservationDAO reservationDAO;

    private static final String SAVE_RESERVATION = "INSERT INTO reservation " +
            "(fk_user_id,fk_room_id,start_rent,end_rent,capacity,status) VALUES (?,?,?,?,?,?) ";

    private static final String GET_RESERVATION_BY_USER = "SELECT * FROM reservation  WHERE fk_user_id=?";
    private static final String GET_RESERVATION = "SELECT * FROM reservation ";
    private static final String DELETE_RESERVATION = "DELETE FROM reservation WHERE reservation_id=? ";
    private static final String DELETE_USER_RESERVATION = "DELETE FROM reservation WHERE reservation_id=? AND fk_user_id=? ";
    private static final String IS_RESERVED = "SELECT reservation_id FROM reservation WHERE fk_room_id=? AND start_rent<? AND end_rent>? ";
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
        try (Connection connection = connectorDAO.getConnection();
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
        try (Connection connection = connectorDAO.getConnection();
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
        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement psCheck = connection.prepareStatement(IS_RESERVED);
             PreparedStatement ps = connection.prepareStatement(SAVE_RESERVATION, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            psCheck.setInt(1, reservation.getRoomId());
            psCheck.setDate(2, Date.valueOf(reservation.getEndRent()));
            psCheck.setDate(3, Date.valueOf(reservation.getStartRent()));
            rs = psCheck.executeQuery();
            if (rs.next()) {
                throw new SQLException("Creating reservation failed, room is ordered.");
            }
            ps.setInt(1, reservation.getUserId());
            if (reservation.getRoomId() == 0) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, reservation.getRoomId());
            }
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
            connection.commit();
            connection.setAutoCommit(false);
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
        try (Connection connection = connectorDAO.getConnection();
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

    public boolean updateRoomAndStatus(Integer roomId, ReservationStatus status, Integer reservationId, Date start, Date end) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;

        try (Connection connection = connectorDAO.getConnection()) {
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(IS_RESERVED);
            ps.setInt(1, roomId);
            ps.setDate(2, end);
            ps.setDate(3, start);

            ps2 = connection.prepareStatement(UPDATE_RESERVATION_ROOM_STATUS);
            ps2.setInt(1, roomId);
            ps2.setObject(2, status, java.sql.Types.OTHER);
            ps2.setInt(3, reservationId);
            rs = ps.executeQuery();
            if (rs.next()) {
                throw new SQLException("Creating reservation failed, room is ordered.");
            }
            if (ps2.executeUpdate() == 0) {
                throw new SQLException("Creating reservation failed, no rows affected.");
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        } finally {
            try {
                ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException throwables) {
                LOGGER.info(throwables.getMessage());
            }
        }
        return true;
    }

    public boolean deleteById(int id) {

        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION)) {

            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deleting failed, no rows affected.");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean deleteByIdAndUser(int id, int userId) {

        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER_RESERVATION)) {
            ps.setInt(1, id);
            ps.setInt(2, userId);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Deleting failed, no rows affected.");
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return false;
        }
        return true;
    }
}
