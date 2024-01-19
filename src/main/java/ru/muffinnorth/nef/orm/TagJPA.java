package ru.muffinnorth.nef.orm;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.muffinnorth.nef.orm.dao.TagsDaoImpl;

import java.util.UUID;

@DatabaseTable(daoClass = TagsDaoImpl.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagJPA extends BaseJPA{
    @DatabaseField(canBeNull = false, unique = true)
    private String title = "";
    @DatabaseField(canBeNull = false)
    private int color;

    public TagJPA(UUID uuid, String title, int color) {
        this(title, color);
        setUuid(uuid);
    }
}
