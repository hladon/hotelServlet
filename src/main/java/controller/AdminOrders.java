package controller;

import service.ReservationService;

import javax.servlet.http.HttpServletRequest;

public class AdminOrders implements Command {

    private ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("reservations", reservationService.findAll());
        return "/adminReservation.jsp";
    }
}
