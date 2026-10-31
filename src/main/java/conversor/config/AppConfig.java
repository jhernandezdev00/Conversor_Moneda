package conversor.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

public final class AppConfig {
    private static final String TEXTS = Resources.CONFIG_TEXTOS;
    private final Properties texts;

    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();


    private AppConfig(Properties texts){
        this.texts = texts;
    }

    public static AppConfig load(){
        try(InputStream in = AppConfig.class.getResourceAsStream(TEXTS)){
            if(in == null){
                throw new IllegalStateException("No se encontró " + TEXTS + " en el classpath");
            }
            Properties props = new Properties();
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            return new AppConfig(props);
        }catch (IOException e){
            throw new UncheckedIOException("No se puede leer "+TEXTS, e);
        }
    }

    public String text(String key, Object... args) {
        return MessageFormat.format(text(key), args);
    }

    public String text(String key) {
        return texts.getProperty(key, key);
    }

    public String apiKey() {
        for (String name : new String[] {"EXCHANGE_API_KEY", "api_key"}) {
            String key = System.getenv(name);
            if (key == null || key.isBlank()) {
                key = DOTENV.get(name);
            }
            if (key != null && !key.isBlank()) {
                return key.trim();
            }
        }
        return "";
    }

}
