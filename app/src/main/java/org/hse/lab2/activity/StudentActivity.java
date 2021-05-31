package org.hse.lab2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.hse.lab2.R;
import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TimeTable;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hse.lab2.utils.Converters.dateFromString;

public class StudentActivity extends MainActivity {
    public TextView status;
    public TextView subject;
    public TextView cabinet;
    public TextView corp;
    public TextView teacher;
    private Spinner spinner;
    ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        spinner = findViewById(R.id.groupList);

        List<Group> groups = new ArrayList<>();
        initGroupList(groups);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long id) {
                Group item = adapter.getItem(selectedItemPosition);
                updateData(dateFromString((String) time.getText()), item.getId());
                Log.d("student", "selectedItem: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time = findViewById(R.id.time);
        initTime();

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initData();

        View scheduleDay = findViewById(R.id.schedule_day);
        scheduleDay.setOnClickListener(v -> showSchedule(ScheduleType.DAY));
        View scheduleWeek = findViewById(R.id.schedule_week);
        scheduleWeek.setOnClickListener(v -> showSchedule(ScheduleType.WEEK));
    }

    private void showSchedule(ScheduleType type) {
        Object selectedItem = spinner.getSelectedItem();
        if (!(selectedItem instanceof Group)) {
            return;
        }
        showScheduleStudent(ScheduleMode.STUDENT, type, (Group) selectedItem);
    }
    @Override
    protected void showTime(Date dateTime) {
        super.showTime(dateTime);
        updateData(dateTime, null);
    }


    private void initGroupList(List<Group> groups) {
       mainViewModel.getGroups().observe(this, new Observer<List<GroupEntity>>() {
           @Override
           public void onChanged(@Nullable List<GroupEntity> list) {
               List<Group> groupsResult = new ArrayList<>();
               for (GroupEntity listEntity : list) {
                   groupsResult.add(new Group(listEntity.id, listEntity.name));
               }
               adapter.clear();
               adapter.addAll(groupsResult);
           }
       });
    }

    private void initData() {
        initDataFromTimeTable(null);
    }

    private void initDataFromTimeTable(TimeTableEntity timeTableWithTeacherEntity) {
        if (timeTableWithTeacherEntity == null) {
            status.setText("Нет пар");
            subject.setText("Дисциплина");
            cabinet.setText("Кабинет");
            corp.setText("Кабинет");
            teacher.setText("Преподаватель");
            return;
        }
        status.setText("Идет пара");
        TimeTable timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;

        subject.setText(timeTableEntity.subj_name);
        cabinet.setText(timeTableEntity.cabinet);
        corp.setText(timeTableEntity.corp);
        teacher.setText(timeTableWithTeacherEntity.timeTableEntity.time_start.toString());
    }

    private Group getSelectedGroup() {
        Object selectedItem = spinner.getSelectedItem();
        return  !(spinner.getSelectedItem() instanceof Group) ? null : (Group) selectedItem;
    }

    private void updateData(Date currentTime, Integer id) {
        if (id == null) {
            mainViewModel.getTimeTableTeacherByDate(currentTime).observe(this, list -> {
                for (TimeTableEntity listEntity : list) {
                    Log.d("Timetable without ID", listEntity.timeTableEntity.subj_name + " " + listEntity.teacherEntity.fio);
                    Group group = getSelectedGroup();
                    if (group != null && group.getId().equals(listEntity.timeTableEntity.groupId)) {
                        initDataFromTimeTable(listEntity);
                        return;
                    }
                }
                initDataFromTimeTable(null);
                return;
            });
        }
        mainViewModel.getTimeTableByGroupIdAndDate(id, currentTime).observe(this, list -> {
            for (TimeTableEntity listEntity : list) {
                Log.d("Timetable with ID", listEntity.timeTableEntity.subj_name + " " + listEntity.teacherEntity.fio);
                initDataFromTimeTable(listEntity);
                return;

            }
            initDataFromTimeTable(null);
        });
    }
}


