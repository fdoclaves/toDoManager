package com.faaya.fernandoaranaandrade.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import com.faaya.fernandoaranaandrade.demo.database.DataBase;
import com.faaya.fernandoaranaandrade.demo.utils.HourUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HourDialogFragment extends DialogFragment {

    public static final String TITLE = "HOUR";
    public static final String BUTTON_TEXT = "BUTTON_TEXT";
    private OkActionDate okAction;
    private Calendar calendar;
    private TimePicker timePicker;
    private Button okButton;
    private Button cancelButton;

    public HourDialogFragment() {

    }

    public static HourDialogFragment newInstance(String title, String textButton) {
        HourDialogFragment frag = new HourDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(BUTTON_TEXT, textButton);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_hour, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendar = Calendar.getInstance();
        timePicker = view.findViewById(R.id.timePicker1);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });
        okButton = view.findViewById(R.id.okbuttonhour);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                okAction.doAction(calendar);
                dismiss();
            }
        });
        cancelButton = view.findViewById(R.id.cancelbuttonhour);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
        String title = getArguments().getString(TITLE, "");
        getDialog().setTitle(title);

        setDate();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    private void setDate() {
        if (getArguments().get(BUTTON_TEXT) != null) {
            String text = (String) getArguments().get(BUTTON_TEXT);
            String[] split = HourUtils.getTime(text).split(":");
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(hour);
                timePicker.setMinute(minute);
            }
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }
    }

    public void setOkActionDate(OkActionDate okAction) {
        this.okAction = okAction;
    }
}