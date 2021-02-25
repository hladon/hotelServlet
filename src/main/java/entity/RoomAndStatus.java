package entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoomAndStatus implements Serializable {
    private int roomId;

    private String roomNameEn;

    private String roomNameUa;

    private int price;

    private int capacity;

    private Category category;

    private ReservationStatus status;
}
