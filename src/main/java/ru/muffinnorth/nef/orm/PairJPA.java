package ru.muffinnorth.nef.orm;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;
import ru.muffinnorth.nef.orm.dao.PairDAOImpl;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@DatabaseTable(daoClass = PairDAOImpl.class)
@Data
public class PairJPA extends BaseJPA{
    public void setFileUUID(UUID fileUUID) {
        this.fileUUID = fileUUID;
        this.hash = DigestUtils.md5DigestAsHex((fileUUID.toString() + tagUUID.toString()).getBytes(StandardCharsets.UTF_8));
    }

    public void setTagUUID(UUID tagUUID) {
        this.tagUUID = tagUUID;
        this.hash = DigestUtils.md5DigestAsHex((fileUUID.toString() + tagUUID.toString()).getBytes(StandardCharsets.UTF_8));
    }

    @DatabaseField(canBeNull = false)
    private UUID fileUUID;
    @DatabaseField(canBeNull = false)
    private UUID tagUUID;

    @DatabaseField(canBeNull = true, unique = true)
    private String hash;

    public PairJPA(UUID fileUUID, UUID tagUUID, String hash) {
        this.fileUUID = fileUUID;
        this.tagUUID = tagUUID;
        this.hash = hash;
    }

    public PairJPA(UUID fileUUID, UUID tagUUID) {
        this.fileUUID = fileUUID;
        this.tagUUID = tagUUID;
        this.hash = DigestUtils.md5DigestAsHex((fileUUID.toString() + tagUUID.toString()).getBytes(StandardCharsets.UTF_8));
    }

    public PairJPA() {}
}
