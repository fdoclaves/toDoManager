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

public class ProyectAdapter extends ArrayAdapter<Proyect> {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final Context context;
    private final List<Proyect> values;

    public ProyectAdapter(Context context, List<Proyect> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.obra_view_list, parent, false);
        TextView textView = rowView.findViewById(R.id.name);
        TextView textView1 = rowView.findViewById(R.id.start_label);
        Proyect proyect = values.get(position);
        textView.setText(proyect.getName());
        try {
            FechasBean fechaBean = FechasUtils.getFechasBean(new Date(proyect.getStart()), proyect.getTime().intValue(), proyect.getRange());
            textView1.setText(fechaBean.getResultaTimeString());
            if(fechaBean.getEndCalendar().getTime().before(new Date())){
                textView1.setTextColor(Color.RED);
                textView1.setText("TIEMPO FINALIZADO");
            } else {
                textView1.setTextColor(Color.GRAY);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rowView;
    }
}