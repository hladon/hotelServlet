import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DAO.ReservationDAO;
import DAO.RoomDAO;
import entity.Reservation;
import entity.ReservationStatus;
import entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import service.ReservationService;

import java.time.LocalDate;
import java.util.Optional;

public class ReservationServiceTest extends BaseServiceTest {
    @Mock
    ReservationDAO reservationDAO;
    @InjectMocks
    ReservationService reservationService;

    @Test
    void createReservationTest() {
        int userId = 1;
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);
        int roomId = 1;
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setRoomId(roomId);
        reservation.setStartRent(start);
        reservation.setEndRent(end);
        reservation.setStatus(ReservationStatus.RESERVED);
        Optional<Reservation> result = Optional.of(reservation);
        User user = new User();
        user.setUserId(userId);
        when(reservationDAO.save(reservation)).thenReturn(result);

        assertTrue(reservationService.createReservation(start, end, user, roomId));
    }

    @Test
    void createOrderTest() {
        int userId = 1;
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(1);
        int capacity=1;
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setStartRent(start);
        reservation.setEndRent(end);
        reservation.setStatus(ReservationStatus.BOOKED);
        reservation.setCapacity(capacity);
        Optional<Reservation> result = Optional.of(reservation);
        User user = new User();
        user.setUserId(userId);
        when(reservationDAO.save(reservation)).thenReturn(result);
        assertTrue(reservationService.createOrder(start, end, user, capacity));
    }

}
