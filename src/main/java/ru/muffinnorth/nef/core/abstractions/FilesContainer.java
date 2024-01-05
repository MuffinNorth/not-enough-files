package ru.muffinnorth.nef.core.abstractions;

import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.models.File;

import java.util.Set;

@Component
public interface FilesContainer {
    void store(File file);

    void remove(File file);

    Set<File> getAllFiles();

    boolean checkIntegrity();

    void fixIntegrity();

    boolean contains(File file);
}
