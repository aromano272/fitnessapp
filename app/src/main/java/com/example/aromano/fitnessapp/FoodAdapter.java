package com.example.aromano.fitnessapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by aRomano on 11/04/2016.
 */
public class FoodAdapter extends ResourceCursorAdapter {
    public FoodAdapter(Context context, Cursor cursor, int flags) {
        super(context, R.layout.food_list_item, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

        tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
    }


}
