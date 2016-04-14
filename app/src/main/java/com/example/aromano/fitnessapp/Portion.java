package com.example.aromano.fitnessapp;

/**
 * Created by aRomano on 23/03/2016.
 */
public class Portion extends Food {
    // sql stuff
    //private static int id_count = 0;
    private int id;

    public int getId() {
        return this.id;
    }

    // +++++++++++++++++

    private float serving;
    private Food food;

    Portion(int id, float serving, Food food) {
        this.id = id;
        this.serving = serving;
        this.food = food;
        //this.calcMacros();

        //Food.Portions.add(this);
    }
/*
    public void update(float serving, Food food) {
        this.serving = serving;
        this.food = food;
        this.calcMacros();
    }

    public void delete() {
        Food.Portions.remove(this);
    }

    public void calcMacros() {
        this.setProtein(this.getFood().getProtein() * this.getServing());
        this.setCarbs(this.getFood().getCarbs() * this.getServing());
        this.setFats(this.getFood().getFats() * this.getServing());
        this.setFiber(this.getFood().getFiber() * this.getServing());
        this.setCalories(this.getFood().getCalories() * this.getServing());
    }


    // DEBUG
    Portion() {
        this.serving = 2;
        this.food = new Ingredient("Default");
        this.calcMacros();

        Food.Portions.add(this);
    }
    // +++++++++
*/
    public float getServing() {
        return this.serving;
    }

    public Food getFood() {
        return this.food;
    }
}
