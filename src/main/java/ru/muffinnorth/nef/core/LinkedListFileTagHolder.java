package ru.muffinnorth.nef.core;

import org.springframework.stereotype.Component;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.models.Pair;
import ru.muffinnorth.nef.models.Tag;

import java.util.*;

@Component
public class LinkedListFileTagHolder implements FileTagHolder {
    private final List<Pair> pairs = new LinkedList<>();

    @Override
    public void put(File file, Tag[] tags) {
        Arrays.stream(tags).map(tag -> new Pair(file, tag)).filter(pair -> !pairs.contains(pair)).forEach(pairs::add);
    }

    @Override
    public void put(File file, Tag tag) {
        put(file, new Tag[]{tag});
    }

    @Override
    public boolean remove(File file) {
        if (!getTaggedFiles().contains(file)) return false;
        var copy = List.copyOf(pairs);
        copy.stream().filter(pair -> pair.file().equals(file)).forEach(pairs::remove);
        return true;
    }

    @Override
    public boolean remove(Tag tag) {
        if (pairs.stream().noneMatch(pair -> pair.tag().equals(tag))) return false;
        var copy = List.copyOf(pairs);
        copy.stream().filter(pair -> pair.tag().equals(tag)).forEach(pairs::remove);
        return true;
    }

    @Override
    public boolean remove(File file, Tag tag) {
        return pairs.remove(new Pair(file, tag));
    }

    @Override
    public Set<File> getTaggedFiles() {
        return new LinkedHashSet<>(pairs.stream().map(Pair::file).toList());
    }

    @Override
    public Set<Tag> getTagsByFile(File file) {
        if (pairs.stream().noneMatch(pair -> pair.file().equals(file))) return Set.of();
        return new HashSet<>(pairs.stream().filter(pair -> pair.file().equals(file)).map(Pair::tag).toList());
    }

    @Override
    public Set<Tag> getUniqueTags() {
        return new HashSet<>(pairs.stream().map(Pair::tag).toList());
    }

}
