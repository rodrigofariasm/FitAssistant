package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
    private int textViewId; // id of the view to fill with the name of the object
    private int resourceId; //id of the xml used to create a item of the list


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
            TextView name = (TextView) v.findViewById(textViewId); // texteView to show on the adapter
            String id = obj.getObjectId(); // id of the parseobject

            String itemName = obj.getString("name");
            if(name != null && itemName != null){ // if the textview is found in the resource
                name.setText(itemName); // set the text, the name of the object
                name.setTag(id); // and the id as a tag
            }
        }
        return v;
    }

    /**To set the dropdown view of the spinner with the names of the objects in the list
     * */
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        T obj = (T) getItem(position);
        label.setText(obj.getString("name"));
        return label;
    }

    /**Method to return the position from the list in the adapter using the objectId
     * */
    public int getPosition(String itemId){
        for(ParseObject obj : objects){
            if(obj.getObjectId().equals(itemId)){
                return getPosition(obj);
            }
        }
        return -1;
    }

}


