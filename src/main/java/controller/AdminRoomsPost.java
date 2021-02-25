package controller;

import entity.Category;
import entity.Room;
import service.RoomService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

public class AdminRoomsPost implements Command {

    private static final Logger LOGGER = Logger.getLogger(AdminRoomsPost.class.toString());

    private RoomService roomService = RoomService.getRoomService();

    @Override
    public String execute(HttpServletRequest request) {
        Room room = new Room();
        room.setRoomNameEn(request.getParameter("roomNameEn"));
        room.setRoomNameUa(request.getParameter("roomNameEn"));
        try {
            room.setCapacity(Integer.parseInt(request.getParameter("capacity")));
            room.setPrice(Integer.parseInt(request.getParameter("price")));
            room.setCategory(Category.valueOf(request.getParameter("category")));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            request.setAttribute("message","Wrong input!");
        }
        roomService.addRoom(room);
        return "redirect:/admin/rooms";
    }
}
