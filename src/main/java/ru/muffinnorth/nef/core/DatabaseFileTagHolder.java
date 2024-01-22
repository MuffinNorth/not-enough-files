package ru.muffinnorth.nef.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
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
        for (Tag tag : tags) {
            put(file, tag);
        }
    }

    @Override
    public void put(File file, Tag tag) {
        try {
            database.savePair(file.getPath(), tag.getTitle());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean remove(File file) {
        try {
            return database.removeFile(file);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean remove(Tag tag) {
        try {
            return database.removeTag(tag.getTitle());
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public boolean remove(File file, Tag tag) {
        try {
            return database.removeFileTag(file.getPath(), tag.getTitle());
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Set<File> getTaggedFiles() {
        return null;
    }

    @Override
    public Set<Tag> getTagsByFile(File file) {
        try {
            return database.getTagsByFile(file.getPath());
        } catch (Exception e) {
            System.out.println(e);
            return Set.of();
        }
    }

    @Override
    public Set<Tag> getUniqueTags() {
        return null;
    }

    @Override
    public Set<File> getFilesByTag(Tag tag) {
        try {
            return database.getAllFilesByTag(tag.getTitle());
        } catch (Exception e) {
            System.out.println(e);
            return Set.of();
        }
    }

    @Override
    public Set<File> getFilesByTags(Tag[] tags) {
        try {
            var sets = new LinkedList<Set<File>>();
            var out = new HashSet<File>(database.getAllFilesByTag(tags[0].getTitle()));
            for (Tag tag : tags) {
                out.retainAll(database.getAllFilesByTag(tag.getTitle()));
            }
            return out;
        } catch (Exception e) {
            System.out.println(e);
            return Set.of();
        }
    }
}
