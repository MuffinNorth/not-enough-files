package ru.muffinnorth.nef.orm;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

import java.util.UUID;

@Data
public class BaseJPA {
    @DatabaseField(id = true)
    protected UUID uuid = UUID.randomUUID();
}
