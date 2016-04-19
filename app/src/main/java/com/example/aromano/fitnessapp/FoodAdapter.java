package com.example.aromano.fitnessapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by aRomano on 11/04/2016.
 */
public class FoodAdapter extends ResourceCursorAdapter {
    public FoodAdapter(Context context, Cursor cursor, int flags) {
        super(context, R.layout.food_list_item2, cursor, flags);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        Button btn_decreaseServings = (Button) view.findViewById(R.id.btn_decreaseServings);
        final EditText et_servings = (EditText) view.findViewById(R.id.et_servings);
        Button btn_increaseServings = (Button) view.findViewById(R.id.btn_increaseServings);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        Button btn_add = (Button) view.findViewById(R.id.btn_add);


        // TODO: implement button functionality on listview buttons
        // http://www.migapro.com/click-events-listview-gridview/

        tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));

        // TODO: leave decrease and increase methods internal to the adapter and only perfromItemClick on btn_add
        // TODO: find a way to pass the et_servings value through performItemClick
        btn_decreaseServings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double servings = Double.parseDouble(et_servings.getText().toString());
                servings -= 0.05d;

                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.UP);

                et_servings.setText(df.format(servings));
            }
        });

        btn_increaseServings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double servings = Double.parseDouble(et_servings.getText().toString());
                servings += 0.05d;

                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.UP);

                et_servings.setText(df.format(servings));
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View parentRow = (View) v.getParent();
                ListView listView = (ListView) parentRow.getParent();
                final int position = listView.getPositionForView(parentRow);

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                float servings = Float.parseFloat(et_servings.getText().toString());
                // send servings through id, ugly but works
                listView.performItemClick(v, position, (long)(servings * 100)); // Let the event be handled in onItemClick()
            }
        });

    }
}
