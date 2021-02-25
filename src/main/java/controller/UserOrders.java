package controller;

import entity.User;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserOrders implements Command {

    private ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("reservations", reservationService.findAllByUser(user));
        return "/userReservation.jsp";
    }
}
