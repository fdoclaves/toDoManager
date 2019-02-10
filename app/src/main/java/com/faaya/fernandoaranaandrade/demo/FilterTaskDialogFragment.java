package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
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
    private OkActionFilter okAction;
    private ListView listViewCheckTypes;
    private Queries queries;
    private Switch switchListCheck;
    private List<TaskType> checkedBefore;
    private List<TaskType> checkedCurrent;
    private Boolean unfishedTask;
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
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switchListCheck = view.findViewById(R.id.switchListCheck);
        if(unfishedTask != null){
            switchListCheck.setChecked(unfishedTask);
        }
        okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                okAction.doAction(checkedCurrent, switchListCheck.isChecked());
                dismiss();
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
        queries = new Queries(view.getContext());
        listViewCheckTypes.setAdapter(new CheckTypeTaskAppAdapter(view.getContext(), allTaskType, checkedCurrent));
        String title = getArguments().getString(TITLE, "");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    public void setData(OkActionFilter okActionFilter, List<TaskType> checked, Boolean unfishedTask, List<TaskType> allTaskType) {
        this.okAction = okActionFilter;
        this.checkedBefore = checked;
        this.unfishedTask = unfishedTask;
        this.allTaskType = allTaskType;
    }
}