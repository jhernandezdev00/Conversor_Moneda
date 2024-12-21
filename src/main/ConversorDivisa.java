package main;

import java.util.Map;

public class ConversorDivisa {
    private Double Conversion_ratEsp;
    private String result;
    private String documentation;
    private String terms_of_use;
    private String time_last_update_unix;
    private String time_last_update_utc;
    private String time_next_update_unix;
    private String time_next_update_utc;
    private String base_code;
    private Map<String,Double> conversion_rates;

    public Map<String, Double> getConversion_rates() {
        return conversion_rates;
    }

    public Double getConversion_ratEsp(String divisa_Dest){
        Conversion_ratEsp = conversion_rates.get(divisa_Dest);
        return Conversion_ratEsp;
    }

    public String getBase_code() {
        return base_code;
    }

    public String getTime_next_update_utc() {
        return time_next_update_utc;
    }

    public String getTime_next_update_unix() {
        return time_next_update_unix;
    }

    public String getTime_last_update_utc() {
        return time_last_update_utc;
    }

    public String getTime_last_update_unix() {
        return time_last_update_unix;
    }

    public String getTerms_of_use() {
        return terms_of_use;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getResult() {
        return result;
    }
}

