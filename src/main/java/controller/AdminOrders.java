package controller;

import service.ReservationService;
import service.RoomService;

import javax.servlet.http.HttpServletRequest;

public class AdminOrders implements Command {

    private ReservationService reservationService = ReservationService.getReservationService();
    private RoomService roomService=RoomService.getRoomService();

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("reservations", reservationService.findAll());
        request.setAttribute("rooms",roomService.findAll());
        return "/adminReservation.jsp";
    }
}
