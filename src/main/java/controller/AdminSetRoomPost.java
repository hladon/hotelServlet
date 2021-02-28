package controller;

import org.apache.log4j.Logger;
import service.ReservationService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;

public class AdminSetRoomPost implements Command{
    private static final Logger LOGGER = Logger.getLogger(AdminSetRoomPost.class.toString());

    private final ReservationService reservationService=ReservationService.getReservationService();
    @Override
    public String execute(HttpServletRequest request) {
        try {
            Integer reservation =Integer.parseInt(request.getParameter("reservation"));
            Integer roomId=Integer.parseInt(request.getParameter("roomId"));
            Date start=Date.valueOf(request.getParameter("start"));
            Date end=Date.valueOf(request.getParameter("end"));
            reservationService.setRoom(reservation,roomId,start,end);
        }catch (Exception e){
            LOGGER.info(e.getMessage());
        }

        return "redirect:admin/orders";
    }
}
