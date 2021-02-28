package controller;

import entity.User;
import org.apache.log4j.Logger;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;

public class UserRemove implements Command{
    private static final Logger LOGGER = Logger.getLogger(UserRemove.class.toString());
    private final ReservationService reservationService=ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {
        try {
            Integer reservationId=Integer.parseInt(request.getParameter("id"));
            User user=(User)request.getSession().getAttribute("user");
            reservationService.deleteUserReservation(reservationId,user.getUserId());
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return "redirect:user/orders";
    }
}
