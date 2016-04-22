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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class  MainActivity extends AppCompatActivity {
    private static int RESULT_UPDATE_GOALS = 1;
    private static int RESULT_ADD_INGREDIENT = 2;
    private static int RESULT_EDIT_INGREDIENT = 3;

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

        db.

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

        lv_food.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) lv_food.getItemAtPosition(position);
                launchEditFoodActivity(cursor.getInt(cursor.getColumnIndex("_id")));
                return true;
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
        startActivityForResult(intent, RESULT_UPDATE_GOALS);
    }

    private void launchAddFoodActivity() {
        Intent intent = new Intent(this, AddFoodActivity.class);
        startActivityForResult(intent, RESULT_ADD_INGREDIENT);
    }

    private void launchAddFoodActivity(String barcodeformat, String barcodecontent) {
        Intent intent = new Intent(this, AddFoodActivity.class);
        Bundle extras = new Bundle();
        extras.putString("barcodeformat", barcodeformat);
        extras.putString("barcodecontent", barcodecontent);
        intent.putExtra("extras", extras);
        startActivityForResult(intent, RESULT_ADD_INGREDIENT);
    }

    private void launchEditFoodActivity(int idingredient) {
        Intent intent = new Intent(this, EditFoodActivity.class);
        Bundle extras = new Bundle();

        Cursor cursor = db.getIngredients(idingredient);

        try {
            float macros[] = new float[5];
            cursor.moveToNext();
            extras.putInt("_id", idingredient);
            // TODO: fix null values in the table not being sanitized
            extras.putString("name", cursor.getString(cursor.getColumnIndex("name")));
            macros[0] = cursor.getFloat(cursor.getColumnIndex("calories"));
            macros[1] = cursor.getFloat(cursor.getColumnIndex("protein"));
            macros[2] = cursor.getFloat(cursor.getColumnIndex("carbs"));
            macros[3] = cursor.getFloat(cursor.getColumnIndex("fats"));
            macros[4] = cursor.getFloat(cursor.getColumnIndex("fiber"));

            extras.putFloatArray("macros", macros);
            // TODO: use putExtras which dont require a key
            intent.putExtra("extras", extras);
            startActivityForResult(intent, RESULT_EDIT_INGREDIENT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
    }

    private void launchScanBarcodeActivity() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void handleBarcodeScanResult(IntentResult scanResult) {
        String barcodeformat = scanResult.getFormatName();
        String barcodecontent = scanResult.getContents();
        Cursor cursor = db.getIngredients(barcodeformat, barcodecontent);

        if(cursor.moveToNext()) {
            Log.d("barcode found", "true");
            // ingredient found, add to diary
            int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            // TODO: show a sort of dialog of popup with the ingredient before adding

            float servings = 0;
            db.addDiaryEntry(servings, _id);

        } else {
            // not found, prompt to create a new one
            // TODO: ask if user wants to add new ingredient or update an existing one
            launchAddFoodActivity(barcodeformat, barcodecontent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_UPDATE_GOALS) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                float[] macros = extras.getFloatArray("macros");

                db.updateGoals(macros);
            }
        } else if (requestCode == RESULT_ADD_INGREDIENT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                String name = extras.getString("name");
                float[] macros = extras.getFloatArray("macros");
                boolean[] givenMacros = extras.getBooleanArray("givenMacros");
                String barcodeformat = extras.getString("barcodeformat");
                String barcodecontent = extras.getString("barcodecontent");

                db.addIngredient(name, macros, givenMacros, barcodeformat, barcodecontent);

                populateFoodList();
            }
        } else if (requestCode == RESULT_EDIT_INGREDIENT) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getBundleExtra("extras");
                int _id = extras.getInt("_id");
                String name = extras.getString("name");
                float[] macros = extras.getFloatArray("macros");
                boolean[] givenMacros = extras.getBooleanArray("givenMacros");

                db.updateIngredient(_id, name, macros, givenMacros);

                populateFoodList();
                // TODO: remainingmacros only updating if populatediarylist is called
                //populateDiaryList();

                populateRemainingMacros();
            } else if(resultCode == 2 && data != null) {
                db.deleteIngredient(data.getIntExtra("_id", -1));
                populateFoodList();
                populateDiaryList();
                populateRemainingMacros();
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if(scanResult != null) {
                    handleBarcodeScanResult(scanResult);
                }
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
            case R.id.menu_scan_barcode:
                launchScanBarcodeActivity();
                return true;
            case R.id.menu_add_food:
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
