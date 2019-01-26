package com.faaya.fernandoaranaandrade.demo.Beans;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.utils.FechasBean;
import com.faaya.fernandoaranaandrade.demo.utils.FechasUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TaskTypeAdapter extends ArrayAdapter<TaskType> {
    private final Context context;
    private final List<TaskType> values;

    public TaskTypeAdapter(Context context, List<TaskType> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_type_layout, parent, false);
        TextView textView = rowView.findViewById(R.id.textViewTitule);
        TaskType taskType = values.get(position);
        textView.setText(taskType.getName());
        return rowView;
    }
}