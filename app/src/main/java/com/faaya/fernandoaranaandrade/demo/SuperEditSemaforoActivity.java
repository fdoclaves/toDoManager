package com.faaya.fernandoaranaandrade.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.SettingsEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.HashMap;
import java.util.Map;

public abstract class SuperEditSemaforoActivity extends AppCompatActivity {

    Spinner spinnerBeforeAfterWhite;
    Spinner spinnerBeforeAfterOrange;
    Spinner spinnerBeforeAfterRed;
    Spinner spinnerBeforeAfterYellow;
    Spinner spinnerColorReal;
    Spinner spinnerColorUnfinish;
    EditText editTextWhite;
    EditText editTextYellow;
    EditText editTextOrange;
    EditText editTextRed;
    Queries queries;
    Map<String, Integer> mapIndex;
    Map<String, String> mapValues;
    Switch aSwitch;
    String[] colorValues, colorUnfinishValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_semaforo);
        queries = new Queries(this);
        mapIndex = new HashMap<String, Integer>();
        mapIndex.put(getString(R.string.daysBefore), 0);
        mapIndex.put(getString(R.string.weekBefore), 1);
        mapIndex.put(getString(R.string.mouthBefore), 2);
        mapIndex.put(getString(R.string.daysAfter), 3);
        mapIndex.put(getString(R.string.weekAfter), 4);
        mapIndex.put(getString(R.string.mouthAfter), 5);

        mapValues = new HashMap<String, String>();
        mapValues.put(getString(R.string.daysBefore), "DB");
        mapValues.put(getString(R.string.weekBefore), "WB");
        mapValues.put(getString(R.string.mouthBefore), "MB");
        mapValues.put(getString(R.string.daysAfter), "DA");
        mapValues.put(getString(R.string.weekAfter), "WA");
        mapValues.put(getString(R.string.mouthAfter), "MA");

        String[] timeValues = new String[6];
        timeValues[0] = getString(R.string.daysBefore);
        timeValues[1] = getString(R.string.weekBefore);
        timeValues[2] = getString(R.string.mouthBefore);
        timeValues[3] = getString(R.string.daysAfter);
        timeValues[4] = getString(R.string.weekAfter);
        timeValues[5] = getString(R.string.mouthAfter);

        editTextWhite = findViewById(R.id.editTextWhite);
        editTextYellow = findViewById(R.id.editTextYellow);
        editTextOrange = findViewById(R.id.editTextOrange);
        editTextRed = findViewById(R.id.editTextRed);
        aSwitch = findViewById(R.id.switch1);
        spinnerColorUnfinish = findViewById(R.id.spinnerColorUnfinish);

        spinnerBeforeAfterWhite = findViewById(R.id.spinnerBeforeAfterWhite);
        spinnerBeforeAfterOrange = findViewById(R.id.spinnerBeforeAfterOrange);
        spinnerBeforeAfterRed = findViewById(R.id.spinnerBeforeAfterRed);
        spinnerBeforeAfterYellow = findViewById(R.id.spinnerBeforeAfterYellow);
        spinnerColorReal = findViewById(R.id.spinnerColorReal);

        spinnerBeforeAfterWhite.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, timeValues));
        spinnerBeforeAfterOrange.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, timeValues));
        spinnerBeforeAfterRed.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, timeValues));
        spinnerBeforeAfterYellow.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, timeValues));

        colorValues = new String[2];
        colorValues[0] = getString(R.string.green);
        colorValues[1] = getString(R.string.white);
        spinnerColorReal.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, colorValues));
        colorUnfinishValues = new String[3];
        colorUnfinishValues[2] = getString(R.string.green);
        colorUnfinishValues[1] = getString(R.string.white);
        colorUnfinishValues[0] = getString(R.string.red);
        spinnerColorUnfinish.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner18, colorUnfinishValues));
        fillData();
    }

    public abstract void save_semaforo(View view);

    private void saveSpinner(Spinner spinner, SettingsEnum semaforo, EditText editText) {
        String data = mapValues.get(spinner.getSelectedItem());
        queries.saveProperty(semaforo, editText.getText() + "-" + data);
    }

    protected boolean check() {
        if (editTextRed.getText() == null || editTextRed.getText().toString().isEmpty()) {
            showMessage(getString(R.string.falta_la_cantidad_para_el_color) + " " + getString(R.string.red));
            return false;
        }
        if (editTextOrange.getText() == null || editTextOrange.getText().toString().isEmpty()) {
            showMessage(getString(R.string.falta_la_cantidad_para_el_color) + " " + getString(R.string.orange));
            return false;
        }
        if (editTextYellow.getText() == null || editTextYellow.getText().toString().isEmpty()) {
            showMessage(getString(R.string.falta_la_cantidad_para_el_color) + " " + getString(R.string.yellow));
            return false;
        }
        if (editTextWhite.getText() == null || editTextWhite.getText().toString().isEmpty()) {
            showMessage(getString(R.string.falta_la_cantidad_para_el_color) + " " + getString(R.string.green));
            return false;
        }
        return true;
    }

    private void showMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected abstract void fillData();

    protected Integer getIndex(String value) {
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            if (entry.getValue().equals(value)) {
                return mapIndex.get(entry.getKey());
            }
        }
        return null;
    }
}
