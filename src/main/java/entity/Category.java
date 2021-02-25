package entity;

public enum Category {
    DELUXE("DELUXE"), STANDARD("STANDARD"), SUITE("SUITE");

    private String value;

    public String value() {
        return value;
    }

    Category(String value) {
        this.value = value;
    }
}
