package DAO;

import entity.Category;
import entity.ReservationStatus;
import entity.Room;
import entity.RoomAndStatus;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    private final static Logger LOGGER = Logger.getLogger(RoomDAO.class);

    private static ConnectorDAO connectorDAO=new ConnectorDAO();
    private static RoomDAO roomDAO;

    private static final String SAVE_ROOM = "INSERT INTO room (room_name_en,room_name_ua,price,capacity,category) VALUES (?,?,?,?,?) ";
    private static final String GET_ROOM = "SELECT * FROM room ";
    private static final String COUNT_ROOM = "SELECT count(room_id) FROM room WHERE capacity>=? ";

    private RoomDAO() {
    }

    public static RoomDAO getRoomDAO() {
        if (roomDAO == null) {
            roomDAO = new RoomDAO();
        }
        return roomDAO;
    }

    public List<Room> findAll() {
        List<Room> res = new ArrayList<>();
        ResultSet rs = null;
        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ROOM)) {
            rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setCapacity(rs.getInt("capacity"));
                room.setPrice(rs.getInt("price"));
                room.setCategory(Category.valueOf(rs.getString("category")));
                room.setRoomNameEn(rs.getString("room_name_en"));
                room.setRoomNameUa(rs.getString("room_name_ua"));
                res.add(room);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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

    public List<RoomAndStatus> findRoomsWithStatus(Date start, Date end, int capacity, String order, int limit, int offset) {
        List<RoomAndStatus> res = new ArrayList<>();
        ResultSet rs = null;
        String GET_ROOMS_RESERVATION = "SELECT room.*,status FROM room left join (SELECT * FROM reservation WHERE start_rent<= ?" +
                "            AND end_rent>=?) as res " +
                " ON room_id=fk_room_id  where  room.capacity>=? order by "
                + order +
                " desc limit ? offset ?";
        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_ROOMS_RESERVATION)) {
            ps.setDate(1, end);
            ps.setDate(2, start);
            ps.setInt(3, capacity);
            ps.setInt(4, limit);
            ps.setInt(5, offset);
            rs = ps.executeQuery();
            while (rs.next()) {
                RoomAndStatus roomAndStatus = new RoomAndStatus();
                roomAndStatus.setRoomId(rs.getInt("room_id"));
                if (rs.getString("status") != null)
                    roomAndStatus.setStatus(ReservationStatus.valueOf(rs.getString("status")));
                roomAndStatus.setCapacity(rs.getInt("capacity"));
                roomAndStatus.setPrice(rs.getInt("price"));
                roomAndStatus.setCategory(Category.valueOf(rs.getString("category")));
                roomAndStatus.setRoomNameEn(rs.getString("room_name_en"));
                roomAndStatus.setRoomNameUa(rs.getString("room_name_ua"));
                res.add(roomAndStatus);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
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

    public Optional<Room> save(Room room) {
        ResultSet rs = null;
        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_ROOM, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, room.getRoomNameEn());
            ps.setString(2, room.getRoomNameUa());
            ps.setInt(3, room.getPrice());
            ps.setInt(4, room.getCapacity());
            ps.setObject(5, room.getCategory(), java.sql.Types.OTHER);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Creating room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    room.setRoomId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating room failed, no ID obtained.");
                }
            }
            return Optional.ofNullable(room);
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

    public int getPageNumber(int capacity){
        ResultSet rs = null;
        int pages=1;
        try (Connection connection = connectorDAO.getConnection();
             PreparedStatement ps = connection.prepareStatement(COUNT_ROOM) ){
            ps.setInt(1, capacity);
            rs = ps.executeQuery();
            while (rs.next()) {
                pages=rs.getInt("count");
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
        return pages;
    }
}
