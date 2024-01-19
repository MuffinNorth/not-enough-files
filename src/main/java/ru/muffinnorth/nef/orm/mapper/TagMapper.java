package ru.muffinnorth.nef.orm.mapper;

import ru.muffinnorth.nef.orm.TagJPA;
import ru.muffinnorth.nef.models.Tag;

public class TagMapper {
    public static Tag toModel(TagJPA target){
        return new Tag(target.getUuid(), target.getTitle(), target.getColor());
    }

    public static TagJPA toJPA(Tag target){
        return new TagJPA(target.getUuid(), target.getTitle(), target.getColor());
    }
}
