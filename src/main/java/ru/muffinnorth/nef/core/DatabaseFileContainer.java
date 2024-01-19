package ru.muffinnorth.nef.core;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.utils.SystemFile;

import java.util.Set;


@Component
@Qualifier("DBContainer")
public class DatabaseFileContainer implements FilesContainer {


    Database database;


    @Autowired
    public DatabaseFileContainer(Database database) {
        this.database = database;
        fixIntegrity();
    }

    @Override
    public void store(File file) {
        try {
            database.saveFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(File file) {
        try {
            database.removeFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<File> getAllFiles() {
        try {
            return database.getAllFiles();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkIntegrity() {
        try {
            return database.getAllFiles().stream().allMatch(file -> new SystemFile(file.getPath()).exists());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    @Override
    public void fixIntegrity() {
        database.getAllFiles().forEach(file -> {
            var systemFile = new SystemFile(file.getPath());
            if(!systemFile.exists()) {
                try {
                    database.removeFile(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public boolean contains(File file) {
        try {
            return database.checkConatainsFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int size() {
        try {
            return database.getAllFiles().size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
