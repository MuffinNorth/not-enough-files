package ru.muffinnorth.nef.core.abstractions;

import ru.muffinnorth.nef.models.Tag;

import java.sql.SQLException;
import java.util.HashSet;

public interface Database {
    void upload(FilesContainer container, FileTagHolder holder, HashSet<Tag> tags) throws Exception;
    void download(FilesContainer container, FileTagHolder holder, HashSet<Tag> tags) throws Exception;
}
