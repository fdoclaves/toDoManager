package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.R;

import java.util.List;

public class CheckProjectAdapter extends ArrayAdapter<Proyect> {
    private final Context context;
    private final List<Proyect> values;
    private List<Proyect> checkeds;

    public CheckProjectAdapter(Context context, List<Proyect> allProyects, List<Proyect> checked) {
        super(context, -1, allProyects);
        this.context = context;
        this.values = allProyects;
        this.checkeds = checked;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.check_type_task_view_list, parent, false);
        CheckBox checkBox = rowView.findViewById(R.id.checkBoxTypeTask);
        final Proyect proyect = values.get(position);
        for (Proyect checked : checkeds) {
            if(checked.getId().longValue() == proyect.getId().longValue()){
                checkBox.setChecked(true);
                break;
            }
        }
        checkBox.setText(proyect.getName().toUpperCase());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checkeds.add(proyect);
                } else {
                    checkeds.remove(proyect);
                }
            }
        });
        return rowView;
    }


}
