package ru.muffinnorth.nef.orm.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.muffinnorth.nef.orm.TagJPA;
import ru.muffinnorth.nef.orm.dao.interfaces.TagsDAO;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class TagsDaoImpl extends BaseDaoImpl<TagJPA, UUID> implements TagsDAO {

    public TagsDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, TagJPA.class);
    }

    @Override
    public Optional<TagJPA> findByTitle(String title) throws SQLException {
        return super.queryForEq("title", title).stream().findAny();
    }
}
