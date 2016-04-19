package com.example.aromano.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class  MainActivity extends AppCompatActivity {
    TextView tv_remainingcalories;
    ListView lv_food;
    ListView lv_consumed;
    DBManager db = new DBManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view2);

        tv_remainingcalories = (TextView) findViewById(R.id.tv_remainingcalories);
        lv_food = (ListView) findViewById(R.id.lv_food);
        lv_consumed = (ListView) findViewById(R.id.lv_consumed);

        populateDiaryList();
        populateFoodList();
        populateRemainingMacros();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    // TODO: extend the functionality to display full macros
    private void populateRemainingMacros() {
        float[] goals = db.getGoals();
        Cursor cursor = db.getTodayDiaryEntries();
        float calories = goals[0];
        float protein = goals[1];
        float carbs = goals[2];
        float fats = goals[3];
        float fiber = goals[4];

        while(cursor.moveToNext()) {
            calories -= cursor.getFloat(cursor.getColumnIndex("calories"));
            protein -= cursor.getFloat(cursor.getColumnIndex("protein"));
            carbs -= cursor.getFloat(cursor.getColumnIndex("carbs"));
            fats -= cursor.getFloat(cursor.getColumnIndex("fats"));
            fiber -= cursor.getFloat(cursor.getColumnIndex("fiber"));
        }
        cursor.close();

        tv_remainingcalories.setText(String.valueOf(calories));
    }


    CursorAdapter foodAdapter;

    private void populateFoodList() {
        foodAdapter = new FoodAdapter(this, db.getIngredients(), 0);
        lv_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // TODO: fix the double click sound when adding item, maybe 2 onClicks are being triggered
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedView = view.getId();
                // a view enviado pelo performItemClick eh a view do btn_add, logo temos que ir ao parent para conseguirmos aceder ao et_servings
                // de cada listitem, utilizando o parent passado no onItemClick nao conseguimos la chegar
                View parentView = (View) view.getParent();
                EditText et_servings = (EditText) parentView.findViewById(R.id.et_servings);
                Cursor cursor = (Cursor) lv_food.getItemAtPosition(position);


                if(clickedView == R.id.btn_add) {
                    // get the data from the id sent from performItemClick(), ugly but works
                    //float servings = ((float) id) / 100;
                    float servings = Float.parseFloat(et_servings.getText().toString());
                    Log.d("et_servings", String.valueOf(servings));

                    db.addDiaryEntry(servings, cursor.getInt(cursor.getColumnIndex("_id")));
                }

                populateDiaryList();
                populateRemainingMacros();
            }
        });
        lv_food.setAdapter(foodAdapter);
    }

    CursorAdapter diaryAdapter;

    private void populateDiaryList() {
        diaryAdapter = new DiaryAdapter(this, db.getTodayDiaryEntries(), 0);
        lv_consumed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) lv_consumed.getItemAtPosition(position);
                db.deleteDiaryEntry(cursor.getInt(cursor.getColumnIndex("_id")));

                populateDiaryList();
                populateRemainingMacros();
            }
        });
        lv_consumed.setAdapter(diaryAdapter);
    }


    private void launchSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    private void launchAddFoodActivity() {
        Intent intent = new Intent(this, AddFoodActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                float[] macros = extras.getFloatArray("macros");

                db.updateGoals(macros);
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                String name = extras.getString("name");
                float[] macros = extras.getFloatArray("macros");
                boolean[] givenMacros = extras.getBooleanArray("givenMacros");

                db.addIngredient(name, macros, givenMacros);

                populateFoodList();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addFood:
                launchAddFoodActivity();
                return true;
            case R.id.menu_settings:
                launchSettingsActivity();
                return true;
            case R.id.menu_debug_PrintSQLTables:
                debug_printSQLTables();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // debug
    public void debug_printSQLTables() {
        Cursor cursorIngredients = db.getIngredients();
        Cursor cursorDiary = db.getTodayDiaryEntries();

        db.debug_printTable(cursorDiary);
        db.debug_printTable(cursorIngredients);
    }
}
