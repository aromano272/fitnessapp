package com.example.aromano.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by aRomano on 01/04/2016.
 */
public class DBManager2 extends SQLiteOpenHelper {
    private static DBManager2 sInstance;

    // ...

    public static synchronized DBManager2 getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBManager2(context.getApplicationContext());
        }
        return sInstance;
    }

    private static final int wakeupTime = 8;

    private static final int DATABASE_VERSION = 20;
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
    // tb_portion
    public static final String tb_portion = "tb_portion";
    public static final String col_portion_idportion = "idportion";
    public static final String col_portion_serving = "serving";
    public static final String col_portion_idingredient = col_ingredient_idingredient;
    public static final String const_portion_fktb_portiontb_ingredient = "constraint fktb_portiontb_ingredient foreign key ("
            + col_portion_idingredient + ") references " + tb_ingredient + " (" +
            col_ingredient_idingredient + ") on update cascade on delete cascade";
    // tb_diary
    public static final String tb_diary = "tb_diary";
    public static final String col_diary_iddiary = "iddiary";
    public static final String col_diary_date = "date";
    public static final String col_diary_idportion = col_portion_idportion;
    public static final String const_diary_fktb_diarytb_portion = "constraint fktb_diarytb_portion foreign key ("
            + col_diary_idportion + ") references " + tb_portion + " (" +
            col_portion_idportion + ") on update cascade on delete cascade";
    // tb_goals
    public static final String tb_goals = "tb_goals";
    public static final String col_goals_idgoals = "idgoals";
    public static final String col_goals_calories = "calories";
    public static final String col_goals_protein = "protein";
    public static final String col_goals_carbs = "carbs";
    public static final String col_goals_fats = "fats";
    public static final String col_goals_fiber = "fiber";

    public DBManager2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "";
        String create_tb_ingredient = "create table " + tb_ingredient + " (" +
                col_ingredient_idingredient + " int primary key," +
                col_ingredient_name + " varchar(20)," +
                col_ingredient_calories + " float," +
                col_ingredient_protein + " float," +
                col_ingredient_carbs + " float," +
                col_ingredient_fats + " float," +
                col_ingredient_fiber + " float" +
                ");";
        String create_tb_portion = "create table " + tb_portion + " (" +
                col_portion_idportion + " int primary key," +
                col_portion_serving + " float," +
                col_portion_idingredient + " int," +
                const_portion_fktb_portiontb_ingredient +
                ");";
        String create_tb_diary = "create table " + tb_diary + " (" +
                col_diary_iddiary + " int primary key," +
                col_diary_idportion + " int," +
                col_diary_date + " datetime default current_timestamp," +
                const_diary_fktb_diarytb_portion +
                ");";
        String create_tb_goals = "create table " + tb_goals + " (" +
                col_goals_idgoals + " int primary key," +
                col_goals_calories + " float," +
                col_goals_protein + " float," +
                col_goals_carbs + " float," +
                col_goals_fats + " float," +
                col_goals_fiber + " float" +
                ");";


        String init_tb_goals = "insert or replace into " + tb_goals + " values(1,0,0,0,0,0)";

        query = create_tb_ingredient + create_tb_portion + create_tb_diary + create_tb_goals;
        try {
            db.execSQL(create_tb_ingredient);
            db.execSQL(create_tb_portion);
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
        db.execSQL("drop table if exists " + tb_portion);
        db.execSQL("drop table if exists " + tb_diary);
        db.execSQL("drop table if exists " + tb_goals);

        Log.d("DBManager2", "onUpgrade() called");

        onCreate(db);
    }

    // Get today diary macros
    public float[] getTodayDiaryMacros() {
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

        float[] macros = new float[5];

        SQLiteDatabase db = getWritableDatabase();
        String query = "select " +
                "round(calories / serving, 1) as 'calories'," +
                "round(protein / serving, 1) as 'protein'," +
                "round(carbs / serving, 1) as 'carbs'," +
                "round(fats / serving, 1) as 'fats'," +
                "round(fiber / serving, 1) as 'fiber' from tb_diary " +
            "inner join tb_portion on tb_diary.idportion = tb_portion.idportion " +
                // TODO    change _id to idingredient
            "inner join tb_ingredient on tb_portion._id = tb_ingredient._id;";

        try {
            Cursor c = db.rawQuery(query, null);
            while(c.moveToNext()) {
                macros[0] = c.getFloat(0);
                macros[1] = c.getFloat(1);
                macros[2] = c.getFloat(2);
                macros[3] = c.getFloat(3);
                macros[4] = c.getFloat(4);
            }
        } catch (Exception e) {

        } finally {
            db.close();
        }

        return macros;
    }

    // Get today diary entries
    public List<Portion> getTodayDiaryEntries() {
        List<Portion> entries = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        //String query = "select * from " + tb_goals + " where 1";
        String query = "select * from " + tb_diary + " where 1";

        try {
            Cursor c = db.rawQuery(query, null);
            while (c.moveToNext()) {
                int idportion = c.getInt(c.getColumnIndex(col_diary_idportion));
                Portion portion = Food.Portions.get(idportion - 1);
                entries.add(portion);
            }
        } catch (Exception ex) {

        } finally {
            db.close();
        }

        return entries;
    }

    // Add diary entry
    public void addDiaryEntry(Portion portion) {
        ContentValues cv = new ContentValues();

        cv.putNull(col_diary_iddiary);
        cv.put(col_diary_idportion, portion.getId());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(tb_diary, null, cv);
        db.close();
    }

    // Delete diary entry
    public void deleteDiaryEntry(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(tb_diary, col_diary_iddiary+"="+id, null);
    }

    // Get portions
    public void getPortions() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_portion + " where 1";

        try {
            Cursor c = db.rawQuery(query, null);
            while(c.moveToNext()) {
                int idportion = c.getInt(c.getColumnIndex(col_portion_idportion));
                float serving = c.getFloat(c.getColumnIndex(col_portion_serving));
                int idingredient = c.getInt(c.getColumnIndex(col_portion_idingredient));


                new Portion(idportion, serving, Food.Ingredients.get(idingredient - 1));
            }
        } catch (Exception ex) {

        } finally {
            db.close();
        }
    }

    // Get goals
    public float[] getGoals() {
        SQLiteDatabase db = getWritableDatabase();
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
        } catch (Exception ex) {

        } finally {
            db.close();
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

        SQLiteDatabase db = getWritableDatabase();
        db.update(tb_goals, cv, col_goals_idgoals+"=1", null);
    }

    // Add ingredient
    public void addIngredient(Ingredient ingredient) {
        ContentValues values = new ContentValues();

        values.putNull(col_ingredient_idingredient);
        values.put(col_ingredient_name, ingredient.getName());
        values.put(col_ingredient_calories, ingredient.getCalories());
        values.put(col_ingredient_protein, ingredient.getProtein());
        values.put(col_ingredient_carbs, ingredient.getCarbs());
        values.put(col_ingredient_fats, ingredient.getFats());
        values.put(col_ingredient_fiber, ingredient.getFiber());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(tb_ingredient, null, values);
        db.close();
    }

    // Delete ingredient
    public void deleteIngredient(Ingredient ingredient) {
        //int id = ingredient.getId();

        SQLiteDatabase db = getWritableDatabase();
        //db.execSQL("delete from " + tb_ingredient + " where " + col_ingredient_idingredient + "=\"" + id + "\";");
    }

    // convert back to object
    public List<Ingredient> getIngredients() {
        List<Ingredient> entries = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_ingredient + " where 1";

        try {
            Cursor c = db.rawQuery(query, null);
            while(c.moveToNext()) {
                int idingredient = c.getInt(c.getColumnIndex(col_ingredient_idingredient));
                String name = c.getString(c.getColumnIndex(col_ingredient_name));
                float calories = c.getFloat(c.getColumnIndex(col_ingredient_calories));
                float protein = c.getFloat(c.getColumnIndex(col_ingredient_protein));
                float carbs = c.getFloat(c.getColumnIndex(col_ingredient_carbs));
                float fats = c.getFloat(c.getColumnIndex(col_ingredient_fats));
                float fiber = c.getFloat(c.getColumnIndex(col_ingredient_fiber));

                Ingredient ingredient = new Ingredient(idingredient, name, calories, protein, carbs, fats, fiber);
                entries.add(ingredient);
            }
        } catch (Exception ex) {

        } finally {
            db.close();
        }

        return entries;
    }

    public Cursor getIngredientsCursor() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "select * from " + tb_ingredient + " where 1";
        Cursor cursor;
        cursor = db.rawQuery(query, null);
        //db.close();

        return cursor;
    }

}
