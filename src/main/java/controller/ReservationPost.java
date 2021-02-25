package controller;

import entity.User;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;

public class ReservationPost implements Command {

    ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        String startRent = request.getParameter("startRent");
        String endRent = request.getParameter("endRent");
        String capacity = request.getParameter("capacity");
        User user = (User) request.getSession().getAttribute("user");
        reservationService.createOrder(startRent, endRent, user, capacity);
        return "redirect:/user/orders";
    }
}
