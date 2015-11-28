package com.g14.ucd.fitassistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

/**
 * Created by rodrigofarias on 11/27/15.
 */

/**
 * ExpandableListExerciseHistoryAdapter is used at ExerciseFragment of MainActivity
 *
 * @param <T>
 */
public class ExpandableListExerciseHistoryAdapter<T extends ParseObject> extends BaseExpandableListAdapter {

    private Context _context;
    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<FitActivity, ArrayList<Exercise>> _listDataChild;
    private HashMap<String, Date> dates;
    private int textViewId;
    private int resourceId;
    private int checkbox;
    Historic history;

    public ExpandableListExerciseHistoryAdapter(Context context, int resource, int textViewResourceId, int check_box,
                                            HashMap<String, Date> dateset, List<T> objects, HashMap<FitActivity,
            ArrayList<Exercise>> listDataChild, Historic historic)  {
        this._context = context;
        this._listDataHeader = objects;
        this._listDataChild = listDataChild;
        textViewId = textViewResourceId;
        resourceId = resource;
        dates = dateset;
        checkbox= check_box;
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

        final Exercise childExercise = (Exercise) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exercise_list_group, null);
        }

        final FitActivity header = (FitActivity) _listDataHeader.get(groupPosition);
        if(header instanceof Gym){
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exercise_list_group,null);
            TextView name = (TextView) convertView.findViewById(R.id.child_exercise_name);
            TextView series = (TextView) convertView.findViewById(R.id.child_exercise_section);
            TextView repetitions = (TextView) convertView.findViewById(R.id.child_exercise_repetition);
            name.setText(childExercise.getName().toString());
            series.setText(""+childExercise.getSections()+" sets of ");
            repetitions.setText(""+childExercise.getRepetitions()+ " repetitions");
            convertView.setFocusableInTouchMode(false);
        }else{
            final Other other = (Other) header;
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.other_list_group,null);
            if(header.getString("description")!= null){
                TextView name = (TextView) convertView.findViewById(R.id.child_other_description);
                name.setText( header.getString("description"));
            }
            /**
             * button GoThere will open a Google Maps application, to the selected location at the exercise.
             */
            if(header.getString("location")!= null){
                TextView location = (TextView) convertView.findViewById(R.id.child_other_location);
                location.setText(header.getString("location"));
                ButtonFlat goButton = (ButtonFlat) convertView.findViewById(R.id.child_button_go);
                goButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uri = "geo:0,0?q="+ other.getLocation();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        _context.startActivity(intent);
                    }
                });

            }


        }

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

        FitActivity obj = (FitActivity) getGroup(groupPosition);
        headerTitle = obj.getName();
        String id = obj.getObjectId();
        TextView time1 = (TextView) convertView.findViewById(R.id.list_meal_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm");
        Date data = dates.get(id);
        time1.setText(dateFormat.format(data));
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.list_meal_name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);


        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_meal_done);


        checkBox.setFocusable(false);
        if(checkBox!= null){
            checkBox.setTag(id);
            checkBox.setOncheckListener(new CheckBox.OnCheckListener() {
                @Override
                public void onCheck(CheckBox checkBox, boolean b) {
                    checkBox.setChecked(b);
//                    MainActivity.exercisesPerformed.put(checkBox.getTag().toString(), b);
//                    MainActivity.history_today.setExercisesDone(MainActivity.exercisesPerformed);
                }
            });
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
