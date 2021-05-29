package org.hse.lab2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import org.hse.lab2.R;
import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.entity.TimeTableWithTeacherEntity;
import org.hse.lab2.model.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentActivity extends MainActivity {
    public TextView status;
    public TextView subject;
    public TextView cabinet;
    public TextView corp;
    public TextView teacher;
    Date currentTime = new Date();
    private Spinner spinner;
    private int selectedIndex = -1;
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
                selectedIndex = selectedItemPosition;
                Object item = adapter.getItem(selectedItemPosition);
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
        showScheduleImpl(ScheduleMode.STUDENT, type, (Group) selectedItem);
    }

    @Override
    protected void showTime(Date dateTime) {
        super.showTime(dateTime);
        mainViewModel.getTimeTableTeacherByDate(dateTime).observe(this, new Observer<List<TimeTableWithTeacherEntity>>() {
            @Override
            public void onChanged(List<TimeTableWithTeacherEntity> list) {
                for (TimeTableWithTeacherEntity listEntity : list) {
                    Log.d("TAG", listEntity.timeTableEntity.subj_name + " " + listEntity.teacherEntity.fio);
                    // TODO move to DB query
                    if (getSelectedGroup() != null && getSelectedGroup().getId().equals(listEntity.timeTableEntity.groupId)) {
                        initDataFromTimeTable(listEntity);
                    }
                }
            }
        });
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

    private void initDataFromTimeTable(TimeTableWithTeacherEntity timeTableWithTeacherEntity) {
        if (timeTableWithTeacherEntity == null) {
            status.setText("Нет пар");
            subject.setText("Дисциплина");
            cabinet.setText("Кабинет");
            corp.setText("Кабинет");
            teacher.setText("Преподаватель");
            return;
        }
        status.setText("Идет пара");
        TimeTableEntity timeTableEntity = timeTableWithTeacherEntity.timeTableEntity;

        subject.setText(timeTableEntity.subj_name);
        cabinet.setText(timeTableEntity.cabinet);
        corp.setText(timeTableEntity.corp);
        teacher.setText(timeTableWithTeacherEntity.teacherEntity.fio);
    }

    private Group getSelectedGroup() {
        return selectedIndex == -1 ? null : adapter.getItem(selectedIndex);
    }
}


