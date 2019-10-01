package net.vortexdata.tsqpf.exceptions;

public class UserAlreadyExistingException extends Exception {

    @Override
    public String getMessage() {
        return "User with same username already exists.";
    }

}
