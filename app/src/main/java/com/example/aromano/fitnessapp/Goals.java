package com.example.aromano.fitnessapp;

/**
 * Created by aRomano on 29/03/2016.
 */
public class Goals {
    private float calories;
    private float protein;
    private float carbs;
    private float fats;
    private float fiber;
    private boolean byCalories;

    Goals(float acalories, float aprotein, float acarbs, float afats, float afiber, boolean abyCalories) {
        this.byCalories = abyCalories;

        if(this.isByCalories()) {
            this.calories = acalories;
            // aprotein, acarbs, etc.. are percentages of those calories
            this.protein = (this.getCalories() / 4) * aprotein;
            this.carbs = (this.getCalories() / 4) * acarbs;
            this.fats = (this.getCalories() / 9) * afats;
            this.fiber = afiber;
        } else {
            this.protein = aprotein;
            this.carbs = acarbs;
            this.fats = afats;
            this.fiber = afiber;

            this.calories = (this.getProtein() * 4) + (this.getCarbs() * 4) + (this.getFats() * 9);
        }

    }

    public void updateGoals(float calories, float aprotein, float acarbs, float afats, float afiber, boolean abyCalories) {
        this.byCalories = abyCalories;

        if(this.isByCalories()) {
            this.calories = calories;
            // aprotein, acarbs, etc.. are percentages of those calories
            this.protein = (this.getCalories() / 4) * aprotein;
            this.carbs = (this.getCalories() / 4) * acarbs;
            this.fats = (this.getCalories() / 9) * afats;
            this.fiber = afiber;
        } else {
            this.protein = aprotein;
            this.carbs = acarbs;
            this.fats = afats;
            this.fiber = afiber;

            this.calories = (this.getProtein() * 4) + (this.getCarbs() * 4) + (this.getFats() * 9);
        }
    }


    public float getCalories() {
        return calories;
    }

    public float getProtein() {
        return protein;
    }

    public float getCarbs() {
        return carbs;
    }

    public float getFats() {
        return fats;
    }

    public float getFiber() {
        return fiber;
    }

    public boolean isByCalories() {
        return byCalories;
    }
}
