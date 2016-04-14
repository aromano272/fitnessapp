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
        float servings = Float.parseFloat(et_servings.getText().toString());
        float calories = Float.parseFloat(et_calories.getText().toString()) / servings;
        float protein = Float.parseFloat(et_protein.getText().toString()) / servings;
        float carbs = Float.parseFloat(et_carbs.getText().toString()) / servings;
        float fats = Float.parseFloat(et_fats.getText().toString()) / servings;
        float fiber = Float.parseFloat(et_fiber.getText().toString()) / servings;

        float[] macros = { calories, protein, carbs, fats, fiber };

        Bundle extras = new Bundle();
        extras.putString("name", name);
        extras.putFloatArray("macros", macros);

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
