package controller;

import entity.User;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;

public class Reservation implements Command {

    ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        String startRent = request.getParameter("start");
        String endRent = request.getParameter("end");
        String roomId = request.getParameter("room");
        User user = (User) request.getSession().getAttribute("user");
        reservationService.createReservation(startRent, endRent, user, roomId);
        return "redirect:/user/orders";
    }
}
