package com.example.aromano.fitnessapp;

/**
 * Created by aRomano on 23/03/2016.
 */
public class CalorieCalculator {
    // Meals > Dishes > ingredients
    // Meals: Dishes + ingredients
    // Dishes: ingredients
    // ingredients: Kcals, Protein, Carbs, Fats, Fiber
    // ingredients: default measure? quantity
    public Goals Goals;
    public Consumed Consumed;
    private Meal Meals;
    private Dish Dishes;
    private Portion Portions;
    private Ingredient ingredients;
    // TODO: review the need for this class
    private float goalCalories = 2000;
    private float goalProtein = 0.40f;
    private float goalCarbs = 0.40f;
    private float goalFats = 0.20f;
    private float goalFiber = 25;
    private boolean goalByCalories = true;


    CalorieCalculator() {
        this.Goals = new Goals(goalCalories, goalProtein, goalCarbs, goalFats, goalFiber, goalByCalories);
        this.Consumed = new Consumed();
    }

    public float getRemainingCalories() {
        return this.Goals.getCalories() - this.Consumed.getCalories();
    }
}
