package ru.muffinnorth.nef.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;

import java.util.Set;

@Component
@Qualifier("DBContainer")
public class DatabaseFileTagHolder implements FileTagHolder {

    private Database database;

    @Autowired
    public DatabaseFileTagHolder(Database database) {
    this.database = database;
    }

    @Override
    public void put(File file, Tag[] tags) {

    }

    @Override
    public void put(File file, Tag tag) {
        try {

            database.savePair(file.getPath(), tag.getTitle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(File file) {
        return false;
    }

    @Override
    public boolean remove(Tag tag) {
        return false;
    }

    @Override
    public boolean remove(File file, Tag tag) {
        return false;
    }

    @Override
    public Set<File> getTaggedFiles() {
        return null;
    }

    @Override
    public Set<Tag> getTagsByFile(File file) {
        return null;
    }

    @Override
    public Set<Tag> getUniqueTags() {
        return null;
    }

    @Override
    public Set<File> getFilesByTag(Tag tag) {
        return null;
    }

    @Override
    public Set<File> getFilesByTags(Tag[] tags) {
        return null;
    }
}
