package ru.muffinnorth.nef.orm.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import ru.muffinnorth.nef.models.Pair;
import ru.muffinnorth.nef.orm.PairJPA;
import ru.muffinnorth.nef.orm.dao.interfaces.PairDAO;

import java.sql.SQLException;
import java.util.UUID;

public class PairDAOImpl extends BaseDaoImpl<PairJPA, UUID> implements PairDAO {

    public PairDAOImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, PairJPA.class);
    }
}
