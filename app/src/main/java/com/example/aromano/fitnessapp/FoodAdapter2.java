package com.example.aromano.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aRomano on 30/03/2016.
 * https://www.youtube.com/watch?v=nOdSARCVYic
 */
public class FoodAdapter2 extends ArrayAdapter<Ingredient> {
    FoodAdapter2(Context context, List<Ingredient> ingredients) {
        super(context, R.layout.food_list_item, ingredients);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.food_list_item, parent, false);

        final Ingredient singleIngredientItem = getItem(position);
        TextView tv_name = (TextView) customView.findViewById(R.id.tv_name);
        //final Button btn_add = (Button) customView.findViewById(R.id.btn_add);

        tv_name.setText(singleIngredientItem.getName());
        /*btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aparently not needed because ArrayAdapter remove method removes it from the list and redraws
                //singleIngredientItem.deleteIngredient();
                //remove(singleIngredientItem);
                //for (Ingredient ingredient : Ingredient_3.List) {
                //    Log.d("Debug", String.valueOf(ingredient.getName()));
                //}
                //Portion p1 = new Portion(1, singleIngredientItem);
                //consumed.addFood(p1);
                //Log.d("Debug", String.valueOf(consumed.getCalories()));
            }
        });*/

        return customView;
    }

}
