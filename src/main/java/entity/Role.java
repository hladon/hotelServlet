package entity;


public enum Role {
    USER("USER"),
    ADMIN("ADMIN");
    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getRole() {
        return value;
    }
}
