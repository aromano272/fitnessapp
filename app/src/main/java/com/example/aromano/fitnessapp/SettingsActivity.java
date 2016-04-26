package com.example.aromano.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup rg_goalstype;
    RadioButton rb_bycalories;
    RadioButton rb_bymacros;
    EditText et_calories;
    EditText et_protein;
    EditText et_carbs;
    EditText et_fats;
    EditText et_fiber;
    TextView tv_protein_type;
    TextView tv_carbs_type;
    TextView tv_fats_type;
    boolean byCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        rg_goalstype = (RadioGroup) findViewById(R.id.rg_goalstype);
        rb_bycalories = (RadioButton) findViewById(R.id.rb_bycalories);
        rb_bymacros = (RadioButton) findViewById(R.id.rb_bymacros);
        et_calories = (EditText) findViewById(R.id.et_calories);
        et_protein = (EditText) findViewById(R.id.et_protein);
        et_carbs = (EditText) findViewById(R.id.et_carbs);
        et_fats = (EditText) findViewById(R.id.et_fats);
        et_fiber = (EditText) findViewById(R.id.et_fiber);
        tv_protein_type = (TextView) findViewById(R.id.tv_protein_type);
        tv_carbs_type = (TextView) findViewById(R.id.tv_carbs_type);
        tv_fats_type = (TextView) findViewById(R.id.tv_fats_type);

        // init
        drawGoalsByCalories();

        rg_goalstype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(R.id.rb_bycalories == checkedId) {
                    Log.d("SetGoals", "Calories");
                    drawGoalsByCalories();
                } else {
                    Log.d("SetGoals", "Macros");
                    drawGoalsByMacros();
                }
            }
        });
    }

    private void drawGoalsByCalories() {
        byCalories = true;
        et_calories.setEnabled(true);
        tv_protein_type.setText("%");
        tv_carbs_type.setText("%");
        tv_fats_type.setText("%");
    }

    private void drawGoalsByMacros() {
        byCalories = false;
        et_calories.setEnabled(false);
        tv_protein_type.setText("g");
        tv_carbs_type.setText("g");
        tv_fats_type.setText("g");
    }

    private void saveSettings() {
        saveGoals();
    }

    private void saveGoals() {
        float calories;
        float protein = Float.parseFloat(et_protein.getText().toString());
        float carbs = Float.parseFloat(et_carbs.getText().toString());
        float fats = Float.parseFloat(et_fats.getText().toString());
        float fiber = Float.parseFloat(et_fiber.getText().toString());

        if(byCalories) {
            // protein, carbs, fats are percentages of those calories, / 100 so we can input 40% and it translates into * 0.4
            calories = Float.parseFloat(et_calories.getText().toString());
            protein = (calories * (protein / 100)) / 4;
            carbs = (calories * (carbs / 100)) / 4;
            fats = (calories * (fats / 100)) / 9;
        } else {
            calories = (protein * 4) + (carbs * 4) + (fats * 9);
        }

        float[] macros = { calories, protein, carbs, fats, fiber };

        Bundle extras = new Bundle();
        extras.putFloatArray("macros", macros);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("extras", extras);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_savesettings:
                saveSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
