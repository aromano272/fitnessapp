package com.example.aromano.fitnessapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class AddFoodActivity extends AppCompatActivity {
    EditText et_name;
    EditText et_servings;
    EditText et_calories;
    EditText et_protein;
    EditText et_carbs;
    EditText et_fats;
    EditText et_fiber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        et_name = (EditText) findViewById(R.id.et_name);
        et_servings = (EditText) findViewById(R.id.et_servings);
        et_calories = (EditText) findViewById(R.id.et_calories);
        et_protein = (EditText) findViewById(R.id.et_protein);
        et_carbs = (EditText) findViewById(R.id.et_carbs);
        et_fats = (EditText) findViewById(R.id.et_fats);
        et_fiber = (EditText) findViewById(R.id.et_fiber);
    }

    private void addFood() {
        String name = et_name.getText().toString();
        float[] macros = { 0,0,0,0,0 };
        boolean[] givenMacros = { false,false,false,false,false };

        String servings_str = et_servings.getText().toString();
        String calories_str = et_calories.getText().toString();
        String protein_str = et_protein.getText().toString();
        String carbs_str = et_carbs.getText().toString();
        String fats_str = et_fats.getText().toString();
        String fiber_str = et_fiber.getText().toString();

        // default value for servings if omitted
        float servings = 1;

        if(!servings_str.isEmpty()) {
            servings = Float.parseFloat(servings_str);
        }
        if(!calories_str.isEmpty()) {
            macros[0] = Float.parseFloat(calories_str) / servings;
            givenMacros[0] = true;
        }
        if(!protein_str.isEmpty()) {
            macros[1] = Float.parseFloat(protein_str) / servings;
            givenMacros[1] = true;
        }
        if(!carbs_str.isEmpty()) {
            macros[2] = Float.parseFloat(carbs_str) / servings;
            givenMacros[2] = true;
        }
        if(!fats_str.isEmpty()) {
            macros[3] = Float.parseFloat(fats_str) / servings;
            givenMacros[3] = true;
        }
        if(!fiber_str.isEmpty()) {
            macros[4] = Float.parseFloat(fiber_str) / servings;
            givenMacros[4] = true;
        }

        Bundle extras = new Bundle();
        extras.putString("name", name);
        extras.putFloatArray("macros", macros);
        extras.putBooleanArray("givenMacros", givenMacros);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("extras", extras);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addfood_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_savefood:
                addFood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
