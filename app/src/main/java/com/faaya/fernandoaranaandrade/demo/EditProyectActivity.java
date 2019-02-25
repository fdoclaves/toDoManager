package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskEnum;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.FechasBean;
import com.faaya.fernandoaranaandrade.demo.utils.FechasUtils;
import com.faaya.fernandoaranaandrade.demo.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProyectActivity extends AppCompatActivity {

    public static final String DATE_REGEX = "^([0-2][0-9]|3[0-1])\\/(0[0-9]|1[0-2])\\/([0-9][0-9])?[0-9][0-9]$";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Spinner rageSpinner;
    EditText obraNameEditText;
    EditText timeEditText;
    Button startEditButton;
    TextView resultTextView;
    TextView endTextView;
    private Queries queries;
    private Proyect proyect;
    ImageButton deleteImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queries = new Queries(this);
        Intent intent = getIntent();
        setProyect(intent);
        setContentView(R.layout.activity_edit_proyect);
        obraNameEditText = findViewById(R.id.obra_name_plain_text);
        resultTextView = findViewById(R.id.resultTextView);
        rageSpinner = findViewById(R.id.rage_spinner);
        fillSpinner();
        startEditButton = findViewById(R.id.buttonAddDate);
        timeEditText = findViewById(R.id.timeEditText);
        endTextView = findViewById(R.id.endTextView);
        timeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                fillResultData();
            }
        });
        fillObraNameTitle();
        fillRage();
        fillStart();
        fillTime();
        fillEndTime();
        fillBorrado();
    }

    public void set_date(View view) {
        String textButton = null;
        if(startEditButton.getText().toString().matches(DATE_REGEX)){
            textButton = startEditButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                startEditButton.setText(simpleDateFormat.format(calendar.getTime()));
                fillResultData();
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    private void fillBorrado() {
        if(isNew()){
            deleteImageButton = findViewById(R.id.deleteImageButton);
            deleteImageButton.setVisibility(View.INVISIBLE);
        }
    }

    private void fillResultData() {
        try {
            Editable timeEditable = timeEditText.getText();
            String statEditable = startEditButton.getText().toString();
            if (timeEditable != null && !timeEditable.toString().isEmpty() && statEditable != null && statEditable.matches(DATE_REGEX)) {
                String timeString = timeEditable.toString();
                FechasBean fechaBean = FechasUtils.getFechasBean(simpleDateFormat.parse(statEditable), Integer.parseInt(timeString), (String) rageSpinner.getSelectedItem(), StringUtils.getMap(this));
                endTextView.setText(fechaBean.getEndDateText());
                resultTextView.setText(fechaBean.getResultaTimeString());
                if(fechaBean.getEndCalendar().getTime().before(new Date())){
                    resultTextView.setTextColor(Color.RED);
                    resultTextView.setText(getString(R.string.tiempo_finalizado));
                } else {
                    resultTextView.setTextColor(Color.GRAY);
                }
            } else {
                endTextView.setText(getString(R.string.fecha_final) + "--/--/----");
                resultTextView.setText("");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void fillEndTime() {
        if (isNew()) {
            endTextView.setText("");
            resultTextView.setText("-");
        } else {
            fillResultData();
        }
    }

    private void setProyect(Intent intent) {
        Long id = intent.getLongExtra(MainActivity.ID_PROYECT, 0);
        if (id == 0) {
            proyect = new Proyect();
        } else {
            proyect = queries.getByIdProyect(id);
        }
    }

    private void fillTime() {
        if (!isNew()) {
            timeEditText.setText(proyect.getTime().toString());
        }
    }

    private void fillStart() {
        if (!isNew()) {
            String format = simpleDateFormat.format(new Date(proyect.getStart()));
            startEditButton.setText(format);
        }
    }

    private void fillRage() {
        if (isNew()) {
            rageSpinner.setSelection(0);
        } else {
            if(proyect.getRange().equals(getString(R.string.MESES))){
                rageSpinner.setSelection(2);
            }
            if(proyect.getRange().equals(getString(R.string.DIAS))){
                rageSpinner.setSelection(1);
            }
            if(proyect.getRange().equals(getString(R.string.SEMANAS))){
                rageSpinner.setSelection(0);
            }
        }
    }

    private void fillObraNameTitle() {
        if (isNew()) {
            obraNameEditText.setText("");
        } else {
            obraNameEditText.setText(proyect.getName());
        }
        obraNameEditText.requestFocus();
    }

    private boolean isNew() {
        return proyect.getId() == null || proyect.getId() == 0;
    }

    private void fillSpinner() {
        String[] rageValues = {getString(R.string.SEMANAS), getString(R.string.DIAS), getString(R.string.MESES)};
        ArrayAdapter<String> rages = new ArrayAdapter<String>(this, R.layout.spinner24, rageValues);
        rageSpinner.setAdapter(rages);
        rageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillResultData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    public void save(View view) {
        try {
            if (isCorrectData()) {
                fillProyectData();
                queries.saveOrUpdateProyect(proyect);
                showMessage(getString(R.string.proyectoGuardado));
                exit();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void fillProyectData() throws ParseException {
        proyect.setRange((String) rageSpinner.getSelectedItem());
        proyect.setTime(Long.parseLong(timeEditText.getText().toString()));
        proyect.setName(obraNameEditText.getText().toString());
        Date date = simpleDateFormat.parse(startEditButton.getText().toString());
        proyect.setStart(date.getTime());
    }

    private boolean isCorrectData() {
        if (rageSpinner.getSelectedItem() == null) {
            showMessage(getString(R.string.El_rango_es_inválido));
            return false;
        }
        if (startEditButton.getText() == null || startEditButton.getText().toString().isEmpty()) {
            showMessage(getString(R.string.La_fecha_novpuede_estar_vacia));
            return false;
        }
        if (obraNameEditText.getText() == null || obraNameEditText.getText().toString().isEmpty()) {
            showMessage(getString(R.string.El_nombre_no_puede_estar_vacio));
            return false;
        }
        if (timeEditText.getText() == null || timeEditText.getText().toString().isEmpty()) {
            showMessage(getString(R.string.El_tiempo_no_puede_estar_vacio));
            return false;
        }
        if (!isValidDate()) {
            showMessage(getString(R.string.No_se_ha_configurado_la_fecha_de_inicio));
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidDate() {
        return startEditButton.getText().toString().matches(DATE_REGEX);
    }

    public void delete(View view) {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(getString(R.string.Se_eliminarán_todos_las_tareas_relacioandas_a_este_proyecto));
        editNameDialogFragment.setOkAction(new OkAction() {
            @Override
            public void doAction() {
                queries.deleteNotificationsByIdProyect(proyect.getId());
                queries.deleteProyect(proyect.getId());
                queries.deleteTasksByIdProyect(proyect.getId());
                exit();
            }
        });
        editNameDialogFragment.show(fm, "fragment_edit_proyect");
    }

    private void exit() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void add_task_edit(View view) throws ParseException {
        if(isNew()){
            if (isCorrectData()) {
                fillProyectData();
                proyect = queries.saveProyect(proyect);
                showMessage(getString(R.string.proyectoGuardado));
            } else {
                return;
            }
        }
        Intent intent = new Intent(this, AllTasksActivity.class);
        intent.putExtra(TaskEnum.ID_PROYECT.toString(), proyect.getId());
        startActivity(intent);
    }
}
