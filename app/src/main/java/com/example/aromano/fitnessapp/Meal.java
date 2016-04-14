package com.example.aromano.fitnessapp;

import java.util.ArrayList;

/**
 * Created by aRomano on 23/03/2016.
 */
public class Meal extends Food {
    // sql stuff
    private static int id_count = 0;
    private int id;

    public int getId() {
        return this.id;
    }

    // +++++++++++++++++

    Meal(String name, Portion... portions) {
        this.id = ++Meal.id_count;
        this.setName(name);
        for (Portion portion : portions) {
            this.contents.add(portion);
        }
        this.calcMacros();
        Food.Meals.add(this);
    }

    public void delete() {
        Food.Meals.remove(this);
    }

    private void calcMacros() {
        float calories = 0, protein = 0, carbs = 0, fats = 0, fiber = 0;
        for(Portion portion : this.contents) {
            calories += portion.getCalories();
            protein += portion.getProtein();
            carbs += portion.getCarbs();
            fats += portion.getFats();
            fiber += portion.getFiber();
        }
        this.setCalories(calories);
        this.setProtein(protein);
        this.setCarbs(carbs);
        this.setFats(fats);
        this.setFiber(fiber);
    }
}
