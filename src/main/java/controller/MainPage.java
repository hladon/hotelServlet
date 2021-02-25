package controller;

import service.RoomService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Logger;

public class MainPage implements Command {

    private static final Logger LOGGER = Logger.getLogger(MainPage.class.toString());

    private RoomService roomService = RoomService.getRoomService();

    @Override
    public String execute(HttpServletRequest request) {
        Optional<String> pageStr = Optional.ofNullable(request.getParameter("page"));
        Optional<String> sortType = Optional.ofNullable(request.getParameter("sort"));
        Optional<String> startRent = Optional.ofNullable(request.getParameter("startRent"));
        Optional<String> endRent = Optional.ofNullable(request.getParameter("endRent"));
        Optional<String> capacity = Optional.ofNullable(request.getParameter("capacity"));
        Integer cap;
        Integer page;
        Date startRentDate;
        Date endRentDate;
        try {
            cap = Integer.parseInt(capacity.orElse("1"));
            page = Integer.parseInt(pageStr.orElse("1"));
            startRentDate = Date.valueOf(startRent.orElse(LocalDate.now().toString()));
            endRentDate = Date.valueOf(endRent.orElse(LocalDate.now().plusDays(1).toString()));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            cap = 1;
            page = 1;
            startRentDate = Date.valueOf(LocalDate.now());
            endRentDate = Date.valueOf(LocalDate.now().plusDays(1));
        }
        request.setAttribute("startRent", startRent.orElse(LocalDate.now().toString()));
        request.setAttribute("endRent", endRent.orElse(LocalDate.now().plusDays(1).toString()));
        request.setAttribute("rooms", roomService.getRooms(startRentDate, endRentDate, cap, sortType, page));
        request.setAttribute("sortType", sortType);
        request.setAttribute("pageNumbers", 2);

        return "/hotelRooms.jsp";
    }
}
