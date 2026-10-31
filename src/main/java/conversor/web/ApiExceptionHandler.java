package conversor.web;

import conversor.service.ExchangeRateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Locale;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageSource messages;

    public ApiExceptionHandler(MessageSource messages) {
        this.messages = messages;
    }

    @ExceptionHandler(ExchangeRateException.class)
    public ProblemDetail handleExchangeRate(ExchangeRateException ex, Locale locale) {
        HttpStatus status = switch (ex.reason()) {
            case UNSUPPORTED_CODE -> HttpStatus.BAD_REQUEST;
            case NETWORK -> HttpStatus.BAD_GATEWAY;
            case QUOTA_REACHED, MISSING_KEY, INVALID_KEY, INACTIVE_ACCOUNT, UNKNOWN ->
                    HttpStatus.SERVICE_UNAVAILABLE;
        };
        String key = switch (ex.reason()) {
            case UNSUPPORTED_CODE -> "error.unsupported";
            case NETWORK -> "error.network";
            case QUOTA_REACHED -> "error.quota";
            case MISSING_KEY, INVALID_KEY, INACTIVE_ACCOUNT, UNKNOWN -> "error.unavailable";
        };
        if (status.is5xxServerError()) {
            log.error("No se pudieron obtener las tasas: {}", ex.getMessage(), ex);
        }
        return problem(status, ex.reason().name(), key, locale, ex.detail());
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException ex, Locale locale) {
        return problem(HttpStatus.BAD_REQUEST, ex.code(), ex.messageKey(), locale);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    public ProblemDetail handleInvalidParameters(Exception ex, Locale locale) {
        return problem(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "error.parameters", locale);
    }

    private ProblemDetail problem(HttpStatus status, String code, String key, Locale locale, Object... args) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, messages.getMessage(key, args, locale));
        problem.setProperty("code", code);
        return problem;
    }
}