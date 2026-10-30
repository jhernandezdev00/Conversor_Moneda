package conversor.config;

import javax.swing.ImageIcon;
import java.awt.Font;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class Resources {
    public static final String CONFIG_TEXTOS   = "/apptextos.properties";
    public static final String DIVISAS_JSON    = "/data/Divisas.json";
    public static final String ICONO_APP       = "/images/exchange.png";
    public static final String IMAGEN_INICIO   = "/images/exchange_1.jpg";
    public static final String FUENTE_RALEWAY  = "/fonts/Raleway/static/Raleway-Thin.ttf";

    private Resources() {
    }

    public static ImageIcon imagen(String ruta) {
        return new ImageIcon(url(ruta));
    }

    public static Font fuente(String ruta, float tamano) {
        try (InputStream is = stream(ruta)) {
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(tamano);
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar la fuente: " + ruta, e);
        }
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

    private static URL url(String ruta) {
        URL url = Resources.class.getResource(ruta);
        if (url == null) {
            throw new IllegalStateException("No se encontró el recurso: " + ruta);
        }
        return url;
    }
}