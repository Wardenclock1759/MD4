package org.hse.lab2.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TeacherEntity;
import org.hse.lab2.entity.TimeTable;
import org.hse.lab2.utils.Converters;

@Database(entities = {GroupEntity.class, TeacherEntity.class, TimeTable.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class DatabaseHelper extends RoomDatabase {
    public static final String DATABASE_NAME = "hse_timetable";

    public abstract HseDao hseDao();
}
