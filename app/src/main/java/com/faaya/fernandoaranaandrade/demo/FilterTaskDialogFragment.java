package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.CheckTypeTaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.List;
// ...

public class FilterTaskDialogFragment extends DialogFragment {

    public static final String TITLE = "ID";
    private Button okButton;
    private Button cancelButton;
    private Button allButton;
    private Button noneButton;
    private OkActionFilter okAction;
    private ListView listViewCheckTypes;
    private Spinner spinnerFinished;
    private List<TaskType> checkedBefore;
    private List<TaskType> checkedCurrent;
    private String comboValue;
    private List<TaskType> allTaskType;

    public FilterTaskDialogFragment() {

    }

    public static FilterTaskDialogFragment newInstance(String title) {
        FilterTaskDialogFragment frag = new FilterTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_task, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerFinished = view.findViewById(R.id.spinnerFinished);
        final String[] rangeTimeValues = {getString(R.string.all), getString(R.string.ToDo), getString(R.string.done)};
        spinnerFinished.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.spinner18, rangeTimeValues));
        spinnerFinished.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                comboValue = rangeTimeValues[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        if(comboValue != null){
            for (int i = 0; i < rangeTimeValues.length; i++) {
                if(rangeTimeValues[i].equalsIgnoreCase(comboValue)){
                    spinnerFinished.setSelection(i);
                    break;
                }
            }
        }
        okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                okAction.doAction(checkedCurrent, comboValue);
                dismiss();
            }
        });
        allButton = view.findViewById(R.id.buttonAll);
        allButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                checkedCurrent.clear();
                checkedCurrent.addAll(allTaskType);
                listViewCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), allTaskType, checkedCurrent));
            }
        });
        noneButton = view.findViewById(R.id.buttonNone);
        noneButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                checkedCurrent.clear();
                listViewCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), allTaskType, checkedCurrent));
            }
        });
        checkedCurrent = new ArrayList<>();
        checkedCurrent.addAll(checkedBefore);
        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dismiss();
            }
        });
        listViewCheckTypes = view.findViewById(R.id.listViewCheckTypes);
        listViewCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), allTaskType, checkedCurrent));
        String title = getArguments().getString(TITLE, "");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    public void setData(OkActionFilter okActionFilter, List<TaskType> checked, String comboValue, List<TaskType> allTaskType) {
        this.okAction = okActionFilter;
        this.checkedBefore = checked;
        this.comboValue = comboValue;
        this.allTaskType = allTaskType;
    }

}