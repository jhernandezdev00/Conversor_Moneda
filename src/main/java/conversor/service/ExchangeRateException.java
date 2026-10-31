package conversor.service;

public class ExchangeRateException extends Exception {

    public enum Reason {
        MISSING_KEY, NETWORK, INVALID_KEY, QUOTA_REACHED,
        INACTIVE_ACCOUNT, UNSUPPORTED_CODE, UNKNOWN
    }

    private final Reason reason;
    private final String detail;

    public ExchangeRateException(Reason reason, String detail) {
        this(reason, detail, null);
    }

    public ExchangeRateException(Reason reason, String detail, Throwable cause) {
        super(reason + (detail == null ? "" : ": " + detail), cause);
        this.reason = reason;
        this.detail = detail == null ? "" : detail;
    }

    public Reason reason() { return reason; }
    public String detail() { return detail; }
}