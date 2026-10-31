package conversor.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import conversor.model.ExchangeRateResponse;
import conversor.model.ExchangeRates;
import conversor.service.ExchangeRateException.Reason;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ExchangeRateClient {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    // Se crea una sola vez y se reutiliza; antes se creaba uno nuevo en cada petición
    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final Gson gson = new Gson();
    private final String apiKey;

    public ExchangeRateClient(String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey;
    }

    public ExchangeRates fetchLatest(String baseCode) throws ExchangeRateException {
        if (apiKey.isBlank()) {
            throw new ExchangeRateException(Reason.MISSING_KEY, null);
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE_URL + apiKey + "/latest/" + baseCode))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new ExchangeRateException(Reason.NETWORK, e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ExchangeRateException(Reason.NETWORK, "Petición interrumpida", e);
        }

        ExchangeRateResponse body;
        try {
            body = gson.fromJson(response.body(), ExchangeRateResponse.class);
        } catch (JsonParseException e) {
            throw new ExchangeRateException(Reason.UNKNOWN, "HTTP " + response.statusCode(), e);
        }
        if (body == null) {
            throw new ExchangeRateException(Reason.UNKNOWN, "HTTP " + response.statusCode());
        }
        if (body.isSuccess() && body.getConversionRates() != null) {
            return body.toExchangeRates();
        }

        String errorType = body.getErrorType() != null ? body.getErrorType() : "HTTP " + response.statusCode();
        Reason reason = switch (errorType) {
            case "invalid-key" -> Reason.INVALID_KEY;
            case "quota-reached" -> Reason.QUOTA_REACHED;
            case "inactive-account" -> Reason.INACTIVE_ACCOUNT;
            case "unsupported-code" -> Reason.UNSUPPORTED_CODE;
            default -> Reason.UNKNOWN;
        };
        throw new ExchangeRateException(reason, errorType);
    }
}