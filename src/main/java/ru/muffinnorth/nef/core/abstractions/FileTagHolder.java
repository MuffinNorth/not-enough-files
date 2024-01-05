package ru.muffinnorth.nef.core.abstractions;

import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Tag;

import java.util.Set;

@Component
public interface FileTagHolder {
    void put(File file, Tag[] tags);

    void put(File file, Tag tag);

    boolean remove(File file);

    boolean remove(Tag tag);

    boolean remove(File file, Tag tag);

    Set<File> getTaggedFiles();

    Set<Tag> getTagsByFile(File file);

    Set<Tag> getUniqueTags();
}
