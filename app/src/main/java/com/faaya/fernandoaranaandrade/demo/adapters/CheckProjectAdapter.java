package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.R;
import java.util.List;

public class CheckProjectAdapter extends ArrayAdapter<Proyect> {
    private List<Proyect> checkeds;
    private final Context context;
    private final List<Proyect> values;

    public CheckProjectAdapter(Context context, List<Proyect> list, List<Proyect> list2) {
        super(context, -1, list);
        this.context = context;
        this.values = list;
        this.checkeds = list2;
    }

    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.check_type_task_view_list, parent, false);
        CheckBox checkBox = rowView.findViewById(R.id.checkBoxTypeTask);
        final Proyect proyect = (Proyect) this.values.get(i);
        for (Proyect id : this.checkeds) {
            if (id.getId().longValue() == proyect.getId().longValue()) {
                checkBox.setChecked(true);
                break;
            }
        }
        checkBox.setText(proyect.getName().toUpperCase());
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    CheckProjectAdapter.this.checkeds.add(proyect);
                } else {
                    CheckProjectAdapter.this.checkeds.remove(proyect);
                }
            }
        });
        return rowView;
    }
}
