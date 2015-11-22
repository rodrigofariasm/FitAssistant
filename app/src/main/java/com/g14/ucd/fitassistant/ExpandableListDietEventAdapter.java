package com.g14.ucd.fitassistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.g14.ucd.fitassistant.models.Other;
import com.gc.materialdesign.views.ButtonFlat;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodrigofarias on 11/19/15.
 */
public class ExpandableListDietEventAdapter<T extends ParseObject> extends BaseExpandableListAdapter {

    private Context _context;
    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<DietEvent,ArrayList<Meal>>_listDataChild;
    private int textViewId;
    private int resourceHeaderId;
    private int resourceChildId;
    private int button1;
    private int button2;
    private SimpleDateFormat dateFormatter;
    private int mealNameTextViewId;
    private int mealTimeTextViewId;

    public ExpandableListDietEventAdapter(Context context, int resourceHeader, int resourceChild,int textViewResourceId, int update,
                                          int delete,List<T> objects,
                                          HashMap<DietEvent,ArrayList<Meal>> listDataChild)  {
        this._context = context;
        this._listDataHeader = objects;
        this._listDataChild = listDataChild;
        textViewId = textViewResourceId;
        resourceHeaderId = resourceHeader;
        resourceChildId = resourceChild;
        button1 = delete;
        button2 = update;
        dateFormatter = new SimpleDateFormat("H:mm");
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Log.d("FitAssistant","childPosition: " + childPosition);
        Log.d("FitAssistant","child: " + getChild(groupPosition, childPosition));
        Log.d("FitAssistant","header: " + _listDataHeader.get(groupPosition));

        final Meal childMeal = (Meal) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(resourceChildId, null);
        }

        final DietEvent header = (DietEvent) _listDataHeader.get(groupPosition);

        LayoutInflater infalInflater = (LayoutInflater) this._context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(resourceChildId,null);

        TextView name = (TextView) convertView.findViewById(R.id.list_meal_name);
        TextView mealTime = (TextView) convertView.findViewById(R.id.list_meal_time);
        TextView mealOptions = (TextView) convertView.findViewById(R.id.list_meal_options);
        name.setText(MealEnum.fromCode(childMeal.getType()).getValue());
        String type = ""+childMeal.getType();
        Date date = (Date) header.getMap("times").get(type);
        mealTime.setText(dateFormatter.format(date));
        String options = "";
        for(String opt : childMeal.getOptions()){
            options += opt + ", ";
        }
        mealOptions.setText(options);
        convertView.setFocusableInTouchMode(false);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(resourceHeaderId, null);
        }
        T obj = (T) getGroup(groupPosition);
        String id = obj.getObjectId();
        if(obj != null){
            TextView name = (TextView) convertView.findViewById(textViewId);
            ImageButton delete = (ImageButton) convertView.findViewById(button1);
            ImageButton update = (ImageButton) convertView.findViewById(button2);
            delete.setFocusable(false);
            update.setFocusable(false);
            if(name != null){
                name.setText(obj.getString("name"));
            }

            if(update != null){
                update.setTag(id);
            }
            if(delete != null){
                delete.setTag(id);
            }
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}