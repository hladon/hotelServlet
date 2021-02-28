package controller;

import org.apache.log4j.Logger;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;

public class AdminRemove implements Command{

    private static final Logger LOGGER = Logger.getLogger(AdminRemove.class.toString());
    private final ReservationService reservationService=ReservationService.getReservationService();

    @Override
    public String execute(HttpServletRequest request) {

        try {
            Integer reservationId=Integer.parseInt(request.getParameter("id"));
            reservationService.deleteReservation(reservationId);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }
        return "redirect:admin/orders";
    }
}
