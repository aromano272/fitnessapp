package com.example.aromano.fitnessapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class EditFoodActivity extends AppCompatActivity {
    EditText et_name;
    EditText et_servings;
    EditText et_calories;
    EditText et_protein;
    EditText et_carbs;
    EditText et_fats;
    EditText et_fiber;
    int _id;

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

        Intent intent = this.getIntent();
        Bundle extras = intent.getBundleExtra("extras");
        _id = extras.getInt("_id");
        float[] macros = extras.getFloatArray("macros");

        // TODO: add a way for change in orientation to keep the inserted data instead of fetching the starting one instead
        et_name.setText(extras.getString("name"));
        et_servings.setText("1");
        et_calories.setText(String.valueOf(macros[0]));
        et_protein.setText(String.valueOf(macros[1]));
        et_carbs.setText(String.valueOf(macros[2]));
        et_fats.setText(String.valueOf(macros[3]));
        et_fiber.setText(String.valueOf(macros[4]));
    }

    private void deleteFood() {
        // TODO: add confirmation before deleting
        AlertDialog dialog = new AlertDialog.Builder(this)
            .setTitle("Delete ingredient.")
            .setMessage("Deleting an ingredient will delete all of that item's diary entries.")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("_id", _id);
                    setResult(2, returnIntent);
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // dont delete
                    dialog.dismiss();
                }
            }).create();

        dialog.show();
    }

    private void editFood() {
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
        extras.putInt("_id", _id);
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
        inflater.inflate(R.menu.edit_food_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deletefood:
                deleteFood();
                return true;
            case R.id.menu_savefood:
                editFood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
