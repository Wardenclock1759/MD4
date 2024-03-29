package org.hse.lab2.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TeacherEntity;
import org.hse.lab2.entity.TimeTableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.hse.lab2.utils.Converters.dateFromString;

public class DatabaseManager {
    private DatabaseHelper db;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseManager(Context context) {
        db = Room.databaseBuilder(context,
                DatabaseHelper.class, DatabaseHelper.DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                initData(context);
                            }
                        });
                    }
                })
                .build();
    }

    public HseDao getHseDao() {
        return db.hseDao();
    }

    private void initData(Context context) {
        List<GroupEntity> groups = new ArrayList<>();
        GroupEntity group = new GroupEntity();
        group.id = 1;
        group.name = "БИ-18-1";
        groups.add(group);
        group = new GroupEntity();
        group.id = 2;
        group.name = "БИ-18-2";
        groups.add(group);
        DatabaseManager.getInstance(context).getHseDao().insertGroup(groups);

        List<TeacherEntity> teachers = new ArrayList<>();
        TeacherEntity teacher = new TeacherEntity();
        teacher.id = 1;
        teacher.fio = "Петров Петр Петрович";
        teachers.add(teacher);
        teacher = new TeacherEntity();
        teacher.id = 2;
        teacher.fio = "Петров2 Петр2 Петрович2";
        teachers.add(teacher);
        DatabaseManager.getInstance(context).getHseDao().insertTeacher(teachers);

        List<TimeTableEntity> timeTables = new ArrayList<>();
        TimeTableEntity timeTable = new TimeTableEntity();
        timeTable.id = 1;
        timeTable.cabinet = "Кабинет 102";
        timeTable.sub_group = "БИ";
        timeTable.subj_name = "Философия";
        timeTable.corp = "К1";
        timeTable.type = 0;
        timeTable.time_start = dateFromString("2021-05-28 9:40");
        timeTable.time_end = dateFromString("2021-05-28 11:00");
        timeTable.groupId = 1;
        timeTable.teacherId = 1;
        timeTables.add(timeTable);
        timeTable = new TimeTableEntity();
        timeTable.id = 2;
        timeTable.cabinet = "Кабинет 507";
        timeTable.sub_group = "БИ";
        timeTable.subj_name = "Мобильная разработка";
        timeTable.corp = "К1";
        timeTable.type = 0;
        timeTable.time_start = dateFromString("2021-05-28 13:00");
        timeTable.time_end = dateFromString("2021-05-28 15:00");
        timeTable.groupId = 1;
        timeTable.teacherId = 2;
        timeTables.add(timeTable);
        DatabaseManager.getInstance(context).getHseDao().insertTimeTable(timeTables);
    }
}
