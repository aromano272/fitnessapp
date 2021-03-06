package com.example.aromano.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Exchanger;

/**
 * Created by aRomano on 01/04/2016.
 */
public class DBManager extends SQLiteOpenHelper {
    private static DBManager instance;
    private static SQLiteDatabase db;

    public static synchronized DBManager getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DBManager(context.getApplicationContext());
        }
        return instance;
    }

    private static final int wakeupTime = 8;

    private static final int DATABASE_VERSION = 36;
    private static final String DATABASE_NAME = "fitnessapp.db";
    // tb_ingredient
    public static final String tb_ingredient = "tb_ingredient";
    public static final String col_ingredient_idingredient = "_id";
    public static final String col_ingredient_name = "name";
    public static final String col_ingredient_calories = "calories";
    public static final String col_ingredient_protein = "protein";
    public static final String col_ingredient_carbs = "carbs";
    public static final String col_ingredient_fats = "fats";
    public static final String col_ingredient_fiber = "fiber";
    public static final String col_ingredient_barcodeformat = "barcodeformat";
    public static final String col_ingredient_barcodecontent = "barcodecontent";
    public static final String col_ingredient_count = "count";

    // tb_diary
    public static final String tb_diary = "tb_diary";
    public static final String col_diary_iddiary = "_id";
    public static final String col_diary_date = "date";
    public static final String col_diary_servings = "servings";
    public static final String col_diary_idingredient = "idingredient";
    public static final String const_diary_fktb_diarytb_ingredient = "constraint fktb_diarytb_ingredient foreign key ("
            + col_diary_idingredient + ") references " + tb_ingredient + " (" +
            col_ingredient_idingredient + ") on update cascade on delete cascade";
    // tb_goals
    public static final String tb_goals = "tb_goals";
    public static final String col_goals_idgoals = "_id";
    public static final String col_goals_calories = "calories";
    public static final String col_goals_protein = "protein";
    public static final String col_goals_carbs = "carbs";
    public static final String col_goals_fats = "fats";
    public static final String col_goals_fiber = "fiber";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_tb_ingredient = "create table " + tb_ingredient + " (" +
                col_ingredient_idingredient + " integer primary key," +
                col_ingredient_name + " varchar(20)," +
                col_ingredient_calories + " float," +
                col_ingredient_protein + " float," +
                col_ingredient_carbs + " float," +
                col_ingredient_fats + " float," +
                col_ingredient_fiber + " float," +
                col_ingredient_barcodeformat + " varchar(20)," +
                col_ingredient_barcodecontent + " varchar(20) unique," +
                col_ingredient_count + " int not null default 0" +
                ");";
        String create_tb_diary = "create table " + tb_diary + " (" +
                col_diary_iddiary + " integer primary key," +
                col_diary_date + " datetime default current_timestamp," +
                col_diary_servings + " float," +
                col_diary_idingredient + " integer," +
                const_diary_fktb_diarytb_ingredient +
                ");";
        String create_tb_goals = "create table " + tb_goals + " (" +
                col_goals_idgoals + " integer primary key," +
                col_goals_calories + " float," +
                col_goals_protein + " float," +
                col_goals_carbs + " float," +
                col_goals_fats + " float," +
                col_goals_fiber + " float" +
                ");";


        String init_tb_goals = "insert or replace into " + tb_goals + " values(null,0,0,0,0,0)";

        try {
            db.execSQL(create_tb_ingredient);
            db.execSQL(create_tb_diary);
            db.execSQL(create_tb_goals);

            db.execSQL(init_tb_goals);
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + tb_ingredient);
        db.execSQL("drop table if exists " + tb_diary);
        db.execSQL("drop table if exists " + tb_goals);

        Log.d("DBManager", "onUpgrade() called");

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /*                 this is needed if we plan to support versions prior to 4.1
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // apparently the fucking foreign keys are disabled by default on sqlite herp derp...
        // so that explains why delete wasnt cascading
        // http://stackoverflow.com/questions/2545558/foreign-key-constraints-in-android-using-sqlite-on-delete-cascade
        // starting from 4.1 we can enable foreignkeys by default on onConfigure
        if(!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
    */

    // TODO: change this so it accepts a date as a parameter, and displays that day's diary
    // Get today diary macros
    public Cursor getTodayDiaryEntries() {
        String targetDate, targetHour;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // we check if its already past the wakeup time of the current day
        // if it is, the target day is gonna be today, if its not its gonna be yesterday at 8am
        if(calendar.get(Calendar.HOUR_OF_DAY) > wakeupTime) {
            targetDate = sdf.format(calendar.getTime());
        } else {
            // goes back to yesterday
            calendar.add(calendar.DATE, -1);
            targetDate = sdf.format(calendar.getTime());
        }

        // adding the 0 in front in case wakeup time its smaller than 10, so it fits the dateformat
        if(wakeupTime >= 10) {
            targetHour = String.valueOf(wakeupTime);
        } else {
            targetHour = "0" + wakeupTime;
        }

        String targetTimestamp = targetDate + " " + targetHour + ":00:00";

        //SQLiteDatabase db = getWritableDatabase();
        String query = "select " +
                "tb_diary._id as '_id'," +
                "name as 'ingredientname'," +
                "date," +
                "servings," +
                "round(calories * servings, 1) as 'calories'," +
                "round(protein * servings, 1) as 'protein'," +
                "round(carbs * servings, 1) as 'carbs'," +
                "round(fats * servings, 1) as 'fats'," +
                "round(fiber * servings, 1) as 'fiber' from tb_diary " +
                "inner join tb_ingredient on tb_diary.idingredient = tb_ingredient._id " +
                "where date > '" + targetTimestamp + "' order by _id desc;";

        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    // Add diary entry
    public void addDiaryEntry(float servings, int idingredient) {
        ContentValues cv = new ContentValues();

        cv.putNull(col_diary_iddiary);
        //cv.put(col_diary_date, "default");
        cv.put(col_diary_servings, servings);
        cv.put(col_diary_idingredient, idingredient);

        //SQLiteDatabase db = getWritableDatabase();
        db.insert(tb_diary, null, cv);
        increaseIngredientCount(idingredient);

        //db.close();
    }

    // Delete diary entry
    public void deleteDiaryEntry(int _id) {
        //SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(String.format("select idingredient from %s where _id = %s;", tb_diary, _id), null);

        while(c.moveToNext()) {
            int idingredient = c.getInt(c.getColumnIndex("idingredient"));
            decreaseIngredientCount(idingredient);
        }
        c.close();

        db.delete(tb_diary, col_diary_iddiary+"="+_id, null);
        //db.close();
    }

    public void increaseIngredientCount(int idingredient) {
        try {
            db.execSQL(String.format("update %s set %s = %s + 1 where _id = %s",
                    tb_ingredient, col_ingredient_count, col_ingredient_count, idingredient));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decreaseIngredientCount(int idingredient) {
        try {
            db.execSQL(String.format("update %s set %s = %s - 1 where _id = %s",
                    tb_ingredient, col_ingredient_count, col_ingredient_count, idingredient));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Get goals
    public float[] getGoals() {
        //SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_goals + " where 1";

        float[] macros = new float[5];
        try {
            Cursor c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                float calories = c.getFloat(c.getColumnIndex(col_goals_calories));
                float protein = c.getFloat(c.getColumnIndex(col_goals_protein));
                float carbs = c.getFloat(c.getColumnIndex(col_goals_carbs));
                float fats = c.getFloat(c.getColumnIndex(col_goals_fats));
                float fiber = c.getFloat(c.getColumnIndex(col_goals_fiber));

                macros[0] = calories;
                macros[1] = protein;
                macros[2] = carbs;
                macros[3] = fats;
                macros[4] = fiber;
            }
            c.close();
        } catch (Exception ex) {

        } finally {
            //db.close();
        }

        return macros;
    }

    // Update goals
    public void updateGoals(float[] macros) {
        ContentValues cv = new ContentValues();
        cv.put(col_goals_calories, macros[0]);
        cv.put(col_goals_protein, macros[1]);
        cv.put(col_goals_carbs, macros[2]);
        cv.put(col_goals_fats, macros[3]);
        cv.put(col_goals_fiber, macros[4]);

        //SQLiteDatabase db = getWritableDatabase();
        db.update(tb_goals, cv, col_goals_idgoals+"=1", null);
        //db.close();
    }

    // TODO: allow null entries on all the methods
    // Add ingredient
    public void addIngredient(String name, float[] macros, boolean[] givenMacros, String barcodeformat, String barcodecontent) {
        ContentValues cv = new ContentValues();

        cv.putNull(col_ingredient_idingredient);
        cv.put(col_ingredient_name, name);

        if(givenMacros[0]) {
            cv.put(col_goals_calories, macros[0]);
        } else {
            cv.putNull(col_goals_calories);
        }
        if(givenMacros[1]) {
            cv.put(col_goals_protein, macros[1]);
        } else {
            cv.putNull(col_goals_protein);
        }
        if(givenMacros[2]) {
            cv.put(col_goals_carbs, macros[2]);
        } else {
            cv.putNull(col_goals_carbs);
        }
        if(givenMacros[3]) {
            cv.put(col_goals_fats, macros[3]);
        } else {
            cv.putNull(col_goals_fats);
        }
        if(givenMacros[4]) {
            cv.put(col_goals_fiber, macros[4]);
        } else {
            cv.putNull(col_goals_fiber);
        }
        if(barcodeformat.isEmpty()) {
            cv.putNull(col_ingredient_barcodeformat);
            Log.d("barcodeformat", "putting null");
        } else{
            cv.put(col_ingredient_barcodeformat, barcodeformat);
            Log.d("barcodeformat", "putting value: " + barcodeformat);
        }
        if(barcodecontent.isEmpty()) {
            cv.putNull(col_ingredient_barcodecontent);
            Log.d("barcodecontent", "putting null");
        } else {
            cv.put(col_ingredient_barcodecontent, barcodecontent);
            Log.d("barcodeformat", "putting value: " + barcodecontent);
        }

        //SQLiteDatabase db = getWritableDatabase();
        db.insert(tb_ingredient, null, cv);
        //db.close();
    }

    public Cursor getIngredients() {
        //SQLiteDatabase db = getWritableDatabase();
        String query = String.format("select * from %s where 1 order by count desc;", tb_ingredient);
        Cursor cursor;
        cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getIngredients(int _id) {
        //SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_ingredient + " where " + col_ingredient_idingredient + " = " + _id + "";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor getIngredients(String barcodeformat, String barcodecontent) {
        //SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_ingredient + " where " + col_ingredient_barcodeformat + " = '" + barcodeformat +
                "' and " + col_ingredient_barcodecontent + " = '" + barcodecontent + "';";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void updateIngredient(int _id, String name, float[] macros, boolean[] givenMacros) {
        //SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(col_ingredient_name, name);

        if(givenMacros[0]) {
            cv.put(col_goals_calories, macros[0]);
        } else {
            cv.putNull(col_goals_calories);
        }
        if(givenMacros[1]) {
            cv.put(col_goals_protein, macros[1]);
        } else {
            cv.putNull(col_goals_protein);
        }
        if(givenMacros[2]) {
            cv.put(col_goals_carbs, macros[2]);
        } else {
            cv.putNull(col_goals_carbs);
        }
        if(givenMacros[3]) {
            cv.put(col_goals_fats, macros[3]);
        } else {
            cv.putNull(col_goals_fats);
        }
        if(givenMacros[4]) {
            cv.put(col_goals_fiber, macros[4]);
        } else {
            cv.putNull(col_goals_fiber);
        }

        db.update(tb_ingredient, cv, col_ingredient_idingredient+ "=" +_id, null);
        //db.close();
    }


    public void deleteIngredient(int _id) {
        //SQLiteDatabase db = getWritableDatabase();
        db.delete(tb_ingredient, col_ingredient_idingredient+"="+ _id, null);
        //db.close();
    }


    // DEBUG
    public void debug_printTable(Cursor cursor) {
        try {
            while(cursor.moveToNext()) {
                String row = "";
                for(int i = 0; i < cursor.getColumnCount(); i++) {
                    if(cursor.getType(i) == Cursor.FIELD_TYPE_FLOAT) {
                        //Log.d(cursor.getColumnName(i), String.valueOf(cursor.getFloat(i)));
                        row += cursor.getColumnName(i) + ": " + String.valueOf(cursor.getFloat(i));
                    } else if(cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER){
                        //Log.d(cursor.getColumnName(i), String.valueOf(cursor.getInt(i)));
                        row += cursor.getColumnName(i) + ": " + String.valueOf(cursor.getInt(i));
                    } else {
                        //Log.d(cursor.getColumnName(i), cursor.getString(i));
                        row += cursor.getColumnName(i) + ": " + cursor.getString(i);
                    }
                    row += " | ";
                }
                Log.d("Debug", row);
            }
        } catch (Exception e) {
            Log.e("SQL", e.toString());
        } finally {
            cursor.close();
        }
    }

}
