package Constants;

public enum Error {

    INVALID_MESSAGE("Please enter valid event"),

    INVALID_STOCK_NAME("Please enter valid stock name");

    private String message;

    Error (String message) {
        this.message = message;
    }

    public String getMessage ( ) {
        return message;
    }

    public static boolean contains(String value) {

        for (Error c : Error.values()) {
            if (c.getMessage().equals(value)) {
                return true;
            }
        }

        return false;
    }


}
