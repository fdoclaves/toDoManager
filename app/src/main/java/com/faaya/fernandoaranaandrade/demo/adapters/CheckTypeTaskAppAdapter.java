package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.R;

import java.util.List;

public class CheckTypeTaskAppAdapter extends ArrayAdapter<TaskType> {
    private final Context context;
    private final List<TaskType> values;
    private List<TaskType> checkeds;

    public CheckTypeTaskAppAdapter(Context context, List<TaskType> allTaskType, List<TaskType> checked) {
        super(context, -1, allTaskType);
        this.context = context;
        this.values = allTaskType;
        this.checkeds = checked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.check_type_task_view_list, parent, false);
        CheckBox checkBox = rowView.findViewById(R.id.checkBoxTypeTask);
        final TaskType taskType = values.get(position);
        for (TaskType checked : checkeds) {
            if(checked.getId().longValue() == taskType.getId().longValue()){
                checkBox.setChecked(true);
                break;
            }
        }
        checkBox.setText(taskType.getName());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkeds.add(taskType);
                } else {
                    checkeds.remove(taskType);
                }
            }
        });
        return rowView;
    }


}
