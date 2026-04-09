package medicalERSystem;

import java.util.InputMismatchException;

public class InvalidInputException extends InputMismatchException {
    public InvalidInputException(String message) {
        super(message);
    }
}