package org.hse.lab2.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TimeTableEntity {
    @Embedded
    public TimeTable timeTableEntity;
    @Relation(
            parentColumn = "teacher_id",
            entityColumn = "id"
    )
    public TeacherEntity teacherEntity;
    @Relation(
            parentColumn = "group_id",
            entityColumn = "id"
    )
    public GroupEntity groupEntity;
}
