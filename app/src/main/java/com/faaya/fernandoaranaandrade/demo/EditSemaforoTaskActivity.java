package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;

import java.io.Serializable;

public class EditSemaforoTaskActivity extends SuperEditSemaforoActivity {

    public static final String TASK = "TASK";

    private TaskApp taskApp;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageButton = findViewById(R.id.imageButtonSaveSemaforo);
        imageButton.setImageResource(R.drawable.ic_keyboard_backspace);
        imageButton.setVisibility(View.INVISIBLE);
    }

    public void save_semaforo(View view) {
        saveData();
    }

    private void saveData() {
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
            exit();
        }
    }

    private void exit() {
        Intent newIntent = new Intent(this, EditTaskActivity.class);
        newIntent.putExtra(TASK, taskApp);
        Serializable serializable = getIntent().getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
        newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
        fillIBackIntent(newIntent, getIntent());
        startActivity(newIntent);
        finish();
    }

    private void fillIBackIntent(final Intent newIntent, final Intent currentIntent) {
        Long idTipoCurrentIntent = currentIntent.getLongExtra(TaskEnum.ID_TYPE.toString(), 0);
        String rangoCurrentIntent = currentIntent.getStringExtra(TaskEnum.RANGO_TIEMPO.toString());
        Long idProyectCurrentIntent = currentIntent.getLongExtra(TaskEnum.ID_PROYECT.toString(), 0);
        if (idTipoCurrentIntent != null && idTipoCurrentIntent.longValue() != 0) {
            newIntent.putExtra(TaskEnum.ID_TYPE.toString(), idTipoCurrentIntent);
        }
        if (rangoCurrentIntent != null && !rangoCurrentIntent.isEmpty()) {
            newIntent.putExtra(TaskEnum.RANGO_TIEMPO.toString(), rangoCurrentIntent);
        }
        if (idProyectCurrentIntent != 0) {
            newIntent.putExtra(TaskEnum.ID_PROYECT.toString(), idProyectCurrentIntent);
        }
        String fromActivity = currentIntent.getStringExtra(EditTaskActivity.FROM_ACTIVITY);
        if(fromActivity != null){
            newIntent.putExtra(EditTaskActivity.FROM_ACTIVITY, fromActivity);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
