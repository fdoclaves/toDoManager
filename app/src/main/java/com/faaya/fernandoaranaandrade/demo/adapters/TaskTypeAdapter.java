package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.R;
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
        textView.setText(taskType.getName().toUpperCase());
        return rowView;
    }
}