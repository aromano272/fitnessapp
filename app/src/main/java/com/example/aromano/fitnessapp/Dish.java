package com.example.aromano.fitnessapp;

import java.util.ArrayList;

/**
 * Created by aRomano on 23/03/2016.
 */
public class Dish extends Food {
    // sql stuff
    private static int id_count = 0;
    private int id;

    public int getId() {
        return this.id;
    }

    // +++++++++++++++++

    Dish(String name, Portion... portions) {
        this.id = ++Dish.id_count;
        this.setName(name);
        for (Portion portion : portions) {
            this.contents.add(portion);
        }
        this.calcMacros();
        Food.Dishes.add(this);
    }
    /*
    public void update(String name, Portion... portions) {
        this.contents.clear();
        this.setName(name);
        for(Portion portion : portions) {
            this.contents.add(portion);
        }
    }
*/
    public void delete() {
        Food.Dishes.remove(this);
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

/*
    public void deleteDish() {
        Dish.List.remove(this);
    }

    public void calcCalories() {
        this.calories = 0;
        this.protein = 0;
        this.carbs = 0;
        this.fats = 0;
        this.fiber = 0;

        for(Portion portion : this.portions) {
            this.calories += portion.getCalories();
            this.protein += portion.getProtein();
            this.carbs += portion.getCarbs();
            this.fats += portion.getFats();
            this.fiber += portion.getFiber();
        }
    }
    */
}
