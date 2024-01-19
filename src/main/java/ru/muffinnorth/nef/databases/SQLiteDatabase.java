package ru.muffinnorth.nef.databases;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import ru.muffinnorth.nef.core.abstractions.Database;
import ru.muffinnorth.nef.core.abstractions.FileTagHolder;
import ru.muffinnorth.nef.core.abstractions.FilesContainer;
import ru.muffinnorth.nef.models.File;
import ru.muffinnorth.nef.orm.FileJPA;
import ru.muffinnorth.nef.orm.PairJPA;
import ru.muffinnorth.nef.orm.TagJPA;
import ru.muffinnorth.nef.orm.dao.interfaces.FilesDAO;
import ru.muffinnorth.nef.orm.dao.interfaces.PairDAO;
import ru.muffinnorth.nef.orm.dao.interfaces.TagsDAO;
import ru.muffinnorth.nef.orm.mapper.FileMapper;
import ru.muffinnorth.nef.orm.mapper.TagMapper;
import ru.muffinnorth.nef.models.Tag;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@Component
public class SQLiteDatabase implements Database {

    private final String CONNECTION_STRING = "jdbc:sqlite:database.db";
    private ConnectionSource connectionSource;

    private static final Class<?>[] tableClasses = new Class[]{FileJPA.class, TagJPA.class, PairJPA.class};

    private Optional<FilesDAO> fileDAO;
    private Optional<TagsDAO> tagDAO;
    private Optional<PairDAO> pairDAO;


    private PairDAO pairDAO() {
        if (pairDAO.isEmpty())
            throw new RuntimeException();
        return pairDAO.get();
    }

    private TagsDAO tagDAO() {
        if (tagDAO.isEmpty())
            throw new RuntimeException();
        return tagDAO.get();
    }

    private FilesDAO fileDAO() {
        if (fileDAO.isEmpty())
            throw new RuntimeException();
        return fileDAO.get();
    }

    private void checkDao() {
        if (tagDAO.isEmpty() || fileDAO.isEmpty() || pairDAO.isEmpty()) throw new RuntimeException("Dao not ready.");
    }

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
    public Set<Tag> getAllTags() throws Exception {
        openDatabase();
        var tags = new HashSet<>(tagDAO.get().queryForAll().stream().map(TagMapper::toModel).toList());
        closeDatabase();
        return tags;
    }

    @Override
    public boolean checkContainsTag(String title) throws Exception {
        openDatabase();
        var result = tagDAO().findByTitle(title).isPresent();
        closeDatabase();
        return result;
    }


    @Override
    public Optional<Tag> getTagByTitle(String title) throws Exception {
        openDatabase();
        var result = tagDAO().findByTitle(title).map(TagMapper::toModel).stream().findFirst();
        closeDatabase();
        return result;
    }

    @Override
    public boolean saveTag(Tag tag) throws Exception {
        openDatabase();
        if (!tagDAO().queryForEq("title", tag.getTitle()).isEmpty())
            return false;
        var result = tagDAO().create(TagMapper.toJPA(tag)) == 1;
        closeDatabase();
        return result;
    }

    @Override
    public boolean removeTag(String strTag) throws Exception {
        openDatabase();
        var tag = tagDAO().findByTitle(strTag);
        boolean result = false;
        if (tag.isPresent())
            result = tagDAO().deleteById(tag.get().getUuid()) == 1;
        closeDatabase();
        return result;
    }

    @Override
    public Set<File> getAllFiles() throws Exception {
        openDatabase();
        var result = new HashSet<>(fileDAO().queryForAll().stream().map(fileJPA -> FileMapper.toModel(fileJPA)).toList());
        closeDatabase();
        return result;
    }

    @Override
    public boolean saveFile(File file) throws Exception {
        openDatabase();
        if (!fileDAO().queryForEq("path", file.getPath()).isEmpty())
            return false;
        var jpa = FileMapper.toJPA(file);
        var result = fileDAO().create(jpa);
        closeDatabase();
        return result == 1;
    }

    @Override
    public boolean checkConatainsFile(File file) throws Exception {
        openDatabase();
        var result = Optional.ofNullable(fileDAO().queryForId(file.getUuid()));
        closeDatabase();
        return result.isPresent();
    }

    @Override
    public boolean removeFile(File file) throws Exception {
        openDatabase();
        var result = 0;
        var jpa = fileDAO().queryForEq("path", file.getPath()).stream().findFirst();
        if (jpa.isPresent())
            result = fileDAO().delete(jpa.get());
        closeDatabase();
        return result == 1;
    }

    @Override
    public boolean savePair(String path, String strTag) throws Exception {
        openDatabase();
        var file = fileDAO().queryForEq("path", path).stream().findFirst();
        var tag = tagDAO().queryForEq("title", strTag).stream().findFirst();

        var result = 0;
        if (file.isPresent() && tag.isPresent()) {
            var hashString = DigestUtils.md5DigestAsHex((file.get().getUuid().toString() + tag.get().getUuid().toString()).getBytes(StandardCharsets.UTF_8));
            if(!pairDAO().queryForEq("hash", hashString).isEmpty())
                return false;
            var pair = new PairJPA(file.get().getUuid(), tag.get().getUuid());
            result = pairDAO().create(pair);
        } else {
            FileJPA fileJpa;
            TagJPA tagJpa;
            if (file.isEmpty()) {
                fileJpa = new FileJPA("", "", path);
                fileDAO().create(fileJpa);
            } else
                fileJpa = file.get();
            if (tag.isEmpty()) {
                tagJpa = new TagJPA(strTag, 0);
                tagDAO().create(tagJpa);
            } else
                tagJpa = tag.get();
            var pair = new PairJPA(fileJpa.getUuid(), tagJpa.getUuid());
            result = pairDAO().create(pair);
        }
        closeDatabase();
        return result == 1;
    }

}
