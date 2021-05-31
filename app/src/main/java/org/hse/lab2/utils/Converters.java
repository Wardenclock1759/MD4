package org.hse.lab2.utils;

import android.util.Log;

import androidx.room.TypeConverter;

import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.ScheduleItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date dateFromString(String value) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm",
                        Locale.forLanguageTag("ru"));
        try {
            return value == null ? null : simpleDateFormat.parse(value);
        }
        catch (ParseException e) {
            Log.e("UTILS", "dateFromString: error parsing" + value, e);
        }
        return null;
    }

    public static ScheduleItem scheduleItemFromTimeTable(TimeTableEntity timetable, Boolean isStudent) {
        if (timetable == null) return null;

        SimpleDateFormat hoursAndMinutes =
                new SimpleDateFormat("HH:mm",
                        Locale.forLanguageTag("ru"));
        SimpleDateFormat day =
                new SimpleDateFormat("EEEE",
                        Locale.forLanguageTag("ru"));

        ScheduleItem item = new ScheduleItem();
        item.setName(timetable.timeTableEntity.subj_name);
        item.setStart(hoursAndMinutes.format(timetable.timeTableEntity.time_start));
        item.setEnd(hoursAndMinutes.format(timetable.timeTableEntity.time_end));
        item.setPlace(timetable.timeTableEntity.cabinet + " " + timetable.timeTableEntity.corp);
        if (isStudent) {
            item.setTeacher(timetable.teacherEntity.fio);
        }
        else item.setTeacher(timetable.groupEntity.name);
        item.setType(day.format(timetable.timeTableEntity.time_start));
        return item;
    }

    public static String trimDate(Date value) {
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd",
                        Locale.forLanguageTag("ru"));
        return simpleDateFormat.format(value);
    }

    public static Date getNearestWeekEnd(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", Locale.forLanguageTag("ru"));
        String day = simpleDateFormat.format(date);
        int amount = 0;

        switch (day) {
            case "понедельник":
                amount = 5;
                break;
            case "вторник":
                amount = 4;
                break;
            case "среда":
                amount = 3;
                break;
            case "четверг":
                amount = 2;
                break;
            case "пятница":
                amount = 1;
                break;
            case "суббота":
                amount = 0;
                break;
            case "воскресенье":
                amount = 6;
                break;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, amount);
       return calendar.getTime();
    }
}
