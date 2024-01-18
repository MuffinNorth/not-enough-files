package ru.muffinnorth.nef.models;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Tag {
    private final UUID uuid;
    private final String title;
    private int color;

    public Tag(UUID uuid, String title) {
        this.uuid = uuid;
        this.title = title;
    }

    public Tag(UUID uuid, String title, Integer color) {
        this(uuid, title);
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(title, tag.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return "Tag{" + "uuid=" + uuid + ", title='" + title + '\'' + ", color=" + color + '}';
    }
}
