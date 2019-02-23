package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class SearchDialogFragment extends DialogFragment {

    public static final String TITLE = "TITLE";
    private OkActionSearch okAction;
    private Button okButton;
    private Button cancelButton;

    public SearchDialogFragment() {

    }

    public static SearchDialogFragment newInstance(String title) {
        SearchDialogFragment frag = new SearchDialogFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText editText = view.findViewById(R.id.editTextSearch);
                okAction.doAction(editText.getText().toString());
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

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SCREEN_BRIGHTNESS_CHANGED);
    }

    public void setOkAction(OkActionSearch okAction) {
        this.okAction = okAction;
    }
}