package controller;

import entity.User;
import org.apache.log4j.Logger;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class ReservationPost implements Command {
    private static final Logger LOGGER = Logger.getLogger(ReservationPost.class.toString());
    ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        try {
            LocalDate startRent = LocalDate.parse(request.getParameter("startRent"));
            LocalDate endRent =LocalDate.parse( request.getParameter("endRent"));
            Integer capacity =Integer.parseInt( request.getParameter("capacity"));
            User user = (User) request.getSession().getAttribute("user");
            reservationService.createOrder(startRent, endRent, user, capacity);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return "redirect:/user/orders";
    }
}
