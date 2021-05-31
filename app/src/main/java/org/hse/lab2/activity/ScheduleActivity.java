package org.hse.lab2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.hse.lab2.R;
import org.hse.lab2.entity.GroupEntity;
import org.hse.lab2.entity.TimeTableEntity;
import org.hse.lab2.model.Group;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hse.lab2.model.ScheduleItem;
import org.hse.lab2.model.ScheduleItemHeader;

import static org.hse.lab2.utils.Converters.dateFromString;
import static org.hse.lab2.utils.Converters.getNearestWeekEnd;
import static org.hse.lab2.utils.Converters.scheduleItemFromTimeTable;
import static org.hse.lab2.utils.Converters.trimDate;

public class ScheduleActivity extends MainActivity {

    public static final String ARG_TYPE = "type";
    public static final String ARG_MODE = "mode";
    public static final String ARG_TITLE = "title";
    public static final String ARG_TIME = "time";
    public static final String ARG_ID = "id";
    public static final int DEFAULT_ID = -1;

    private MainActivity.ScheduleType type;
    private MainActivity.ScheduleMode mode;
    private Integer id;
    private String title;
    private Date currentTime;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private TextView timeView;
    List<ScheduleItem> scheduleItemList = new ArrayList<>();
    ScheduleItem item = new ScheduleItem();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        type = (MainActivity.ScheduleType) getIntent().getSerializableExtra(ARG_TYPE);
        mode = (MainActivity.ScheduleMode) getIntent().getSerializableExtra(ARG_MODE);
        title = getIntent().getStringExtra(ARG_TITLE);
        id = getIntent().getIntExtra(ARG_ID, DEFAULT_ID);
        currentTime = (Date) getIntent().getSerializableExtra(ARG_TIME);

        TextView titleView = findViewById(R.id.title);
        titleView.setText(title);
        timeView = findViewById(R.id.time);

        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new ItemAdapter(this::onScheduleItemClick);
        recyclerView.setAdapter(adapter);

        Date t = getNearestWeekEnd(currentTime);
        Log.d("time", t.toString());

        initData();
    }

    private void initData() {
        if (mode == ScheduleMode.STUDENT) {
            if (type == ScheduleType.DAY) {
                mainViewModel.getTimeTableByTimeGroup(trimDate(currentTime), trimDate(currentTime), id).observe(this, list -> {
                    Log.d("time", list.toString());
                    for (TimeTableEntity listEntity : list) {
                        item = new ScheduleItem();
                        item = scheduleItemFromTimeTable(listEntity, true);
                        scheduleItemList.add(item);
                        Log.d("scheduleItem", item.getTeacher());
                    }
                    adapter.setDataList(scheduleItemList);
                });
            }
            else {
                mainViewModel.getTimeTableByTimeGroup(trimDate(currentTime), trimDate(getNearestWeekEnd(currentTime)), id).observe(this, list -> {
                    Log.d("time", list.toString());
                    for (TimeTableEntity listEntity : list) {
                        item = new ScheduleItem();
                        item = scheduleItemFromTimeTable(listEntity, true);
                        scheduleItemList.add(item);
                        Log.d("scheduleItem", item.getTeacher());
                    }
                    adapter.setDataList(scheduleItemList);
                });
            }
        }
        else {
            if (type == ScheduleType.DAY) {
                mainViewModel.getTimeTableByTimeTeacher(trimDate(currentTime), trimDate(currentTime), id).observe(this, list -> {
                    Log.d("time", list.toString());
                    for (TimeTableEntity listEntity : list) {
                        item = new ScheduleItem();
                        item = scheduleItemFromTimeTable(listEntity, false);
                        scheduleItemList.add(item);
                        Log.d("scheduleItem", item.getTeacher());
                    }
                    adapter.setDataList(scheduleItemList);
                });
            }
            else {
                mainViewModel.getTimeTableByTimeTeacher(trimDate(currentTime), trimDate(getNearestWeekEnd(currentTime)), id).observe(this, list -> {
                    Log.d("time", list.toString());
                    for (TimeTableEntity listEntity : list) {
                        item = new ScheduleItem();
                        item = scheduleItemFromTimeTable(listEntity, false);
                        scheduleItemList.add(item);
                        Log.d("scheduleItem", item.getTeacher());
                    }
                    adapter.setDataList(scheduleItemList);
                });
            }
        }

        if (currentTime != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.forLanguageTag("ru"));
            timeView.setText(simpleDateFormat.format(currentTime));
        }
    }

    private void onScheduleItemClick(ScheduleItem data) {

    }

    public final static class ItemAdapter extends
            RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int TYPE_ITEM = 0;
        private final static int TYPE_HEADER = 1;

        private List<ScheduleItem> dataList = new ArrayList<>();
        private OnItemClick onItemClick;

        public ItemAdapter(OnItemClick onItemClick) { this.onItemClick = onItemClick; }

        public static class ViewHolderHeader extends RecyclerView.ViewHolder {
            private Context context;
            private OnItemClick onItemClick;
            private TextView title;

            public ViewHolderHeader(View itemView, Context context, OnItemClick onItemClick) {
                super(itemView);
                this.context = context;
                this.onItemClick = onItemClick;
                title = itemView.findViewById(R.id.title);
            }

            public void bind(final ScheduleItemHeader data) { title.setText(data.getTitle()); }
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private Context context;
            private OnItemClick onItemClick;
            private TextView start;
            private TextView end;
            private TextView type;
            private TextView name;
            private TextView place;
            private TextView teacher;

            public ViewHolder(View itemView, Context context, OnItemClick onItemClick) {
                super(itemView);
                this.context = context;
                this.onItemClick = onItemClick;
                start = itemView.findViewById(R.id.start);
                end = itemView.findViewById(R.id.end);
                type = itemView.findViewById(R.id.type);
                name = itemView.findViewById(R.id.name);
                place = itemView.findViewById(R.id.place);
                teacher = itemView.findViewById(R.id.teacher);
            }

            public void bind(final ScheduleItem data) {
                start.setText(data.getStart());
                end.setText(data.getEnd());
                type.setText(data.getType());
                name.setText(data.getName());
                place.setText(data.getPlace());
                teacher.setText(data.getTeacher());
            }
        }

        public void setDataList(List<ScheduleItem> dataList) {
            this.dataList = new ArrayList<>();
            if (dataList != null) {
                this.dataList.addAll(dataList);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == TYPE_ITEM) {
                View contactView = inflater.inflate(R.layout.item_schedule, parent, false);
                return new ViewHolder(contactView, context, onItemClick);
            } else if (viewType == TYPE_HEADER) {
                View contactView = inflater.inflate(R.layout.item_schedule_header, parent, false);
                return new ViewHolderHeader(contactView, context, onItemClick);
            }
            throw new IllegalArgumentException("Invalid view type");
        }

        public int getItemViewType(int position) {
            ScheduleItem data = dataList.get(position);
            if (data instanceof ScheduleItemHeader) {
                return TYPE_HEADER;
            }
            return TYPE_ITEM;
        }

        @Override
        public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, int position) {
            ScheduleItem data = dataList.get(position);
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).bind(data);
            } else if (viewHolder instanceof ViewHolderHeader) {
                ((ViewHolderHeader) viewHolder).bind((ScheduleItemHeader) data);
            }
        }

        @Override
        public int getItemCount() { return dataList.size(); }
    }

    interface OnItemClick {
        void onClick(ScheduleItem data);
    }
}