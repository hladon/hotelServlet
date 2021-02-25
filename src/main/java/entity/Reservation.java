package entity;

import lombok.Data;

import java.time.LocalDate;


@Data
public class Reservation {

    private int reservationId;
    private int userId;
    private int roomId;
    private LocalDate startRent;
    private LocalDate endRent;
    private int capacity;
    private ReservationStatus status;

}
