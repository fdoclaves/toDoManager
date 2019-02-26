package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
// ...

public class DateDialogFragment extends DialogFragment {

    private static final String BUTTON_TEXT = "buttonText";

    public static final String TITLE = "ID";
    private Button okButton;
    private Button cancelButton;
    private OkActionDate okAction;
    private Calendar calendar;

    public DateDialogFragment() {

    }

    public static DateDialogFragment newInstance(String title, String textButton) {
        DateDialogFragment frag = new DateDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(BUTTON_TEXT, textButton);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_date, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calendar = Calendar.getInstance();
        final CalendarView calendarView = view.findViewById(R.id.calendarView2);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        });
        okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                okAction.doAction(calendar);
                dismiss();
            }
        });
        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
        String title = getArguments().getString(TITLE, "");
        getDialog().setTitle(title);

        setDate(calendarView);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    private void setDate(CalendarView calendarView) {
        try {
            if (getArguments().get(BUTTON_TEXT) != null) {
                Date date = DateEnum.dateSimpleDateFormat.parse((String) getArguments().get(BUTTON_TEXT));
                calendarView.setDate(date.getTime());
                calendar.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setOkActionDate(OkActionDate okAction) {
        this.okAction = okAction;
    }
}