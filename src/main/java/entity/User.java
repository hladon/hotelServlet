package entity;

import lombok.Data;


@Data
public class User {

    private int userId;

    private String userName;

    private String password;

    private boolean active;

    private Role role;

}
