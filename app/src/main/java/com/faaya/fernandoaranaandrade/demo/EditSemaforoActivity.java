package com.faaya.fernandoaranaandrade.demo;

import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;

public class EditSemaforoActivity extends SuperEditSemaforoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void save_semaforo(View view) {
        if (check()) {
            saveSpinner(spinnerBeforeAfterRed, SettingsEnum.RED_SEMAFORO, editTextRed);
            saveSpinner(spinnerBeforeAfterOrange, SettingsEnum.ORANGE_SEMAFORO, editTextOrange);
            saveSpinner(spinnerBeforeAfterYellow, SettingsEnum.YELLOW_SEMAFORO, editTextYellow);
            saveSpinner(spinnerBeforeAfterWhite, SettingsEnum.WHITE_SEMAFORO, editTextWhite);
            if (aSwitch.isChecked()) {
                queries.saveProperty(SettingsEnum.ACTIVE, SettingsEnum.ON.toString());
            } else {
                queries.saveProperty(SettingsEnum.ACTIVE, SettingsEnum.OFF.toString());
            }
            queries.saveProperty(SettingsEnum.REAL_SEMAFORO, (String) spinnerColorReal.getSelectedItem());
            Snackbar.make(view, "Ajustes guardados", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void saveSpinner(Spinner spinner, SettingsEnum semaforo, EditText editText) {
        String data = mapValues.get(spinner.getSelectedItem());
        queries.saveProperty(semaforo, editText.getText() + "-" + data);
    }

    protected void fillData() {
        String active = queries.getValueByProperty(SettingsEnum.ACTIVE);
        if (active != null && active.equals(SettingsEnum.ON.toString())) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        fillColor(SettingsEnum.WHITE_SEMAFORO, editTextWhite, spinnerBeforeAfterWhite);
        fillColor(SettingsEnum.YELLOW_SEMAFORO, editTextYellow, spinnerBeforeAfterYellow);
        fillColor(SettingsEnum.ORANGE_SEMAFORO, editTextOrange, spinnerBeforeAfterOrange);
        fillColor(SettingsEnum.RED_SEMAFORO, editTextRed, spinnerBeforeAfterRed);

        String realSemanforo = queries.getValueByProperty(SettingsEnum.REAL_SEMAFORO);
        for (int index = 0; index < colorValues.length; index++) {
            if(colorValues[index].equals(realSemanforo)){
                spinnerColorReal.setSelection(index);
            }
        }
    }

    private void fillColor(SettingsEnum colorSemaforo, EditText editText, Spinner spinner) {
        String valueByProperty = queries.getValueByProperty(colorSemaforo);
        String[] values = valueByProperty.split("-");
        editText.setText(values[0]);
        Integer index = getIndex(values[1]);
        if (index != null) {
            spinner.setSelection(index);
        }
    }
}
