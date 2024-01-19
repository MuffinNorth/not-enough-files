package ru.muffinnorth.nef.orm.dao.interfaces;

import com.j256.ormlite.dao.Dao;
import ru.muffinnorth.nef.orm.FileJPA;
import ru.muffinnorth.nef.orm.TagJPA;

import java.util.UUID;

public interface FilesDAO extends Dao<FileJPA, UUID> {
}
