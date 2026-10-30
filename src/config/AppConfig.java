package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class AppConfig {
    private static final String TEXTS = "/resources/apptextos.properties";
    private final Properties texts;

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

    public String text(String key){
        return texts.getProperty(key,key);
    }

}
