package AppointmentManagementSystem.exceptions;
/**
This class is for the exception that happens if the daily user login fails
@author Ohzecodes
@version 1.0  */
public class InvalidCredentialException extends Throwable {

    public  InvalidCredentialException(String message) {
        super(message);
    }
    public  InvalidCredentialException() {
        super("You have got invalid Credentials");
    }
}
