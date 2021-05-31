package org.hse.lab2.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.hse.lab2.dao.HseRepository;
import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TeacherEntity;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.ScheduleItem;

import java.util.Date;
import java.util.List;

import static org.hse.lab2.utils.Converters.dateFromString;

public class MainViewModel extends AndroidViewModel {

    private HseRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new HseRepository(application);
    }

    public LiveData<List<GroupEntity>> getGroups() {
        return repository.getGroups();
    }

    public LiveData<List<TeacherEntity>> getTeachers() {
        return repository.getTeachers();
    }

    public LiveData<List<TimeTableEntity>> getTimeTableTeacherByDate(Date date) {
        return repository.getTimeTableByDate(date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByGroupIdAndDate(Integer id, Date date) {
        return repository.getTimeTableByGroupIdAndDate(id, date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTeacherIdAndDate(Integer id, Date date) {
        return repository.getTimeTableByTeacherIdAndDate(id, date);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTimeGroup(String date_start, String date_end, Integer group_id) {
        return repository.getTimeTableByTimeGroup(dateFromString(date_start + " 00:00"), dateFromString(date_end + " 23:59"), group_id);
    }

    public LiveData<List<TimeTableEntity>> getTimeTableByTimeTeacher(String date_start, String date_end, Integer teacher_id) {
        return repository.getTimeTableByTimeTeacher(dateFromString(date_start + " 00:00"), dateFromString(date_end + " 23:59"), teacher_id);
    }
}
