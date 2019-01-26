package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;

public class EditSemaforoTaskActivity extends SuperEditSemaforoActivity {

    public static final String TASK = "TASK";

    private TaskApp taskApp;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageButton = findViewById(R.id.imageButtonSaveSemaforo);
        imageButton.setImageResource(R.drawable.ic_keyboard_backspace);
    }

    public void save_semaforo(View view) {
        if (check()) {
            taskApp.setRedSemaforo(getText(spinnerBeforeAfterRed,  editTextRed));
            taskApp.setOrangeSemaforo(getText(spinnerBeforeAfterOrange,  editTextOrange));
            taskApp.setYellowSemaforo(getText(spinnerBeforeAfterYellow,  editTextYellow));
            taskApp.setWhiteSemaforo(getText(spinnerBeforeAfterWhite, editTextWhite));
            taskApp.setRealSemaforo((String) spinnerColorReal.getSelectedItem());
            if (aSwitch.isChecked()) {
                taskApp.setActiveSemaforo(SettingsEnum.ON.toString());
            } else {
                taskApp.setActiveSemaforo(SettingsEnum.OFF.toString());
            }
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra(TASK, taskApp);
            startActivity(intent);
        }
    }

    private String getText(Spinner spinner, EditText editText) {
        return editText.getText() + "-" + mapValues.get(spinner.getSelectedItem());
    }

    protected void fillData() {
        taskApp = (TaskApp) getIntent().getSerializableExtra(TASK);
        String active = taskApp.getActiveSemaforo();
        if (active != null && active.equals(SettingsEnum.ON.toString())) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        fillColor(taskApp.getWhiteSemaforo(), editTextWhite, spinnerBeforeAfterWhite);
        fillColor(taskApp.getYellowSemaforo(), editTextYellow, spinnerBeforeAfterYellow);
        fillColor(taskApp.getOrangeSemaforo(), editTextOrange, spinnerBeforeAfterOrange);
        fillColor(taskApp.getRedSemaforo(), editTextRed, spinnerBeforeAfterRed);
        for (int index = 0; index < colorValues.length; index++) {
            if(colorValues[index].equals(taskApp.getRealSemaforo())){
                spinnerColorReal.setSelection(index);
            }
        }
    }

    private void fillColor(String valueByProperty, EditText editText, Spinner spinner) {
        String[] values = valueByProperty.split("-");
        editText.setText(values[0]);
        Integer index = getIndex(values[1]);
        if (index != null) {
            spinner.setSelection(index);
        }
    }
}
