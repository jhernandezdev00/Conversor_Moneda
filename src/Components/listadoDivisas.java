package Components;

import java.util.Collection;
import java.util.Map;

public class listadoDivisas {
    Map<String, String> Country;

    public String getCodCountry(String nombreCountry){
        String codCountry = "";
        for (Map.Entry<String, String> Country : Country.entrySet()) {
            if (Country.getValue().equals(nombreCountry)) {
                codCountry = Country.getKey();
                break;
            }else{
                codCountry="ERROR";
            }
        }
        return codCountry;
    }

    public Collection<String> getCountryList(){
        return Country.values();
    }

}
