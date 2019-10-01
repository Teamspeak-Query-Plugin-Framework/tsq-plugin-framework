package net.vortexdata.tsqpf.exceptions;

public class InvalidCredentialsException extends Exception {

    @Override
    public String getMessage() {
        return "Username or password are incorrect.";
    }

}
