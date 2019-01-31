package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.FechasBean;
import com.faaya.fernandoaranaandrade.demo.utils.FechasUtils;
import com.faaya.fernandoaranaandrade.demo.utils.StringUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProyectAdapter extends ArrayAdapter<Proyect> {
    private final Context context;
    private final List<Proyect> values;
    private String tiempo_finalizado;
    private String proyect_finished;
    private Map<String, String> map;
    private static int RED = Color.argb(41, 216, 27, 96);
    private static int RED_R = Color.argb(100, 186, 32, 37);
    private static int GREEN = Color.argb(41, 0, 133, 119);
    private static int GREEN_R = Color.argb(100, 0, 133, 119);
    private Queries queries;

    public ProyectAdapter(Context context, List<Proyect> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.tiempo_finalizado = context.getString(R.string.tiempo_finalizado);
        this.proyect_finished = context.getString(R.string.Finished_project);
        this.map = StringUtils.getMap(context);
        this.queries = new Queries(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.obra_view_list, parent, false);
        TextView textView = rowView.findViewById(R.id.name);
        TextView textView1 = rowView.findViewById(R.id.start_label);
        RelativeLayout relativeLayout = rowView.findViewById(R.id.relaviteLayoutProyect);
        Proyect proyect = values.get(position);
        textView.setText(proyect.getName());
        try {
            FechasBean fechaBean = FechasUtils.getFechasBean(new Date(proyect.getStart()), proyect.getTime().intValue(), proyect.getRange(), map);
            textView1.setText(fechaBean.getResultaTimeString());
            if(fechaBean.getEndCalendar().getTime().before(new Date())){
                int countTaskWithoutRealDate = queries.getCountTaskWithoutRealDate(proyect.getId());
                if(hasTaskWithoutFinish(countTaskWithoutRealDate)){
                    textView1.setTextColor(RED_R);
                    textView1.setText(getBoldText(this.tiempo_finalizado + ". " + countTaskWithoutRealDate + " " + context.getString(R.string.unfinishTask)));
                    relativeLayout.setBackgroundColor(RED);
                } else {
                    textView1.setTextColor(GREEN_R);
                    textView1.setText(getBoldText(this.proyect_finished));
                    relativeLayout.setBackgroundColor(GREEN);
                }
            } else {
                textView1.setTextColor(Color.GRAY);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rowView;
    }

    private SpannableString getBoldText(String text) {
        SpannableString spanString = new SpannableString(text);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        return spanString;
    }

    private boolean hasTaskWithoutFinish(int countTaskWithoutRealDate) {
        System.out.println("count:"+countTaskWithoutRealDate);
        return countTaskWithoutRealDate > 0;
    }
}