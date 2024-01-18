package ru.muffinnorth.nef.jpa;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@DatabaseTable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileJPA extends BaseJPA{
    @DatabaseField(canBeNull = false)
    private String title = "";
    @DatabaseField(canBeNull = false)
    private String description = "";
    @DatabaseField(canBeNull = false, unique = true)
    private String path = "";

    public FileJPA(UUID uuid, String title, String description, String path) {
        this(title, description, path);
        setUuid(uuid);
    }
}
