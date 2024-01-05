package ru.muffinnorth.nef.core;

import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;


import java.util.HashSet;
import java.util.Set;

@Component
public class SetFileContainer implements FilesContainer {

    private final Set<File> files = new HashSet<>();

    public SetFileContainer() {
    }


    @Override
    public void store(File file) {
        files.add(file);
    }

    @Override
    public void remove(File file) {
        files.remove(file);
    }

    @Override
    public Set<File> getAllFiles() {
        return Set.copyOf(files);
    }

    @Override
    public boolean checkIntegrity() {
        return files.stream().map(file -> new java.io.File(file.getPath())).allMatch(java.io.File::exists);
    }

    @Override
    public void fixIntegrity() {
        var copy = Set.copyOf(files);
        copy.stream().filter(file -> !(new java.io.File(file.getPath()).exists())).forEach(this::remove);
    }

    @Override
    public boolean contains(File file) {
        return files.contains(file);
    }
}
