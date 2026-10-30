package conversor.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ExchangeRateResponse {

    @SerializedName("result")
    private String result;

    @SerializedName("error-type")
    private String errorType;

    @SerializedName("documentation")
    private String documentation;

    @SerializedName("terms_of_use")
    private String termsOfUse;

    @SerializedName("time_last_update_unix")
    private long timeLastUpdateUnix;

    @SerializedName("time_last_update_utc")
    private String timeLastUpdateUtc;

    @SerializedName("time_next_update_unix")
    private long timeNextUpdateUnix;

    @SerializedName("time_next_update_utc")
    private String timeNextUpdateUtc;

    @SerializedName("base_code")
    private String baseCode;

    @SerializedName("conversion_rates")
    private Map<String, Double> conversionRates;

    public boolean isSuccess() {
        return "success".equals(result);
    }

    public Double getRate(String targetCode) {
        return conversionRates == null ? null : conversionRates.get(targetCode);
    }

    public String getResult() { return result; }
    public String getErrorType() { return errorType; }
    public String getDocumentation() { return documentation; }
    public String getTermsOfUse() { return termsOfUse; }
    public long getTimeLastUpdateUnix() { return timeLastUpdateUnix; }
    public String getTimeLastUpdateUtc() { return timeLastUpdateUtc; }
    public long getTimeNextUpdateUnix() { return timeNextUpdateUnix; }
    public String getTimeNextUpdateUtc() { return timeNextUpdateUtc; }
    public String getBaseCode() { return baseCode; }
    public Map<String, Double> getConversionRates() { return conversionRates; }
}