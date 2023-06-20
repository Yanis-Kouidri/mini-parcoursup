package model;

/**
 * @author Yanis Kouidri
 * @author Cédric Abdelbaki
 */
public class MissingSchoolException extends RuntimeException {

    public MissingSchoolException(String message) {
        super(message);
    }
}
