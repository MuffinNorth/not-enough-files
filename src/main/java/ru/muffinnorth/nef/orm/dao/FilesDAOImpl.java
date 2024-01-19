package ru.muffinnorth.nef.orm.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.muffinnorth.nef.orm.BaseJPA;
import ru.muffinnorth.nef.orm.FileJPA;
import ru.muffinnorth.nef.orm.dao.interfaces.FilesDAO;

import java.sql.SQLException;
import java.util.UUID;

public class FilesDAOImpl extends BaseDaoImpl<FileJPA, UUID> implements FilesDAO {
    public FilesDAOImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, FileJPA.class);
    }
}
