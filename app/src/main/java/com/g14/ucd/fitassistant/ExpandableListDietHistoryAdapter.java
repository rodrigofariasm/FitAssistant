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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.g14.ucd.fitassistant.models.Other;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.CheckBox;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rodrigofarias on 11/19/15.
 */
public class ExpandableListDietHistoryAdapter<T extends ParseObject> extends BaseExpandableListAdapter {

    private Context _context;
    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Meal, ArrayList<String>> _listDataChild;
    private ArrayList< Date> dates;
    private int textViewId;
    private int resourceId;
    private int checkbox;
    Historic history;
    private Map<String,Boolean> mapCheckbox;

    public ExpandableListDietHistoryAdapter(Context context, int resource, int textViewResourceId, int check_box,
                                            ArrayList<Date> dateset, List<T> objects, HashMap<Meal,
                                            ArrayList<String>> listDataChild, Historic historic)  {
        this._context = context;
        this._listDataHeader = objects;
        this._listDataChild = listDataChild;
        textViewId = textViewResourceId;
        resourceId = resource;
        dates = dateset;
        checkbox= check_box;
        history = historic;
        mapCheckbox = new HashMap<String,Boolean>();
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


        final String child = getChild(groupPosition, childPosition).toString();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exercise_list_group, null);
        }

        final String header =  _listDataHeader.get(groupPosition).toString();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exercise_list_group,null);
            TextView name = (TextView) convertView.findViewById(R.id.child_exercise_name);
            name.setText(child);
            TextView sets = (TextView) convertView
                .findViewById(R.id.child_exercise_repetition);
            TextView reps = (TextView) convertView
                .findViewById(R.id.child_exercise_section);
            sets.setVisibility(View.INVISIBLE);
            reps.setVisibility(View.INVISIBLE);
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
            convertView = infalInflater.inflate(R.layout.list_item_meal_historic, null);
        }

        T obj = (T) getGroup(groupPosition);
        headerTitle = MealEnum.fromCode(obj.getInt("type")).getValue();
        String id = obj.getObjectId();
        TextView time = (TextView) convertView.findViewById(R.id.list_meal_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
        time.setText(dateFormat.format(dates.get(groupPosition)));
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.list_meal_name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        final String typeString = "" + obj.getInt("type");

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_meal_done);
        checkBox.setFocusable(false);
        if(checkBox!= null){
            checkBox.setTag(obj.getInt("type"));
            checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
                @Override
                public void onCheck(CheckBox checkBox, boolean b) {
                    mapCheckbox.put(typeString,b);
                    checkBox.setChecked(b);
                }
            });
            if(history.getMealsAte() != null && history.getMealsAte().size() > 0){
                checkBox.setChecked(history.getMealsAte().get(typeString));
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


    public Map<String, Boolean> getMapCheckbox() {
        return mapCheckbox;
    }

    public void setMapCheckbox(Map<String, Boolean> mapCheckbox) {
        this.mapCheckbox = mapCheckbox;
    }
}