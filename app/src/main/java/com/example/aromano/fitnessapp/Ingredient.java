package com.example.aromano.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

/**
 * Created by aRomano on 01/04/2016.
 */
public class Ingredient extends Food {
    // sql stuff
    //private static int id_count = 0;
    private int id;

    public int getId() {
        return this.id;
    }

    // +++++++++++++++++

    Ingredient(int id, String name, float calories, float protein, float carbs, float fats, float fiber) {
        this.id = id;
        this.setName(name);
        this.setCalories(calories);
        this.setProtein(protein);
        this.setCarbs(carbs);
        this.setFats(fats);
        this.setFiber(fiber);

        //this.calcCalories();

        Food.Ingredients.add(this);
    }
/*
    public void update(String name, float servings, float protein, float carbs, float fats, float fiber) {
        this.setName(name);
        this.setProtein(protein / servings);
        this.setCarbs(carbs / servings);
        this.setFats(fats / servings);
        this.setFiber(fiber / servings);

        this.calcCalories();
    }

    public void delete() {
        Food.Ingredients.remove(this);
    }

    //private void calcCalories() {
    //    float proteinCals = this.getProtein();
    //    float carbsCals = this.getCarbs();
    //    float fatsCals = this.getFats();
    //    float fiberCals = this.getFiber();
    //    this.calories = proteinCals + carbsCals + fatsCals + fiberCals;
    //}


    // DEBUG
    Ingredient(String name) {
        this.id = ++id_count;
        this.setName(name);
        this.setProtein(25);
        this.setCarbs(25);
        this.setFats(10);
        this.setFiber(2.5f);

        this.calcCalories();

        Food.Ingredients.add(this);
    }*/
}
