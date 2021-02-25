package controller;

import service.RoomService;

import javax.servlet.http.HttpServletRequest;

public class AdminRooms implements Command {

    private RoomService roomService = RoomService.getRoomService();

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("rooms", roomService.findAll());
        return "/adminRoom.jsp";
    }
}
