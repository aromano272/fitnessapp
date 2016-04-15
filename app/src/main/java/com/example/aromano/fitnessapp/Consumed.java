package com.example.aromano.fitnessapp;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by aRomano on 30/03/2016.
 */
public class Consumed {
    // TODO: check if needed
    private float calories;
    private float protein;
    private float carbs;
    private float fats;
    private float fiber;
    public ArrayList<Portion> portions;

    Consumed() {
        this.portions = new ArrayList<>();
    }

    public void addFood(Portion portion) {
        this.portions.add(portion);
        this.calories += portion.getCalories();
        this.protein += portion.getProtein();
        this.carbs += portion.getCarbs();
        this.fats += portion.getFats();
        this.fiber += portion.getFiber();
    }

    public void removeFood(Portion portion) {
        this.portions.remove(portion);
        this.calories -= portion.getCalories();
        this.protein -= portion.getProtein();
        this.carbs -= portion.getCarbs();
        this.fats -= portion.getFats();
        this.fiber -= portion.getFiber();
    }

    public void reset() {
        this.portions.clear();
        this.calories = 0;
        this.protein = 0;
        this.carbs = 0;
        this.fats = 0;
        this.fiber = 0;
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
