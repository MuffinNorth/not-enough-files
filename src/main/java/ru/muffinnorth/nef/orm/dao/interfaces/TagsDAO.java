package ru.muffinnorth.nef.orm.dao.interfaces;

import com.j256.ormlite.dao.Dao;
import ru.muffinnorth.nef.models.Tag;
import ru.muffinnorth.nef.orm.TagJPA;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface TagsDAO extends Dao<TagJPA, UUID> {
    public Optional<TagJPA> findByTitle(String title) throws SQLException;
}
