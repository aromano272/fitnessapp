package com.example.aromano.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by aRomano on 01/04/2016.
 */
public class Ingredient extends Food {
    Ingredient(String name, float calories, float protein, float carbs, float fats, float fiber) {
        this.setName(name);
        this.setCalories(calories);
        this.setProtein(protein);
        this.setCarbs(carbs);
        this.setFats(fats);
        this.setFiber(fiber);
    }

}
