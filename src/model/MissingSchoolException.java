package model;

public class MissingSchoolException extends RuntimeException {
    public MissingSchoolException() {
    }

    public MissingSchoolException(String message) {
        super(message);
    }
}
