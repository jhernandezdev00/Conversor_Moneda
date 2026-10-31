package conversor.web;

public class BadRequestException extends RuntimeException {
    private final String code;
    private final String messageKey;

    public BadRequestException(String code, String messageKey) {
        super(code);
        this.code = code;
        this.messageKey = messageKey;
    }

    public String code() { return code; }
    public String messageKey() { return messageKey; }
}