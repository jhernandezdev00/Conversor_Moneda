package conversor.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Map;

public class CurrencyCatalog {

    @SerializedName("Country")
    private Map<String, String> currencies;

    public String getCode(String currencyName) {
        for (Map.Entry<String, String> entry : currencies.entrySet()) {
            if (entry.getValue().equals(currencyName)) {
                return entry.getKey();
            }
        }
        return "ERROR";
    }

    public Collection<String> getNames() {
        return currencies.values();
    }
}