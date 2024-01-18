package ru.muffinnorth.nef;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.muffinnorth.nef.core.FileSystemCore;
import ru.muffinnorth.nef.utils.SystemFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.awt.Desktop;

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

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        FileSystemCore core = context.getBean(FileSystemCore.class);
//        core.save();
//        core.load();

    }
}
