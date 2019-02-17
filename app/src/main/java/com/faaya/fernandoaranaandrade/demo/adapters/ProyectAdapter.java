package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
        GREEN_R = context.getResources().getColor(R.color.colorPrimaryDark);
        RED_R = context.getResources().getColor(R.color.colorRed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.obra_view_list, parent, false);
        try {
            TextView nameTextView = rowView.findViewById(R.id.name);
            TextView textView1 = rowView.findViewById(R.id.start_label);
            Proyect proyect = values.get(position);
            nameTextView.setText(proyect.getName());
            if(proyect.getStart() == 0l){
                textView1.setVisibility(View.INVISIBLE);
                ImageView imageView = rowView.findViewById(R.id.imageViewSandClock);
                imageView.setVisibility(View.INVISIBLE);

            } else {
                RelativeLayout relativeLayout = rowView.findViewById(R.id.relaviteLayoutProyect);
                FechasBean fechaBean = FechasUtils.getFechasBean(new Date(proyect.getStart()), proyect.getTime().intValue(), proyect.getRange(), map);
                int countTaskWithoutRealDate = queries.getCountTaskWithoutRealDate(proyect.getId());
                if(countTaskWithoutRealDate > 0){
                    textView1.setText(fechaBean.getResultaTimeString() + " - " + context.getString(R.string.unfinishTask) + ": " + countTaskWithoutRealDate);
                } else {
                    textView1.setText(fechaBean.getResultaTimeString());
                }
                if (fechaBean.getEndCalendar().getTime().before(new Date())) {
                    if (hasTaskWithoutFinish(countTaskWithoutRealDate)) {
                        textView1.setTextColor(RED_R);
                        textView1.setText(getBoldText(this.tiempo_finalizado + " - " + context.getString(R.string.unfinishTask) + ": " + countTaskWithoutRealDate));
                        relativeLayout.setBackgroundColor(RED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageView imageView = rowView.findViewById(R.id.imageViewSandClock);
                            imageView.setImageTintList(rowView.getResources().getColorStateList(R.color.colorRed));
                        }
                    } else {
                        textView1.setTextColor(GREEN_R);
                        textView1.setText(getBoldText(this.proyect_finished));
                        relativeLayout.setBackgroundColor(GREEN);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ImageView imageView = rowView.findViewById(R.id.imageViewSandClock);
                            imageView.setImageDrawable(rowView.getResources().getDrawable(R.drawable.ic_check));
                            imageView.setImageTintList(rowView.getResources().getColorStateList(R.color.colorPrimaryDark));
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ColorStateList colorStateList = rowView.getResources().getColorStateList(R.color.colorGray);
                        textView1.setTextColor(colorStateList);
                        ImageView imageView = rowView.findViewById(R.id.imageViewSandClock);
                        imageView.setImageTintList(colorStateList);
                    }
                }
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
        System.out.println("count:" + countTaskWithoutRealDate);
        return countTaskWithoutRealDate > 0;
    }
}