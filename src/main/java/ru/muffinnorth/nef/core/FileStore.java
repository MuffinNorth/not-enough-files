package ru.muffinnorth.nef.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;

import java.util.*;

@Component
public class FileStore {

    private final FileTagHolder fileTagHolder;

    private final FilesContainer filesContainer;

    private Database database;

    private final HashSet<Tag> tagSet = new HashSet<>();


    @Autowired
    public FileStore(@Qualifier("DBContainer") FileTagHolder fileTagHolder, @Qualifier("DBContainer") FilesContainer filesContainer, Database database) {
        this.fileTagHolder = fileTagHolder;
        this.filesContainer = filesContainer;
        this.database = database;
    }

    public File put(File file) {
        var internalFile = tryGetInternalFileOrNew(file);
        filesContainer.store(internalFile);
        return internalFile;
    }

    public void put(File file, String[] tags) {
        var internalFile = put(file);
        Arrays.stream(tags).forEach(strTag -> fileTagHolder.put(internalFile, getTagEntry(strTag)));
    }

    public void put(File file, String tag) {
        put(file, new String[]{tag});
    }

    public Optional<File> popOptional(File file) {
        if (contains(file)) {
            var targetFile = filesContainer.getAllFiles().stream().filter(f -> f.getPath().equals(file.getPath())).findFirst();
            targetFile.ifPresent(this::remove);
            return targetFile;
        } else return Optional.empty();
    }

    private void remove(File file) {
        filesContainer.remove(file);
        fileTagHolder.remove(file);
    }

    public boolean isUntagged(File file) {
        return fileTagHolder.getTagsByFile(file).isEmpty();
    }

    public Set<File> getFiles() {
        return filesContainer.getAllFiles();
    }

    public Set<Tag> getFileTag(File file) {
        return fileTagHolder.getTagsByFile(file);
    }

    public Set<File> getFilesByTag(String tag) {
        return fileTagHolder.getFilesByTag(getTagEntry(tag));
    }

    public Set<File> getFilesByTags(String[] tags) {
        Tag[] innerTags = new Tag[tags.length];
        Arrays.stream(tags).map(this::getTagEntry).toList().toArray(innerTags);
        return fileTagHolder.getFilesByTags(innerTags);
    }

    public Set<File> getUntaggedFile() {
        var taggedFiles = fileTagHolder.getTaggedFiles();
        var allFiles = filesContainer.getAllFiles();
        var untagged = new HashSet<>(allFiles);
        untagged.removeAll(taggedFiles);
        return untagged;
    }

    public boolean removeTag(File file, String strTag) {
        if (!filesContainer.contains(file)) return false;
        if (!containsTag(strTag)) return false;
        var result = fileTagHolder.remove(file, getTagEntry(strTag));
        removeUnusedTag();
        return result;
    }

    public boolean removeAllEntriesTag(String strTag) {
        if (!containsTag(strTag)) return false;
        fileTagHolder.remove(getTagEntry(strTag));
        return true;
    }

    public boolean checkIntegrity() {
        return filesContainer.checkIntegrity();
    }

    public void fixIntegrity() {
        if (checkIntegrity()) return;
        filesContainer.fixIntegrity();
        /*var files = filesContainer.getAllFiles();
        files.removeAll(fileTagHolder.getTaggedFiles());
        files.forEach(fileTagHolder::remove);
        removeUnusedTag();*/
    }

    public boolean containsTag(String strTag) {
        try {
            return database.checkContainsTag(strTag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(File file) {
        return filesContainer.contains(file);
    }


    private void removeUnusedTag() {
        try {
            Set<Tag> copy = database.getAllTags();
            copy.removeAll(fileTagHolder.getUniqueTags());
            if (copy.isEmpty()) return;
            copy.forEach(tag -> {
                try {
                    database.removeTag(tag.getTitle());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Tag getTagEntry(String strTag) {
        try {
            var tag = database.getTagByTitle(strTag);
            return tag.orElseGet(() -> {
                var newTag = new Tag(UUID.randomUUID(), strTag);
                try {
                    database.saveTag(newTag);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return newTag;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private File tryGetInternalFileOrNew(File file) {
        return getFiles().stream().filter(f -> f.getPath().equals(file.getPath())).findFirst().orElse(file);
    }
}
