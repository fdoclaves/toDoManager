package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
// ...

public class EditNameDialogFragment extends DialogFragment {

    public static final String TITLE = "ID";
    private Button okButton;
    private Button cancelButton;
    private OkAction okAction;

    public EditNameDialogFragment() {

    }

    public static EditNameDialogFragment newInstance(String title) {
        EditNameDialogFragment frag = new EditNameDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_name, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                okAction.doAction();
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
        TextView titleTextView = view.findViewById(R.id.title_dialog_textView);
        titleTextView.setText(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    public void setAlgo(OkAction okAction) {
        this.okAction = okAction;
    }
}