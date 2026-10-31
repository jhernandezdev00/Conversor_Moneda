package conversor.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public final class Resources {
    public static final String DIVISAS_JSON = "/data/Divisas.json";

    private Resources() {
    }

    public static Reader lector(String ruta) {
        return new InputStreamReader(stream(ruta), StandardCharsets.UTF_8);
    }

    public static InputStream stream(String ruta) {
        InputStream is = Resources.class.getResourceAsStream(ruta);
        if (is == null) {
            throw new IllegalStateException("No se encontró el recurso: " + ruta);
        }
        return is;
    }
}