package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.g14.ucd.fitassistant.models.MealEnum;
import com.parse.ParseObject;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Natália on 12/11/2015.
 */
public class ListMealScheduleAdapter<T extends  ParseObject> extends ArrayAdapter {

    private Context context = null;
    private List<T> objects = null;
    private int textViewId;
    private int resourceId;
    private int editTextViewId;


    public ListMealScheduleAdapter(Context context, int resource, int textViewResourceId, int editTextViewId,List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context  = context;
        this.objects = objects;
        textViewId = textViewResourceId;
        resourceId = resource;
        this.editTextViewId = editTextViewId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(resourceId, null);
        }

        T obj = (T) getItem(position);
        if(obj != null){
            TextView name = (TextView) v.findViewById(textViewId);
            EditText time = (EditText) v.findViewById(editTextViewId);
            time.setInputType(InputType.TYPE_NULL);

            String id = obj.getObjectId();
            if(name != null){
                name.setText(MealEnum.fromCode(obj.getInt("type")).getValue());
            }

            if(time != null){
                time.setTag(obj.getInt("type"));
            }
        }
        return v;
    }
}
