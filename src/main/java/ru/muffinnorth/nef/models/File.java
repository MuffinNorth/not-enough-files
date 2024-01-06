package ru.muffinnorth.nef.models;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class File {
    private final UUID uuid;
    private String title;
    private String description;
    private final String path;

    public File(UUID uuid, String title, String description, String path) {
        this(uuid, path);
        this.title = title;
        this.description = description;
    }

    public File(UUID uuid, String path) {
        this.uuid = uuid;
        this.path = path;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return Objects.equals(path, file.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return "File{" + "uuid=" + uuid + ", title='" + title + '\'' + ", description='" + description + '\'' + ", path='" + path + '\'' + '}';
    }
}
