package org.hse.lab2;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    public TextView time;
    public TextView status;
    public TextView subject;
    public TextView cabinet;
    public TextView corp;
    public TextView teacher;
    Date currentTime = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        final Spinner spinner = findViewById(R.id.groupList);

        List<StudentActivity.Group> groups = new ArrayList<>();
        initGroupList(groups);

        ArrayAdapter<?> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, groups);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long id) {
                Object item = adapter.getItem(selectedItemPosition);
                Log.d("student", "selectedItem: " + item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        time = findViewById(R.id.time);
        //initTime();

        status = findViewById(R.id.status);
        subject = findViewById(R.id.subject);
        cabinet = findViewById(R.id.cabinet);
        corp = findViewById(R.id.corp);
        teacher = findViewById(R.id.teacher);

        initData();
    }

    static class Group {
        private Integer id;
        private String name;

        public Group(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private void initGroupList(List<Group> groups) {
        @SuppressLint("SimpleDateFormat") String yearString = new java.text.SimpleDateFormat("yyyy")
                .format(currentTime);
        int year = Integer.parseInt(yearString.substring(2));
        String[] programs = new String[] { "БИ", "ПИ", "Э",
                "М", "И", "Ю"};
        int minYear = year - 4;
        String groupString;
        for (int i = year - 1; i >= minYear; i--) {
            for (String program : programs) {
                for (int k = 1; k < 3; k++) {
                    groupString = program + "-" + i + "-" + k;
                    groups.add(new Group(groups.size() + 1, groupString));
                }
            }
        }
    }
/*
    private void initTime() {
        SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat
                ("HH:mm:EEEE", Locale.getDefault());
        time.setText(java.text.SimpleDateFormat.format((currentTime)));
    }
*/
    private void initData() {
        status.setText("Нет пар");
        subject.setText("Дисциплина");
        cabinet.setText("Кабинет");
        corp.setText("Корпус");
        teacher.setText("Преподаватель");
    }
}


