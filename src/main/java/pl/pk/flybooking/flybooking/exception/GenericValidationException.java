package pl.pk.flybooking.flybooking.exception;

public class GenericValidationException extends RuntimeException {

    private String[] args;

    public GenericValidationException(String messageCode) {
        super(messageCode);
    }

    public GenericValidationException(String messageCode, String... args) {
        super(messageCode);
        this.args = args;
    }

    public String[] getArgs() {
        return args;
    }
}