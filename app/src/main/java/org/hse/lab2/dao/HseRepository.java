package org.hse.lab2.dao;

import android.content.Context;
import android.text.format.DateFormat;

import androidx.lifecycle.LiveData;

import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TeacherEntity;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.ScheduleItem;

import java.util.Date;
import java.util.List;

import static org.hse.lab2.utils.Converters.dateFromString;

public class HseRepository {
    private final HseDao dao;

    public HseRepository(Context context) {
        DatabaseManager databaseManager = DatabaseManager.getInstance(context);
        dao = databaseManager.getHseDao();
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return dao.getAllGroup();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return dao.getAllTeacher();
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByDate(Date date) {
        return dao.getTimeTableByDate(date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByGroupIdAndDate(Integer id, Date date) {
        return dao.getTimeTableByGroupIdAndDate(id, date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTeacherIdAndDate(Integer id, Date date) {
        return dao.getTimeTableByTeacherIdAndDate(id, date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTimeGroup(Date start_time, Date end_time, Integer group_id) {
        return dao.getTimeTableByTimeGroup(start_time, end_time, group_id);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTimeTeacher(Date start_time, Date end_time, Integer teacher_id) {
        return dao.getTimeTableByTimeTeacher(start_time, end_time, teacher_id);
    }
}
