package ru.muffinnorth.nef.orm.mapper;

import ru.muffinnorth.nef.orm.FileJPA;
import ru.muffinnorth.nef.models.File;

public class FileMapper {
    public static File toModel(FileJPA target){
        return new File(target.getUuid(), target.getTitle(), target.getDescription(), target.getPath());
    }

    public static FileJPA toJPA(File target){
        return new FileJPA(target.getUuid(), target.getTitle(), target.getDescription(), target.getPath());
    }

}
