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

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        FileSystemCore core = context.getBean(FileSystemCore.class);
        String[] files = new String[]{
                "D:\\Старые файлы\\Images\\8pw2D8ilFV8.jpg",
                "D:\\Старые файлы\\Images\\irvXOWY2OTg.jpg",
                "D:\\Старые файлы\\Images\\J_PYIkQ1OjA.jpg",
                "D:\\Старые файлы\\Images\\AOCEUzE8Lh8.jpg",
                "D:\\Старые файлы\\Images\\Tumblr_l_1556293459436683.jpg",
        };

        List<SystemFile> flist = new LinkedList<>();
        Arrays.stream(files).forEach(f -> flist.add(new SystemFile(f)));
        flist.forEach(file -> {
            try {
                core.applyTag(file, "Image");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        core.applyTag(flist.get(0), "Furry");
        var f = core.getByTags("Image", "Furry");

        if (!Desktop.isDesktopSupported())//check if Desktop is supported by Platform or not
        {
            System.out.println("not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        f.stream().findFirst().ifPresent(file -> {
            try {
                desktop.open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
