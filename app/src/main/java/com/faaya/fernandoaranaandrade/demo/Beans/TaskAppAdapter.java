package com.faaya.fernandoaranaandrade.demo.Beans;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAppAdapter extends ArrayAdapter<TaskApp> {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final Context context;
    private final List<TaskApp> values;
    private Queries queries;

    private static int ORANGE = Color.argb(41, 255, 165, 0);
    private static int WHITE = Color.argb(41, 255, 255, 255);
    private static int RED = Color.argb(41, 216, 27, 96);
    private static int YELLOW = Color.argb(41, 255, 233, 69);
    private static int GREEN = Color.argb(41, 0, 133, 119);

    private static int ORANGE_R = Color.argb(100, 170, 99, 42);
    private static int RED_R = Color.argb(100, 186, 32, 37);
    private static int YELLOW_R = Color.argb(100, 172, 148, 49);
    private static int GREEN_R = Color.argb(100, 0, 133, 119);

    public TaskAppAdapter(Context context, List<TaskApp> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.queries = new Queries(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.task_view_list, parent, false);
        TextView nameTextView = rowView.findViewById(R.id.name_task);
        TextView startTextView = rowView.findViewById(R.id.start_label_task);
        TextView typeTextView = rowView.findViewById(R.id.textView_type);
        TextView proyectTextView = rowView.findViewById(R.id.textView_proyect);

        TaskApp taskApp = values.get(position);


        SpannableString spanString = new SpannableString(taskApp.getName().toUpperCase());
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        nameTextView.setText(spanString);
        Date endDate = new Date(taskApp.getDateEnd());
        startTextView.setText("Fecha estimada: " + simpleDateFormat.format(endDate));
        typeTextView.setText(queries.getTaskTypeById(taskApp.getIdType()).getName().toUpperCase());
        Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
        proyectTextView.setText(proyect.getName().toUpperCase());

        if (taskApp.getActiveSemaforo().equals(SettingsEnum.ON.toString())) {
            LinearLayout linearLayout = rowView.findViewById(R.id.layout_all_task_);
            if (taskApp.getRealDate() == null || taskApp.getRealDate().longValue() == 0l) {
                int color = getColor(taskApp);
                linearLayout.setBackgroundColor(color);
                if(color == RED){
                    nameTextView.setTextColor(RED_R);
                }
                if(color == YELLOW){
                    nameTextView.setTextColor(YELLOW_R);
                }
                if(color == ORANGE){
                    nameTextView.setTextColor(ORANGE_R);
                }
            } else {
                int color = Color.WHITE;
                if (taskApp.getRealSemaforo().equals(SettingsEnum.VERDE.toString())) {
                    color = GREEN;
                    nameTextView.setTextColor(GREEN_R);
                }
                linearLayout.setBackgroundColor(color);
            }
        }
        return rowView;
    }

    private int getColor(TaskApp taskApp) {
        int color = Color.WHITE;
        int priority = 365;
        Date endDate = new Date(taskApp.getDateEnd());
        List<DateColor> dateColors = new ArrayList<>();
        dateColors.add(new DateColor(taskApp.getWhiteSemaforo(), endDate, WHITE));
        dateColors.add(new DateColor(taskApp.getYellowSemaforo(), endDate, YELLOW));
        dateColors.add(new DateColor(taskApp.getOrangeSemaforo(), endDate, ORANGE));
        dateColors.add(new DateColor(taskApp.getRedSemaforo(), endDate, RED));
        for (DateColor dateColor : dateColors) {
            if (dateColor.show() && dateColor.getPriority() <= priority) {
                color = dateColor.getColor();
                priority = dateColor.getPriority();
            }
        }
        return color;
    }
}
