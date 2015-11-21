package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Natália on 17/11/2015.
 */
public class SpinnerAdapter <T extends ParseObject> extends ArrayAdapter {

    private Context context = null;
    private List<T> objects = null;
    private int textViewId;
    private int resourceId;


    public SpinnerAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context  = context;
        this.objects = objects;
        textViewId = textViewResourceId;
        resourceId = resource;
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
            String id = obj.getObjectId();
            String itemName = obj.getString("name");
            if(name != null && itemName != null){
                name.setText(itemName);
                name.setTag(id);
            }
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        T obj = (T) getItem(position);
        label.setText(obj.getString("name"));
        return label;
    }

    public int getPosition(String itemId){
        for(ParseObject obj : objects){
            if(obj.getObjectId().equals(itemId)){
                return getPosition(obj);
            }
        }
        return -1;
    }
}


