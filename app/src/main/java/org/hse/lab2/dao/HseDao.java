package org.hse.lab2.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TeacherEntity;
import org.hse.lab2.entity.TimeTable;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.ScheduleItem;

import java.util.Date;
import java.util.List;

@Dao
public interface HseDao {
    @Query("SELECT * FROM `group`")
    LiveData<List<GroupEntity>> getAllGroup();
    @Insert
    void insertGroup(List<GroupEntity> data);

    @Query("SELECT * FROM teacher")
    LiveData<List<TeacherEntity>> getAllTeacher();
    @Insert
    void insertTeacher(List<TeacherEntity> data);

    @Transaction
    @Query("SELECT * FROM time_table")
    LiveData<List<TimeTableEntity>> getTimeTable();

    @Transaction
    @Query("SELECT * FROM time_table WHERE time_start <= :current_time AND time_end >= :current_time")
    LiveData<List<TimeTableEntity>> getTimeTableByDate(Date current_time);
    @Transaction
    @Query("SELECT * FROM time_table WHERE group_id = :group_id AND time_start <= :current_time AND time_end >= :current_time")
    LiveData<List<TimeTableEntity>> getTimeTableByGroupIdAndDate(Integer group_id, Date current_time);

    @Transaction
    @Query("SELECT * FROM time_table WHERE teacher_id = :teacher_id AND time_start <= :current_time AND time_end >= :current_time")
    LiveData<List<TimeTableEntity>> getTimeTableByTeacherIdAndDate(Integer teacher_id, Date current_time);

    @Transaction
    @Query("SELECT * FROM time_table WHERE group_id = :group_id AND time_start >= :start_time AND time_end <= :end_time ORDER BY time_start")
    LiveData<List<TimeTableEntity>> getTimeTableByTimeGroup(Date start_time, Date end_time, Integer group_id);

    @Transaction
    @Query("SELECT * FROM time_table WHERE teacher_id = :teacher_id AND time_start >= :start_time AND time_end <= :end_time ORDER BY time_start")
    LiveData<List<TimeTableEntity>> getTimeTableByTimeTeacher(Date start_time, Date end_time, Integer teacher_id);

    @Transaction
    @Insert
    void insertTimeTable(List<TimeTable> data);
}
