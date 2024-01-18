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
public class PairJPA extends BaseJPA{
    @DatabaseField(canBeNull = false)
    private UUID fileUUID;
    @DatabaseField(canBeNull = false)
    private UUID tagUUID;
}
