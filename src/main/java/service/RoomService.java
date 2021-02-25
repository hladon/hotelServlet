package service;


import DAO.RoomDAO;
import entity.Category;
import entity.Room;
import entity.RoomAndStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class RoomService {
    private static final Logger LOGGER = Logger.getLogger(RoomService.class.toString());

    RoomDAO roomDAO = RoomDAO.getRoomDAO();

    private final int PAGE_SIZE = 5;

    private static RoomService roomService;

    private RoomService() {
    }

    public static RoomService getRoomService() {
        if (roomService == null) {
            roomService = new RoomService();
        }
        return roomService;
    }

    public boolean addRoom(Room room) {

        if (!roomDAO.save(room).isPresent())
            return false;
        return true;
    }

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    public List<RoomAndStatus> getRooms(Date startRentDate, Date endRentDate, Integer cap, Optional<String> sortType, Integer page) {
        String sort = getSortType(sortType.get());
        int offset = PAGE_SIZE * (page - 1);
        return roomDAO.findRoomsWithStatus(startRentDate, endRentDate, cap, sort, PAGE_SIZE, offset);
    }

    private String getSortType(String sort) {
        if (sort == null)
            return "price";
        switch (sort) {
            case "capacity":
                return "capacity";
            case "status":
                return "status";
            case "category":
                return "category";
            default:
                return "price";
        }
    }

    private int getPageNumber(List list) {
        int i = list.size() / PAGE_SIZE;
        if (list.size() % PAGE_SIZE > 0)
            i++;
        return i;
    }
}
