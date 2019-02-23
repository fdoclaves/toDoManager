package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.database.Queries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AllTasksActivity extends AppCompatActivity {

    Spinner spinnerFilterOrder;

    private Queries queries;

    ListView listView;

    List<TaskApp> taskToday = new ArrayList<>();

    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_all_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        load();
    }

    private void load() {
        queries = new Queries(this);
        listView = findViewById(R.id.list_all_task);
        final List<TaskType> typesValues = new ArrayList<TaskType>();
        typesValues.add(new TaskType(getString(R.string.TODO)));
        typesValues.addAll(queries.getAllTaskTypes());
        final List<Proyect> allProyects = new ArrayList<Proyect>();
        allProyects.add(new Proyect(getString(R.string.TODOS)));
        allProyects.addAll(queries.getAllProyects());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToEditActivity(taskToday.get(position).getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                StringBuffer title = new StringBuffer(getString(R.string.eliminar_definitivamente));
                title.append(" \"" + taskToday.get(position).getName()+"\"");
                EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance(title.toString());
                editNameDialogFragment.setOkAction(new OkAction() {
                    @Override
                    public void doAction() {
                        Long idTask = taskToday.get(position).getId();
                        queries.deleteTask(idTask);
                        queries.deleteNotificationByIdTask(idTask);
                        filter();
                        showMessage(getString(R.string.task_delete));
                    }
                });
                editNameDialogFragment.show(fm, "fragment_edit_name");
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditTask(view, allProyects, typesValues);
            }
        });
    }

    private void filter() {
        Long idProyect = null;
        Long startRange = null;
        Long endRange = null;
        Long idTaskType = null;
        fillProyectSpinnerWithData(queries.selectTaskByIdProyectEndDateAndType(idProyect, startRange, endRange, idTaskType, null, searchText));
    }

    @NonNull
    private Calendar getCalendarToday() {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);
        calendarStart.set(Calendar.MILLISECOND, 0);
        return calendarStart;
    }

    private void fillProyectSpinnerWithData(List<TaskApp> taskApps) {
        taskToday.clear();
        taskToday.addAll(taskApps);
        listView.setAdapter(new TaskAppAdapter(this, taskApps, false));
        String message = getString(R.string.hayTareas);
        showMessage(String.format(message, taskToday.size()));
    }

    private void goToEditTask(View view, List<Proyect> allProyects, List<TaskType> typesValues){
        if(allProyects.size() <= 1){
            Snackbar.make(view, getString(R.string.youNeedToCreateAProjectBefore), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        if(typesValues.size() <= 1){
            Snackbar.make(view, getString(R.string.youNeedToCreateAHaveCategories), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }
        goToEditActivity((Long)null);
    }

    private void goToEditActivity(Long id) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        if(id != null){
            intent.putExtra(MainActivity.ID_TASK, id);
        }
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        startActivity(intent);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        filter();
    }

    private void search() {
        SearchDialogFragment dialogFragment = SearchDialogFragment.newInstance(" ");
        dialogFragment.setOkAction(new OkActionSearch() {
            @Override
            public void doAction(String text) {
                if(text.isEmpty()){
                    searchText = null;
                } else {
                    searchText = text;
                }
                System.out.println("Buscar:" + searchText);
                filter();
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "fragment_search");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_all_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.filterAllTaskSearchTool:
                search();
                return true;
            case R.id.filterAllTaskFilterTool:
                filter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
