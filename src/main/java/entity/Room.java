package entity;

import lombok.Data;


@Data
public class Room {

    private int roomId;

    private String roomNameEn;

    private String roomNameUa;

    private int price;

    private int capacity;

    private Category category;
}
