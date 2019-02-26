package com.faaya.fernandoaranaandrade.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import com.faaya.fernandoaranaandrade.demo.Beans.AllTareasBean;
import com.faaya.fernandoaranaandrade.demo.Beans.Proyect;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskApp;
import com.faaya.fernandoaranaandrade.demo.Beans.TaskType;
import com.faaya.fernandoaranaandrade.demo.adapters.TaskAppAdapter;
import com.faaya.fernandoaranaandrade.demo.database.Queries;
import com.faaya.fernandoaranaandrade.demo.utils.FechasUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AllTasksActivity extends AppCompatActivity {
    public static final String FILTER = "FILTER";
    private AllTareasBean allTareasBean;
    ListView listView;
    private Queries queries;
    private String searchText;
    Spinner spinnerFilterOrder;
    List<TaskApp> taskToday = new ArrayList();

    class C03543 implements OnItemSelectedListener {
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        C03543() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            AllTasksActivity.this.allTareasBean.setOrderBy((String) AllTasksActivity.this.spinnerFilterOrder.getSelectedItem());
            AllTasksActivity.this.filter();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.add_all_task);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.queries = new Queries(this);
        fill();
        load();
    }

    private void load() {
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

        this.spinnerFilterOrder = (Spinner) findViewById(R.id.spinnerFilterOrder);
        String[] orderByValues = new String[8];
        orderByValues[0] = getString(R.string.creationUp);
        orderByValues[1] = getString(R.string.creationDown);
        orderByValues[2] = getString(R.string.tagUp);
        orderByValues[3] = getString(R.string.tagDown);
        orderByValues[4] = getString(R.string.dateUp);
        orderByValues[5] = getString(R.string.dateDown);
        orderByValues[6] = getString(R.string.finish_order);
        orderByValues[7] = getString(R.string.finish_order_Down);
        this.spinnerFilterOrder.setAdapter(new ArrayAdapter(this, R.layout.spinner18_bond, orderByValues));
        this.spinnerFilterOrder.setOnItemSelectedListener(new C03543());
        int i = 0;
        if (this.allTareasBean.getOrderBy() != null) {
            while (i < orderByValues.length) {
                if (orderByValues[i].equals(this.allTareasBean.getOrderBy())) {
                    this.spinnerFilterOrder.setSelection(i);
                    break;
                }
                i++;
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditTask(view, allProyects, typesValues);
            }
        });
    }

    private void fill() {
        Serializable serializableExtra = getIntent().getSerializableExtra(FILTER);
        if (serializableExtra == null) {
            this.allTareasBean = new AllTareasBean();
            List<TaskType> allTaskTypes = this.queries.getAllTaskTypes();
            this.allTareasBean.setCheckedBefore(allTaskTypes);
            List arrayList = new ArrayList();
            arrayList.addAll(allTaskTypes);
            this.allTareasBean.setAllTaskType(arrayList);
            this.allTareasBean.setCheckedCurrent(new ArrayList());
            this.allTareasBean.getCheckedCurrent().addAll(this.allTareasBean.getCheckedBefore());
            List allProyects = this.queries.getAllProyects();
            this.allTareasBean.setProjectCheckedBefore(allProyects);
            this.allTareasBean.setAllProjects(allProyects);
            this.allTareasBean.setProjectsCheckedCurrent(new ArrayList());
            this.allTareasBean.getProjectsCheckedCurrent().addAll(this.allTareasBean.getProjectCheckedBefore());
            return;
        }
        this.allTareasBean = (AllTareasBean) serializableExtra;
    }

    private void filter() {
        Boolean unFinishTask = null;
        Long valueOf = this.allTareasBean.getStartDate() != null ? Long.valueOf(FechasUtils.getCalendarStart(this.allTareasBean.getStartDate()).getTimeInMillis()) : null;
        Long valueOf2 = this.allTareasBean.getEndDate() != null ? Long.valueOf(FechasUtils.getCalendarEnd(this.allTareasBean.getEndDate()).getTimeInMillis()) : null;
        if (this.allTareasBean.getUnfinish() != null) {
            if (this.allTareasBean.getUnfinish().equalsIgnoreCase(getString(R.string.done))) {
                unFinishTask = Boolean.valueOf(false);
            }
            if (this.allTareasBean.getUnfinish().equalsIgnoreCase(getString(R.string.ToDo))) {
                unFinishTask = Boolean.valueOf(true);
            }
        }
        fillProyectSpinnerWithData(this.queries.selectTaskByIdProyectEndDateAndType(allTareasBean.getProjectsCheckedCurrent(), valueOf, valueOf2, this.allTareasBean.getCheckedCurrent(), unFinishTask, this.allTareasBean.getOrderBy(), this.searchText));
    }

    private void fillProyectSpinnerWithData(List<TaskApp> list) {
        this.taskToday.clear();
        this.taskToday.addAll(list);
        this.listView.setAdapter(new TaskAppAdapter(this, list, false));
        showMessage(String.format(getString(R.string.hayTareas), new Object[]{Integer.valueOf(this.taskToday.size())}));
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
        if (id != null) {
            intent.putExtra(MainActivity.ID_TASK, id);
        }
        intent.putExtra(EditTaskActivity.FROM_ACTIVITY, this.getClass().getName());
        intent.putExtra(FILTER, this.allTareasBean);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.filterAllTaskSearchTool:
                search();
                return true;
            case R.id.filterAllTaskFilterTool:
                Intent newIntent = new Intent(this, FilterAllTaskActivity.class);
                newIntent.putExtra(FILTER, this.allTareasBean);
                startActivity(newIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
