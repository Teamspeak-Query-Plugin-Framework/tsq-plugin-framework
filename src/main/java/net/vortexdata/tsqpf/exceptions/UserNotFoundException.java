package net.vortexdata.tsqpf.exceptions;

public class UserNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "User does not exist.";
    }

}
