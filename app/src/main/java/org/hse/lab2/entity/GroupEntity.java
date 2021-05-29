package org.hse.lab2.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "group", indices = {@Index(value = {"name"}, unique = true)})
public class GroupEntity {
    @PrimaryKey
    public Integer id;

    @ColumnInfo(name = "name")
    @NonNull
    public String name = "";
}



