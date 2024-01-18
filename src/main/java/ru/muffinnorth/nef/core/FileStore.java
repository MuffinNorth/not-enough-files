package ru.muffinnorth.nef.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class FileStore {


    private final FileTagHolder fileTagHolder;

    private final FilesContainer filesContainer;

    private Database database;

    private final HashSet<Tag> tagSet = new HashSet<>();


    @Autowired
    public FileStore(FileTagHolder fileTagHolder, FilesContainer filesContainer, Database database) {
        this.fileTagHolder = fileTagHolder;
        this.filesContainer = filesContainer;
        this.database = database;
    }

    public void put(File file) {
        filesContainer.store(file);
    }

    public void put(File file, String[] tags) {
        var internalFile = tryGetInternalFileOrNew(file);
        put(internalFile);
        Arrays.stream(tags).forEach(strTag -> fileTagHolder.put(internalFile, getTagEntry(strTag)));
    }

    public void put(File file, String tag) {
        put(tryGetInternalFileOrNew(file), new String[]{tag});
    }

    public Optional<File> popOptional(File file){
        if(contains(file)){
            var targetFile = filesContainer.getAllFiles().stream().filter(f -> f.getPath().equals(file.getPath())).findFirst();
            targetFile.ifPresent(this::remove);
            return targetFile;
        }   else
            return Optional.empty();
    }

    private void remove(File file){
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

    public Set<File> getFilesByTag(String tag){
        return fileTagHolder.getFilesByTag(getTagEntry(tag));
    }

    public Set<File> getFilesByTags(String[] tags){
        Tag[] innerTags = new Tag[tags.length];
        Arrays.stream(tags).map(this::getTagEntry)
                .toList()
                .toArray(innerTags);
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
        return tagSet.stream().anyMatch(tag -> tag.getTitle().equals(strTag));
    }

    public boolean contains(File file){
        return filesContainer.contains(file);
    }


    private void removeUnusedTag() {
        var copy = new HashSet<>(Set.copyOf(tagSet));
        copy.removeAll(fileTagHolder.getUniqueTags());
        if (copy.isEmpty()) return;
        copy.forEach(tagSet::remove);
    }

    private Tag getTagEntry(String strTag) {
        Tag ghostTag = new Tag(UUID.randomUUID(), strTag);
        Tag reallyTag;
        if (tagSet.contains(ghostTag)) {
            int index;
            List<Tag> tagList = tagSet.stream().toList();
            index = tagList.indexOf(ghostTag);
            reallyTag = tagList.get(index);
        } else {
            reallyTag = ghostTag;
            tagSet.add(reallyTag);
        }

        return reallyTag;
    }

    private File tryGetInternalFileOrNew(File file){
        return getFiles().stream().filter(f -> f.getPath().equals(file.getPath())).findFirst().orElse(file);
    }

    public void save() throws Exception {
        database.upload(filesContainer, fileTagHolder, tagSet);
    }

    public void load() throws Exception {
        database.download(filesContainer, fileTagHolder,tagSet);
        fixIntegrity();
    }


}
