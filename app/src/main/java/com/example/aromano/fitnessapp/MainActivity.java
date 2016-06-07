package com.example.aromano.fitnessapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class  MainActivity extends AppCompatActivity {
    private static int RESULT_UPDATE_GOALS = 1;
    private static int RESULT_ADD_INGREDIENT = 2;
    private static int RESULT_EDIT_INGREDIENT = 3;



    TextView tv_remainingcalories;
    TextView tv_remainingprotein;
    TextView tv_remainingcarbs;
    TextView tv_remainingfats;
    TextView tv_remainingfiber;
    ListView lv_food;
    ListView lv_consumed;
    DBManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view2);

        tv_remainingcalories = (TextView) findViewById(R.id.tv_remainingcalories);
        tv_remainingprotein = (TextView) findViewById(R.id.tv_remainingprotein);
        tv_remainingcarbs = (TextView) findViewById(R.id.tv_remainingcarbs);
        tv_remainingfats = (TextView) findViewById(R.id.tv_remainingfats);
        tv_remainingfiber = (TextView) findViewById(R.id.tv_remainingfiber);
        lv_food = (ListView) findViewById(R.id.lv_food);
        lv_consumed = (ListView) findViewById(R.id.lv_consumed);

        db = DBManager.getInstance(this);

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

        tv_remainingcalories.setText(String.valueOf((int)calories));
        tv_remainingprotein.setText(String.valueOf((int)protein));
        tv_remainingcarbs.setText(String.valueOf((int)carbs));
        tv_remainingfats.setText(String.valueOf((int)fats));
        tv_remainingfiber.setText(String.valueOf((int)fiber));
    }


    CursorAdapter foodAdapter;

    private void populateFoodList() {
        if(foodAdapter == null) {
            foodAdapter = new FoodAdapter(this, db.getIngredients(), 0);
            lv_food.setAdapter(foodAdapter);
        }
        foodAdapter.changeCursor(db.getIngredients());

        lv_food.setSoundEffectsEnabled(false);
        lv_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // TODO: fix the double click sound when adding item, maybe 2 onClicks are being triggered
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedView = view.getId();
                // the view sent by performItemClick is the btn_add view, there for we have to go to the parent to be
                // able to access the et_servings of each listitem, using parent argument from onItemClick doesnt seem to work
                View parentView = (View) view.getParent();
                EditText et_servings = (EditText) parentView.findViewById(R.id.et_servings);
                Cursor cursor = (Cursor) lv_food.getItemAtPosition(position);


                if(clickedView == R.id.btn_add) {
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


    }

    CursorAdapter diaryAdapter;

    // TODO: make it possible to edit diary entry
    private void populateDiaryList() {
        if(diaryAdapter == null) {
            diaryAdapter = new DiaryAdapter(this, db.getTodayDiaryEntries(), 0);
            lv_consumed.setAdapter(diaryAdapter);
        }
        // i use changeCursor so whenever i want to refresh the list i request a new cursor and delete the old one with changeCursor
        // another option would have been to make the DBManager return a List instead of a cursor, that way i could close the cursor on DBManager itself
        diaryAdapter.changeCursor(db.getTodayDiaryEntries());
        // TODO: add dialog to confirm diary delete

        // disables soundeffects triggered by performItemClick on the adapter, leaving only the dialog buttons to make sound
        lv_consumed.setSoundEffectsEnabled(false);
        lv_consumed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //populateDiaryList();
                populateRemainingMacros();

            }
        });

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
            final int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            // TODO: show a sort of dialog of popup with the ingredient before adding

            //DialogFragment newFrag = new CustomDialogs.selectServings();
            //newFrag.show(getFragmentManager(), "frag1");
            LayoutInflater inflater = this.getLayoutInflater();
            View layout = inflater.inflate(R.layout.dialog_select_servings, null);
            final TextView tv_decreaseServings = (TextView) layout.findViewById(R.id.tv_decreaseServings);
            final TextView tv_increaseServings = (TextView) layout.findViewById(R.id.tv_increaseServings);
            final EditText et_servings = (EditText) layout.findViewById(R.id.et_servings);

            tv_decreaseServings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double servings = Double.parseDouble(et_servings.getText().toString());
                    servings -= 0.05d;

                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_EVEN);

                    et_servings.setText(df.format(servings));
                }
            });

            tv_decreaseServings.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    double servings = Double.parseDouble(et_servings.getText().toString());
                    servings -= 1d;

                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_EVEN);

                    et_servings.setText(df.format(servings));
                    return false;
                }
            });

            tv_increaseServings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double servings = Double.parseDouble(et_servings.getText().toString());
                    servings += 0.05d;

                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_EVEN);

                    et_servings.setText(df.format(servings));
                }
            });

            tv_increaseServings.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    double servings = Double.parseDouble(et_servings.getText().toString());
                    servings += 1d;

                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.HALF_EVEN);

                    et_servings.setText(df.format(servings));
                    return false;
                }
            });

            AlertDialog alert = new AlertDialog.Builder(this)
            .setTitle("Add to diary: " + cursor.getString(cursor.getColumnIndex("name")))
            .setView(layout)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    float servings = Float.parseFloat(et_servings.getText().toString());
                    db.addDiaryEntry(servings, _id);

                    populateDiaryList();
                    populateRemainingMacros();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // dont delete
                    dialog.dismiss();
                }
            }).create();
            alert.show();
        } else {
            // not found, prompt to create a new one
            // TODO: ask if user wants to add new ingredient or update an existing one
            launchAddFoodActivity(barcodeformat, barcodecontent);
        }
        cursor.close();
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
        SQLiteDatabase debug_db = db.getWritableDatabase();
        Cursor cursorIngredients = debug_db.rawQuery("select * from tb_ingredient", null);
        Cursor cursorDiary = debug_db.rawQuery("select * from tb_diary order by _id desc", null);
        Cursor cursorGoals = debug_db.rawQuery("select * from tb_goals", null);

        db.debug_printTable(cursorIngredients);
        db.debug_printTable(cursorDiary);
        db.debug_printTable(cursorGoals);
    }
}
