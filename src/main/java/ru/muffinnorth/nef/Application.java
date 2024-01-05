package ru.muffinnorth.nef;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.muffinnorth.nef.core.FileStore;
import ru.muffinnorth.nef.models.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class Application {

    public static final Properties config = new Properties();

    static {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream resourceStream = loader.getResourceAsStream("config.ini")) {
            config.load(resourceStream);
        } catch (IOException _) {
        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        FileStore fileStore = context.getBean(FileStore.class);
    }
}
