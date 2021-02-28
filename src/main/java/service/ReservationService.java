package service;

import DAO.ReservationDAO;
import entity.Reservation;
import entity.ReservationStatus;
import entity.User;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class ReservationService {
    private static final Logger LOGGER = Logger.getLogger(ReservationService.class.toString());

    ReservationDAO reservationDAO = ReservationDAO.getReservationDAO();
    private static ReservationService reservationService;

    private ReservationService() {
    }

    public static ReservationService getReservationService() {
        if (reservationService == null) {
            reservationService = new ReservationService();
        }
        return reservationService;
    }

    public boolean setRoom(Integer reservation, Integer roomId, Date start,Date end){
        return reservationDAO.updateRoomAndStatus(roomId,ReservationStatus.BOOKED,reservation,start,end);
    }

    public boolean deleteReservation(Integer reservationId){
        return reservationDAO.deleteById(reservationId);
    }
    public boolean deleteUserReservation(Integer reservationId,Integer userId){
        return reservationDAO.deleteByIdAndUser(reservationId,userId);
    }
    public boolean createReservation(String start, String end, User user, String roomId) {
        Reservation reservation = new Reservation();
        try {
            reservation.setStartRent(LocalDate.parse(start));
            reservation.setEndRent(LocalDate.parse(end));
            reservation.setStatus(ReservationStatus.RESERVED);
            reservation.setUserId(user.getUserId());
            reservation.setRoomId(Integer.parseInt(roomId));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }

        if (reservationDAO.save(reservation).isPresent())
            return true;
        return false;
    }

    public boolean createOrder(String start, String end, User user, String capacity) {
        Reservation reservation = new Reservation();
        try {
            reservation.setStartRent(LocalDate.parse(start));
            reservation.setEndRent(LocalDate.parse(end));
            reservation.setStatus(ReservationStatus.BOOKED);
            reservation.setUserId(user.getUserId());
            reservation.setCapacity(Integer.parseInt(capacity));
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return false;
        }
        if (reservationDAO.save(reservation).isPresent())
            return true;
        return false;
    }


    public List<Reservation> findAllByUser(User user) {
        return reservationDAO.findAllByUser(user);
    }

    public List<Reservation> findAll() {
        return reservationDAO.findAll();
    }
}
