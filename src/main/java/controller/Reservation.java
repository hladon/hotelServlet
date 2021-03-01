package controller;

import entity.User;
import org.apache.log4j.Logger;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class Reservation implements Command {

    private static final Logger LOGGER = Logger.getLogger(Reservation.class.toString());
    ReservationService reservationService = ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        try{
            LocalDate startRent =LocalDate.parse( request.getParameter("start"));
            LocalDate endRent =LocalDate.parse( request.getParameter("end"));
            Integer roomId = Integer.parseInt(request.getParameter("room"));
            User user = (User) request.getSession().getAttribute("user");
            reservationService.createReservation(startRent, endRent, user, roomId);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

        return "redirect:user/orders";
    }
}
