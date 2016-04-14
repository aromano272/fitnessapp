package com.example.aromano.fitnessapp;

import java.util.ArrayList;

/**
 * Created by aRomano on 23/03/2016.
 */
public class Portion_2 {
    public static ArrayList<Portion_2> List = new ArrayList<>();

    private float servings;
    private float calories;
    private float protein;
    private float carbs;
    private float fats;
    private float fiber;
    private Ingredient_3 ingredient3;

    Portion_2(float aservings, Ingredient_3 aingredient) {
        this.servings = aservings;
        this.ingredient3 = aingredient;
        this.protein = this.getIngredient3().getProtein() * this.getServings();
        this.carbs = this.getIngredient3().getCarbs() * this.getServings();
        this.fats = this.getIngredient3().getFats() * this.getServings();
        this.fiber = this.getIngredient3().getFiber() * this.getServings();
        this.calories = this.getIngredient3().getCalories() * this.getServings();

        Portion_2.List.add(this);
    }

    public void updatePortion_21(float servings, Ingredient_3 ingredient3) {
        this.servings = servings;
        this.ingredient3 = ingredient3;
        this.calcMacros();
    }

    public void calcMacros() {

    }


    // DEBUG
    Portion_2() {
        this.servings = 2;
        this.ingredient3 = new Ingredient_3("Default");
        this.protein = this.getIngredient3().getProtein() * this.getServings();
        this.carbs = this.getIngredient3().getCarbs() * this.getServings();
        this.fats = this.getIngredient3().getFats() * this.getServings();
        this.fiber = this.getIngredient3().getFiber() * this.getServings();
        this.calories = this.getIngredient3().getCalories() * this.getServings();

        Portion_2.List.add(this);
    }
    // +++++++++

    public void updatePortion_2(float aservings, Ingredient_3 aingredient) {
        this.servings = aservings;
        this.ingredient3 = aingredient;
        this.protein = this.ingredient3.getProtein() * this.getServings();
        this.carbs = this.ingredient3.getCarbs() * this.getServings();
        this.fats = this.ingredient3.getFats() * this.getServings();
        this.fiber = this.ingredient3.getFiber() * this.getServings();
        this.calories = this.ingredient3.getCalories() * this.getServings();
    }

    public void deletePortion_2() {
        Portion_2.List.remove(this);
    }

    public float getServings() {
        return this.servings;
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

    public Ingredient_3 getIngredient3() {
        return this.ingredient3;
    }
}
