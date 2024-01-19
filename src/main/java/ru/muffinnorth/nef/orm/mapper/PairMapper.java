package ru.muffinnorth.nef.orm.mapper;

import ru.muffinnorth.nef.orm.FileJPA;
import ru.muffinnorth.nef.orm.PairJPA;
import ru.muffinnorth.nef.orm.TagJPA;
import ru.muffinnorth.nef.models.Pair;

public class PairMapper {
    public static Pair toModel(FileJPA fileJPA, TagJPA tagJPA){
        return new Pair(FileMapper.toModel(fileJPA), TagMapper.toModel(tagJPA));
    }

    public static PairJPA toJPA(Pair pair){
        return new PairJPA(pair.file().getUuid(), pair.tag().getUuid());
    }
}
