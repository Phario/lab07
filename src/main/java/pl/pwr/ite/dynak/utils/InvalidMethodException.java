package pl.pwr.ite.dynak.utils;

public class InvalidMethodException extends RuntimeException {
    public InvalidMethodException() {
        super("Invalid method name");
    }
}
