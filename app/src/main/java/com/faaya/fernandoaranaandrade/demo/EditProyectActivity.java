package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.DateEnum;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.Fechas;
import com.faaya.fernandoaranaandrade.demo.utils.FechasBean;
import com.faaya.fernandoaranaandrade.demo.utils.FechasBean2;
import com.faaya.fernandoaranaandrade.demo.utils.FechasUtils;
import com.faaya.fernandoaranaandrade.demo.utils.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class EditProyectActivity extends AppCompatActivity {

    Spinner rageSpinner;
    EditText obraNameEditText;
    EditText timeEditText;
    Button startEditButton;
    TextView resultTextView, endTextView, timeTextView;
    private Queries queries;
    private Proyect proyect;
    Button buttonDelete, endProyectButton;
    ImageButton imageButton;
    private boolean stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queries = new Queries(this);
        Intent intent = getIntent();
        setProyect(intent);
        setContentView(R.layout.activity_edit_proyect);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        obraNameEditText = findViewById(R.id.obra_name_plain_text);
        resultTextView = findViewById(R.id.resultTextView);
        rageSpinner = findViewById(R.id.rage_spinner);
        endTextView = findViewById(R.id.endTextView);
        timeTextView = findViewById(R.id.textView5);
        imageButton = findViewById(R.id.imageButtonClean);
        rageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                fillResultData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
        endProyectButton = findViewById(R.id.buttonEndProyect);
        startEditButton = findViewById(R.id.buttonAddDate);
        timeEditText = findViewById(R.id.timeEditText);
        boolean isNew = isNew();
        fillSpinner();
        fillObraNameTitle(isNew);
        fillRage(isNew);
        fillStart(isNew);
        fillTime(isNew);
        fillEndTime(isNew);
        fillBorrado(isNew);
        if(isNew || proyect.getStart() == 0){
            setVisibilityDates(View.INVISIBLE);
        }
        timeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                proyect.setEndDate(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!stop) {
                    fillResultData();
                }
            }
        });
    }

    public void set_date(View view) {
        String textButton = null;
        if (!startEditButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
            textButton = startEditButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                cleanDate();
                startEditButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                setVisibilityDates(View.VISIBLE);
                fillResultData();
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    public void set_end_date(View view) {
        String textButton = null;
        if (!endProyectButton.getText().toString().equalsIgnoreCase(getString(R.string.setDate))) {
            textButton = endProyectButton.getText().toString();
        }
        DateDialogFragment dateDialogFragment = DateDialogFragment.newInstance(" ", textButton);
        dateDialogFragment.setOkActionDate(new OkActionDate() {
            @Override
            public void doAction(Calendar calendar) {
                endProyectButton.setText(DateEnum.dateSimpleDateFormat.format(calendar.getTime()));
                proyect.setEndDate(calendar.getTimeInMillis());
                fillResultData();
            }
        });
        dateDialogFragment.show(getSupportFragmentManager(), "fragment_edit_date");
    }

    private void fillBorrado(boolean isNew) {
        if (isNew) {
            buttonDelete = findViewById(R.id.buttonDelete);
            buttonDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void fillResultData() {
        stop = true;
        try {
            String statEditable = startEditButton.getText().toString();
            if (proyect.getEndDate() == null || proyect.getEndDate().longValue() == 0) {
                Editable timeEditable = timeEditText.getText();
                if (timeEditable != null && !timeEditable.toString().isEmpty() && isDate(statEditable)) {
                    int number = Integer.parseInt(timeEditable.toString());
                    String range = (String) rageSpinner.getSelectedItem();
                    Date startDate = DateEnum.dateSimpleDateFormat.parse(statEditable);
                    FechasBean fechaBean = FechasUtils.getFechasBean(startDate, number, range, StringUtils.getMap(this));
                    endProyectButton.setText(fechaBean.getEndDateText());
                    fillResultTextView(fechaBean);
                } else {
                    resultTextView.setText("");
                }
            } else {
                String endEditable = endProyectButton.getText().toString();
                if (isDate(statEditable) && isDate(endEditable)) {
                    Date startDate = DateEnum.dateSimpleDateFormat.parse(statEditable);
                    Date endDate = DateEnum.dateSimpleDateFormat.parse(endEditable);
                    String range = (String) rageSpinner.getSelectedItem();
                    FechasBean2 fechaBean = FechasUtils.getFechasBean(startDate, endDate, range, StringUtils.getMap(this));
                    timeEditText.setText(fechaBean.getCount().toString());
                    fillResultTextView(fechaBean);
                } else {
                    resultTextView.setText("");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            stop = false;
        }
    }

    private boolean isDate(String date) {
        return !date.equalsIgnoreCase(getString(R.string.setDate));
    }

    private void fillResultTextView(Fechas fechaBean) {
        if (fechaBean.getEndCalendar().getTime().before(new Date())) {
            resultTextView.setText(getString(R.string.tiempo_finalizado));
            resultTextView.setTextColor(Color.RED);
        } else {
            resultTextView.setText(fechaBean.getResultaTimeString());
            resultTextView.setTextColor(Color.GRAY);
        }
    }

    private void fillEndTime(boolean isNew) {
        if (isNew) {
            resultTextView.setText("");
        } else {
            if (proyect.getEndDate() != null && proyect.getEndDate().longValue() != 0) {
                String format = DateEnum.dateSimpleDateFormat.format(new Date(proyect.getEndDate()));
                endProyectButton.setText(format);
            }
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

    private void fillTime(boolean isNew) {
        if (!isNew && proyect.getStart() != 0) {
            timeEditText.setText(proyect.getTime().toString());
        }
    }

    private void fillStart(boolean isNew) {
        if (!isNew && proyect.getStart() != 0l) {
            String format = DateEnum.dateSimpleDateFormat.format(new Date(proyect.getStart()));
            startEditButton.setText(format);
        }
    }

    private void fillRage(boolean isNew) {
        if (isNew) {
            rageSpinner.setSelection(0);
        } else {
            if (proyect.getRange().equals(getString(R.string.DIAS))) {
                rageSpinner.setSelection(1);
            }
            if (proyect.getRange().equals(getString(R.string.SEMANAS))) {
                rageSpinner.setSelection(0);
            }
        }
    }

    private void fillObraNameTitle(boolean isNew) {
        if (isNew) {
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
        String[] rageValues = {getString(R.string.SEMANAS), getString(R.string.DIAS)};
        ArrayAdapter<String> rages = new ArrayAdapter<String>(this, R.layout.spinner24, rageValues);
        rageSpinner.setAdapter(rages);
    }

    public void save() {
        try {
            if (isCorrectData()) {
                fillProyectData();
                queries.saveOrUpdateProyect(proyect);
                showMessage(getString(R.string.proyectoGuardado));
                exit(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void fillProyectData() throws ParseException {
        if(timeEditText.getText() != null && !timeEditText.getText().toString().isEmpty()){
            proyect.setTime(Long.parseLong(timeEditText.getText().toString()));
        }
        String setDate = getString(R.string.setDate);
        if(!endProyectButton.getText().toString().equalsIgnoreCase(setDate)){
            proyect.setEndDate(DateEnum.dateSimpleDateFormat.parse(endProyectButton.getText().toString()).getTime());
        }
        if(!startEditButton.getText().toString().equalsIgnoreCase(setDate)){
            proyect.setStart(DateEnum.dateSimpleDateFormat.parse(startEditButton.getText().toString()).getTime());
        }
        proyect.setRange((String) rageSpinner.getSelectedItem());
        proyect.setName(obraNameEditText.getText().toString());

    }

    private boolean isCorrectData() {
        if (obraNameEditText.getText() == null || obraNameEditText.getText().toString().isEmpty()) {
            showMessage(getString(R.string.El_nombre_no_puede_estar_vacio));
            return false;
        }
        String setDate = getString(R.string.setDate);
        if (!startEditButton.getText().toString().equalsIgnoreCase(setDate)) {
            if (timeEditText.getText() == null || timeEditText.getText().toString().isEmpty()) {
                showMessage(getString(R.string.El_tiempo_no_puede_estar_vacio));
                return false;
            }
            if (endProyectButton.getText() == null || endProyectButton.getText().toString().equalsIgnoreCase(setDate)) {
                showMessage(getString(R.string.El_tiempo_no_puede_estar_vacio));
                return false;
            }
        }
        if (rageSpinner.getSelectedItem() == null) {
            showMessage(getString(R.string.El_rango_es_inválido));
            return false;
        }
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                exit(true);
            }
        });
        editNameDialogFragment.show(fm, "fragment_edit_proyect");
    }

    public void cleanDate(View view) {
        cleanDate();
    }

    private void cleanDate() {
        setVisibilityDates(View.INVISIBLE);
        String setDate = getString(R.string.setDate);
        endProyectButton.setText(setDate);
        startEditButton.setText(setDate);
        timeEditText.setText(null);
        proyect.setEndDate(null);
        proyect.setRange(null);
        proyect.setTime(null);
        proyect.setStart(null);
    }

    private void setVisibilityDates(int visibility) {
        endTextView.setVisibility(visibility);
        resultTextView.setVisibility(visibility);
        timeTextView.setVisibility(visibility);
        endProyectButton.setVisibility(visibility);
        timeEditText.setVisibility(visibility);
        rageSpinner.setVisibility(visibility);
        imageButton.setVisibility(visibility);
    }

    private void exit(boolean delete) {
        if (delete || isNew()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            Intent intent = getIntent();
            Intent newIntent = new Intent(this, TaskListProyectActivity.class);
            newIntent.putExtra(MainActivity.ID_PROYECT, proyect.getId());
            Serializable serializable = intent.getSerializableExtra(TaskListProyectActivity.FILTER_BEAN);
            if (serializable != null) {
                newIntent.putExtra(TaskListProyectActivity.FILTER_BEAN, serializable);
            }
            startActivity(newIntent);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                exit(false);
                return true;
            case R.id.saveTool:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_save, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        exit(false);
    }

}
