package ru.muffinnorth.nef.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SetFileContainerTest {


    private static final File[] files = new File[10];

    @BeforeAll
    public static void generateFiles() {
        Random random = new Random();
        for (int i = 0; i < files.length - 1; i++) {
            files[i] = new File(UUID.randomUUID(), UUID.randomUUID().toString());
        }
        files[files.length - 1] = new File(UUID.randomUUID(), files[0].getPath());
    }

    @Test
    void storeFilesArray() {
        FilesContainer container = new SetFileContainer();
        for (int i = 0; i < files.length - 1; i++) {
            container.store(files[i]);
        }
        assertEquals(container.size(), files.length - 1);

    }

    @Test
    void storeSameFilePath() {
        FilesContainer container = new SetFileContainer();
        for (int i = 0; i < files.length - 1; i++) {
            container.store(files[i]);
        }
        container.store(files[files.length - 1]);
        assertEquals(container.size(), files.length - 1);
    }

    @Test
    void storeDuplicateFile() {
        FilesContainer container = new SetFileContainer();
        for (File file : files) {
            container.store(file);
        }
        assertEquals(container.size(), files.length - 1);
    }


    @Test
    void removeOne() {
        FilesContainer container = new SetFileContainer();
        for (File file : files) {
            container.store(file);
        }
        container.remove(files[0]);
        assertEquals(container.size(), files.length - 2);
    }

    @Test
    void removeAll() {
        FilesContainer container = new SetFileContainer();
        for (File file : files) {
            container.store(file);
        }
        for (File file : files) {
            container.remove(file);
        }
        assertEquals(container.size(), 0);
    }

    @Test
    void getAllFiles() {
        FilesContainer container = new SetFileContainer();
        for (File file : files) {
            container.store(file);
        }
        var fileList = container.getAllFiles();
        var sortedFileList = fileList.stream().sorted(Comparator.comparing(File::getUuid)).toArray();
        var sortedFileArray = Arrays.stream(files).limit(files.length - 1).sorted(Comparator.comparing(File::getUuid)).toArray();
        assertArrayEquals(sortedFileList, sortedFileArray);
    }

    @Test
    void checkTrueIntegrity() {
        FilesContainer container = new SetFileContainer();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("config.ini");
        assert url != null;
        container.store(new File(UUID.randomUUID(), url.getPath()));
        assertTrue(container.checkIntegrity());
    }

    @Test
    void checkNotExistFileIntegrity(){
        FilesContainer container = new SetFileContainer();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("config.ini");
        assert url != null;
        container.store(new File(UUID.randomUUID(), url.getPath()));
        for (File file : files) {
            container.store(file);
        }
        assertFalse(container.checkIntegrity());
    }

    @Test
    void fixIntegrity() {
        FilesContainer container = new SetFileContainer();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("config.ini");
        assert url != null;
        container.store(new File(UUID.randomUUID(), url.getPath()));
        for (File file : files) {
            container.store(file);
        }
        container.fixIntegrity();
        assertEquals(container.size(), 1);
    }

    @Test
    void contains() {
        FilesContainer container = new SetFileContainer();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("config.ini");
        assert url != null;
        var classloaderFile = new File(UUID.randomUUID(), url.getPath());
        for (File file : files) {
            container.store(file);
        }
        assertTrue(container.contains(files[files.length - 1]));
        for (File file : files) {
            assertTrue(container.contains(file));
        }
        assertFalse(container.contains(classloaderFile));
        container.store(classloaderFile);
        assertTrue(container.contains(classloaderFile));
    }
}