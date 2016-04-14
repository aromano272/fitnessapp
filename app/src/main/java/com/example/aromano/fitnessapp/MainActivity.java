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
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class  MainActivity extends AppCompatActivity {
    final CalorieCalculator calorieCalculator = new CalorieCalculator();
    TextView et_totalkcal;
    ListView lv_food;
    ListView lv_consumed;
    DBManager db = new DBManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view2);

        et_totalkcal = (TextView) findViewById(R.id.tv_totalkcal);
        lv_food = (ListView) findViewById(R.id.lv_food);
        lv_consumed = (ListView) findViewById(R.id.lv_consumed);

        float remainingCalories = calorieCalculator.getRemainingCalories();

        et_totalkcal.setText(String.valueOf(remainingCalories));
        populateDiaryList();
        populateFoodList();

        db.getIngredients();
        float[] goals = db.getGoals();
        calorieCalculator.Goals.updateGoals(goals[0], goals[1], goals[2], goals[3], goals[4], false);

    }

    @Override
    protected void onStart() {
        super.onStart();

        //updateRemainingCalories();
    }

    private void updateRemainingCalories(float[] consumed) {
        float[] macros = db.getGoals();

        float remainingCalories = macros[0] - consumed[0];

        et_totalkcal.setText(String.valueOf(remainingCalories));
    }


    CursorAdapter foodAdapter;

    private void populateFoodList() {
        foodAdapter = new FoodAdapter(this, db.getIngredientsCursor(), 0);
        lv_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) lv_food.getItemAtPosition(position);
                db.addDiaryEntry(1, cursor.getInt(cursor.getColumnIndex("_id")));

                Log.d("onitemclick", String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                //((CursorAdapter) lv_food.getAdapter()).notifyDataSetChanged();
                populateDiaryList();
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

                //Log.d("onitemclick", String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                //((CursorAdapter) lv_consumed.getAdapter()).notifyDataSetChanged();
                //updateRemainingCalories();
                populateDiaryList();
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
                //updateRemainingCalories();
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                String name = extras.getString("name");
                float[] macros = extras.getFloatArray("macros");

                Ingredient ingredient = new Ingredient(Food.Ingredients.size(), name, macros[0], macros[1], macros[2], macros[3], macros[4]);

                db.addIngredient(ingredient);

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
        Cursor cursorIngredients = db.getIngredientsCursor();
        Cursor cursorDiary = db.getTodayDiaryEntries();

        db.debug_printTable(cursorDiary);
        db.debug_printTable(cursorIngredients);
    }
}
