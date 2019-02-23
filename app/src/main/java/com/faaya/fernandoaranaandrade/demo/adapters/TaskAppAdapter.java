package com.faaya.fernandoaranaandrade.demo.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.faaya.fernandoaranaandrade.demo.Beans.DateColor;
import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.R;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.HourUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskAppAdapter extends ArrayAdapter<TaskApp> {
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
    private String verde, rojo, blanco;
    private boolean sameProyect;

    public TaskAppAdapter(Context context, List<TaskApp> values, boolean sameProyect) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
        this.queries = new Queries(context);
        this.verde = context.getString(R.string.green);
        this.rojo = context.getString(R.string.red);
        this.blanco = context.getString(R.string.white);
        this.sameProyect = sameProyect;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView;
        TaskApp taskApp = values.get(position);
        boolean activeNotifications = isActiveNotifications(taskApp);
        boolean isUnfinished = isUnfinished(taskApp);
        boolean hasEndDate = hasEndDate(taskApp);
        int layout = getLayout(activeNotifications, hasEndDate);
        rowView = inflater.inflate(layout, parent, false);
        TextView nameTextView = rowView.findViewById(R.id.name_task);
        TextView typeTextView = rowView.findViewById(R.id.textView_type);
        typeTextView.setText(queries.getTaskTypeById(taskApp.getIdType()).getName().toUpperCase());
        if(!sameProyect){
            TextView proyectTextView = rowView.findViewById(R.id.textView_proyect);
            Proyect proyect = queries.getByIdProyect(taskApp.getProyectId());
            proyectTextView.setText(proyect.getName().toUpperCase());
        }

        SpannableString spanString = new SpannableString(taskApp.getName().toUpperCase());
        spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
        nameTextView.setText(spanString);
        if(hasEndDate){
            TextView startTextView = rowView.findViewById(R.id.start_label_task);
            Date endDate = new Date(taskApp.getDateEnd());
            String dateString;
            if(isBegingDate(taskApp.getDateEnd())){
                dateString = DateEnum.dateSimpleDateFormat.format(endDate);
            } else {
                dateString = DateEnum.fullDateSimpleDateFormat.format(endDate);
            }
            startTextView.setText(dateString);
            putColor(rowView, taskApp, nameTextView, isUnfinished);
        } else {
            if (taskApp.getActiveSemaforo().equals(SettingsEnum.ON.toString())) {
                LinearLayout linearLayout = rowView.findViewById(R.id.layout_all_task_);
                int color = getColorWithEndDate(taskApp, isUnfinished);
                linearLayout.setBackgroundColor(color);
                if(color == RED){
                    nameTextView.setTextColor(RED_R);
                }
                if(color == GREEN){
                    nameTextView.setTextColor(GREEN_R);
                }
            } else {
                strikeThruText(taskApp, nameTextView, isUnfinished);
            }
        }
        if(activeNotifications){
            TextView textViewInfo = rowView.findViewById(R.id.textViewNotificationInfo);
            textViewInfo.setText(taskApp.getDateNotification());
            if(!isAfterTodayDate(taskApp)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ColorStateList colorStateList = rowView.getResources().getColorStateList(R.color.colorGray);
                    textViewInfo.setTextColor(colorStateList);
                    ImageView imageView = rowView.findViewById(R.id.imageViewClock);
                    imageView.setImageTintList(colorStateList);
                }
            }
        }
        return rowView;
    }

    private int getColorWithEndDate(TaskApp taskApp, boolean isUnfinished) {
        if(isUnfinished){
            return getSemaforoByColor(taskApp.getUnfinishSemaforo());
        } else {
            return getSemaforoByColor(taskApp.getRealSemaforo());
        }
    }

    private int getSemaforoByColor(String semaforo) {
        if (semaforo.equalsIgnoreCase(rojo)) {
            return RED;
        }
        if (semaforo.equalsIgnoreCase(blanco)) {
            return WHITE;
        }
        if (semaforo.equalsIgnoreCase(verde)) {
            return GREEN;
        }
        return WHITE;
    }

    private boolean isBegingDate(Long dateEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateEnd);
        return calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0;
    }

    private void putColor(View rowView, TaskApp taskApp, TextView nameTextView, boolean isUnfinished) {
        if (taskApp.getActiveSemaforo().equals(SettingsEnum.ON.toString())) {
            LinearLayout linearLayout = rowView.findViewById(R.id.layout_all_task_);
            if (isUnfinished) {
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
                if (taskApp.getRealSemaforo().equals(verde)) {
                    color = GREEN;
                    nameTextView.setTextColor(GREEN_R);
                }
                linearLayout.setBackgroundColor(color);
            }
        } else {
            strikeThruText(taskApp, nameTextView, isUnfinished);
        }
    }

    private void strikeThruText(TaskApp taskApp, TextView nameTextView, boolean isUnfinished) {
        if(!isUnfinished){
            SpannableString spanString = new SpannableString(taskApp.getName().toUpperCase());
            spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
            nameTextView.setText(spanString);
            nameTextView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private boolean hasEndDate(TaskApp taskApp) {
        return taskApp.getDateEnd() != null && taskApp.getDateEnd().longValue() != 0l;
    }

    private int getLayout(boolean activeNotifications, boolean hasEndDate) {
        if(hasEndDate){
            if(activeNotifications){
                if(sameProyect){
                    return R.layout.task_view_list_without_proyect;
                } else {
                    return R.layout.task_view_list;
                }
            } else {
                if(sameProyect){
                    return R.layout.task_view_list_without_notification_and_proyect;
                } else {
                    return R.layout.task_view_list_without_notification;
                }
            }
        } else {
            if(activeNotifications){
                if(sameProyect){
                    return R.layout.task_view_list_without_proyect_sin_fecha;
                } else {
                    return R.layout.task_view_list_sin_fecha;
                }
            } else {
                if(sameProyect){
                    return R.layout.task_view_list_without_notification_and_proyect_sin_fecha;
                } else {
                    return R.layout.task_view_list_without_notification_sin_fecha;
                }
            }
        }
    }

    private boolean isUnfinished(TaskApp taskApp) {
        return taskApp.getRealDate() == null || taskApp.getRealDate().longValue() == 0l;
    }

    private boolean isActiveNotifications(TaskApp taskApp) {
        return taskApp.getActiveNotification() != null && taskApp.getActiveNotification().equals(SettingsEnum.ON.toString());
    }

    private boolean isAfterTodayDate(TaskApp taskApp) {
        Long alarmTime = HourUtils.getCalendar(taskApp.getDateNotification());
        return new Date().before(new Date(alarmTime));
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
            //System.out.println("priority:"+priority + "show:" + dateColor.show() + ",semaforo:" + dateColor.getSemaforo()+", prioriy" + dateColor.getPriority());
            if (dateColor.show() && dateColor.getPriority() <= priority) {
                color = dateColor.getColor();
                priority = dateColor.getPriority();
            }
        }
        return color;
    }
}
