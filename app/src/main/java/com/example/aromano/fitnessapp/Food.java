package com.example.aromano.fitnessapp;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aRomano on 01/04/2016.
 */
abstract public class Food {
    static ArrayList<Ingredient> Ingredients = new ArrayList<>();
    static ArrayList<Dish> Dishes = new ArrayList<>();
    static ArrayList<Meal> Meals = new ArrayList<>();
    static ArrayList<Portion> Portions = new ArrayList<>();

    static List<Portion> Diary = new ArrayList<>();

    private static float kcalInProtein = 4;
    private static float kcalInCarbs = 4;
    private static float kcalInFats = 9;
    private static float kcalInFiber = 0;

    private String name;
    private float calories;
    private float protein;
    private float carbs;
    private float fats;
    private float fiber;

    public ArrayList<Portion> contents = new ArrayList<>();

    // update ingredient
    public void update(String name, float protein, float carbs, float fats, float fiber) {
        this.setName(name);
        this.setProtein(protein);
        this.setCarbs(carbs);
        this.setFats(fats);
        this.setFiber(fiber);
    }

    public void update(String name, Portion... portions) {
        this.contents.clear();
        this.setName(name);
        for(Portion portion : portions) {
            this.contents.add(portion);
        }
        this.calcCalories();
    }

    public void delete() {

    }

    public void calcCalories() {
        float proteinCals = this.protein * Food.kcalInProtein;
        float carbsCals = this.carbs * Food.kcalInCarbs;
        float fatsCals = this.fats * Food.kcalInFats;
        float fiberCals = this.fiber * Food.kcalInFiber;
        this.calories = proteinCals + carbsCals + fatsCals + fiberCals;
    }

    public String getName() {
        return this.name;
    }

    public float getCalories() {
        return this.calories;
    }

    public float getProtein() {
        return this.protein;
    }

    public float getCarbs() {
        return this.carbs;
    }

    public float getFats() {
        return this.fats;
    }

    public float getFiber() {
        return this.fiber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setFiber(float fiber) {
        this.fiber = fiber;
    }
}
