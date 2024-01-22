package ru.muffinnorth.nef.core.abstractions;

import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;
import ru.muffinnorth.nef.orm.FileJPA;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public interface Database {

    public Set<Tag> getAllTags() throws Exception;

    boolean checkContainsTag(String title) throws Exception;
    Optional<Tag> getTagByTitle(String title) throws Exception;

    boolean saveTag(Tag tag) throws Exception;

    boolean removeTag(String strTag) throws Exception;

    public Set<File> getAllFiles() throws Exception;

    boolean saveFile(File file) throws Exception;

    boolean checkConatainsFile(File file) throws Exception;

    boolean removeFile(File file) throws Exception;

    boolean savePair(String path, String strTag) throws Exception;

    boolean removeFileTag(String path) throws Exception;

    boolean removeFileTag(String path, String strTag) throws Exception;

    Set<File> getAllFilesByTag(String strTag) throws Exception;

    Set<Tag> getTagsByFile(String file) throws Exception;
}
