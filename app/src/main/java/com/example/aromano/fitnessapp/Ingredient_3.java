package com.example.aromano.fitnessapp;

import java.util.ArrayList;

/**
 * Created by aRomano on 23/03/2016.
 */
public class Ingredient_3 {

    // sql stuff
    private int id;

    public int getId() {
        return id;
    }

    // +++++++++++++++++

    public static ArrayList<Ingredient_3> List = new ArrayList<>();
    
    final private static float kcalInProtein = 4;
    final private static float kcalInCarbs = 4;
    final private static float kcalInFats = 9;
    final private static float kcalInFiber = 0;

    private String name;
    private float servings;
    private float calories;
    private float protein;
    private float carbs;
    private float fats;
    private float fiber;

    Ingredient_3(String aname, float aservings, float aprotein, float acarbs, float afats, float afiber) {
        this.name = aname;
        this.servings = aservings;
        this.protein = aprotein / this.servings;
        this.carbs = acarbs / this.servings;
        this.fats = afats / this.servings;
        this.fiber = afiber / this.servings;
        this.calcCalories();
        
        Ingredient_3.List.add(this);
    }

    // DEBUG
    Ingredient_3(String aname) {
        this.name = aname;

        this.servings = 1;
        this.protein = 10 / this.servings;
        this.carbs = 20 / this.servings;
        this.fats = 5 / this.servings;
        this.fiber = 2.5f / this.servings;
        this.calcCalories();

        Ingredient_3.List.add(this);
    }



    public void updateIngredient(String aname, float aservings, float aprotein, float acarbs, float afats, float afiber) {
        this.name = aname;
        this.servings = aservings;
        this.protein = aprotein / this.servings;
        this.carbs = acarbs / this.servings;
        this.fats = afats / this.servings;
        this.fiber = afiber / this.servings;
        this.calcCalories();
    }
    
    public void deleteIngredient() {
        Ingredient_3.List.remove(this);
    }

    private void calcCalories() {
        float proteinCals = this.protein * Ingredient_3.kcalInProtein;
        float carbsCals = this.carbs * Ingredient_3.kcalInCarbs;
        float fatsCals = this.fats * Ingredient_3.kcalInFats;
        float fiberCals = this.fiber * Ingredient_3.kcalInFiber;
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
}
