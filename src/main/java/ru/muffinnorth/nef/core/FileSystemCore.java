package ru.muffinnorth.nef.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.models.Tag;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.utils.SystemFile;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class FileSystemCore {

    private final FileStore store;

    @Autowired
    public FileSystemCore(FileStore store) {
        this.store = store;
    }

    public void watchFile(SystemFile file) throws FileNotFoundException {
        checkFileExistOrThrow(file);
        store.put(toInternalFormat(file));
    }

    public void applyTag(SystemFile file, String tag) throws FileNotFoundException {
        watchFile(file);
        store.put(toInternalFormat(file), tag);
    }

    public boolean unwatchFile(SystemFile file) {
        var optional = store.popOptional(toInternalFormat(file));
        return optional.isPresent();
    }

    public Set<SystemFile> getByTag(String tag){
        return new HashSet<>(store.getFilesByTag(tag).stream().map(file -> new SystemFile(file.getPath())).toList());
    }

    public Set<SystemFile> getByTags(String... tags){
        return new HashSet<>(store.getFilesByTags(tags).stream().map(file -> new SystemFile(file.getPath())).toList());
    }

    private void checkFileExistOrThrow(SystemFile file) throws FileNotFoundException {
        if(!file.exists())
            throw new FileNotFoundException("File not found or not exists with path " + file.getPath());
    }

    private File toInternalFormat(SystemFile file) {
        return new File(UUID.randomUUID(), file.getPath());
    }

    public Set<Tag> getFileTags(File file) {
        return null;
    }

}
