package com.example.aromano.fitnessapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by aRomano on 11/04/2016.
 */
public class DiaryAdapter extends ResourceCursorAdapter {

    public DiaryAdapter(Context context, Cursor cursor, int flags) {
        super(context, R.layout.diary_list_item, cursor, flags);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_calories = (TextView) view.findViewById(R.id.tv_calories);
        TextView tv_protein = (TextView) view.findViewById(R.id.tv_protein);
        Button btn_remove = (Button) view.findViewById(R.id.btn_remove);

        final int _id = cursor.getInt(cursor.getColumnIndex("_id"));
        final String ingredientname = cursor.getString(cursor.getColumnIndex("ingredientname"));
        tv_name.setText(ingredientname);
        tv_quantity.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndex("servings"))));
        tv_calories.setText(String.valueOf((int)cursor.getFloat(cursor.getColumnIndex("calories"))));
        tv_protein.setText(String.valueOf((int)cursor.getFloat(cursor.getColumnIndex("protein"))));

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setMessage("Delete " + ingredientname + " entry?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        // TODO http://stackoverflow.com/questions/3619693/getting-views-coordinates-relative-to-the-root-layout
                        // TODO http://stackoverflow.com/questions/9467026/changing-position-of-the-dialog-on-screen-android
                        // TODO search for how to implement a 3 dot menu style popup, or a dropdown
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager db = DBManager.getInstance(context);
                                db.deleteDiaryEntry(_id);
                                DiaryAdapter.this.changeCursor(db.getTodayDiaryEntries());

                                // i need to propagate the event to MainActivity so i can refresh the remaining calories
                                View parentRow = (View) v.getParent();
                                ListView listView = (ListView) parentRow.getParent();
                                final int position = listView.getPositionForView(parentRow);
                                listView.performItemClick(v, position, 0);

                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });
    }

}
