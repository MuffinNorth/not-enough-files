package ru.muffinnorth.nef.databases;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.springframework.stereotype.Component;
import org.sqlite.SQLiteErrorCode;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.jpa.BaseJPA;
import ru.muffinnorth.nef.jpa.FileJPA;
import ru.muffinnorth.nef.jpa.PairJPA;
import ru.muffinnorth.nef.jpa.TagJPA;
import ru.muffinnorth.nef.jpa.mapper.FileMapper;
import ru.muffinnorth.nef.jpa.mapper.TagMapper;
import ru.muffinnorth.nef.models.Tag;

import java.sql.SQLException;
import java.util.*;

@Component
public class SQLiteDatabase implements Database {

    private final String CONNECTION_STRING = "jdbc:sqlite:database.db";
    private ConnectionSource connectionSource;

    private static final Class<?>[] tableClasses = new Class[]{
            FileJPA.class,
            TagJPA.class,
            PairJPA.class
    };

    private Optional<Dao<FileJPA, UUID>> fileDAO;
    private Optional<Dao<TagJPA, UUID>> tagDAO;
    private Optional<Dao<PairJPA, UUID>> pairDAO;

    public SQLiteDatabase() throws Exception {
        System.out.println("SQLite database was invoke");
        connectionSource = new JdbcConnectionSource(CONNECTION_STRING);
        preinitDatabase();
    }

    private void preinitDatabase() throws Exception {
        openDatabase();
        for (Class<?> tableClass : tableClasses) {
            TableUtils.createTableIfNotExists(connectionSource, tableClass);
        }
        closeDatabase();
    }

    private void openDatabase() throws SQLException {
        connectionSource = new JdbcConnectionSource(CONNECTION_STRING);
        fileDAO = Optional.of(DaoManager.createDao(connectionSource, FileJPA.class));
        tagDAO = Optional.of(DaoManager.createDao(connectionSource, TagJPA.class));
        pairDAO = Optional.of(DaoManager.createDao(connectionSource, PairJPA.class));
    }

    private void closeDatabase() throws Exception {
        connectionSource.close();
        fileDAO = Optional.empty();
        tagDAO = Optional.empty();
        pairDAO = Optional.empty();
    }

    @Override
    public void upload(FilesContainer container, FileTagHolder holder, HashSet<Tag> tags) throws Exception {
        openDatabase();
        container.getAllFiles().forEach(file -> {
            var jpa = FileMapper.toJPA(file);
            try {
                fileDAO.get().create(jpa);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        });

        tags.forEach(tag -> {
            var jpa = TagMapper.toJPA(tag);
            try {
                tagDAO.get().create(jpa);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            holder.getFilesByTag(tag).forEach(file -> {
                var pairJpa = new PairJPA(file.getUuid(), tag.getUuid());
                try {
                    pairDAO.get().create(pairJpa);
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            });
        });


        closeDatabase();
    }

    @Override
    public void download(FilesContainer container, FileTagHolder holder, HashSet<Tag> tags) throws Exception {
        openDatabase();
        fileDAO.get().queryForAll().forEach(fileJPA -> {
            var model = FileMapper.toModel(fileJPA);
            container.store(model);
        });

        tagDAO.get().queryForAll().forEach(tagJPA -> {
            var model = TagMapper.toModel(tagJPA);
            tags.add(model);
        });

        pairDAO.get().queryForAll().forEach(pairJPA -> {
            var file = container.getAllFiles().stream().filter(f -> f.getUuid().equals(pairJPA.getFileUUID())).findFirst();
            var tag = tags.stream().filter(t -> t.getUuid().equals(pairJPA.getTagUUID())).findFirst();
            if (file.isPresent() && tag.isPresent())
                holder.put(file.get(), tag.get());
        });

        closeDatabase();
    }
}
