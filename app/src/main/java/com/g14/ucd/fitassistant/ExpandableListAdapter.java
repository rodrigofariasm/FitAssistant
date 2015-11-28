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
import com.g14.ucd.fitassistant.models.Other;
import com.gc.materialdesign.views.ButtonFlat;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodrigofarias on 11/19/15.
 */


/**
 * ExpandableListAdapter used at ExpandableListView of ExerciseActitivy
 * @param <T>
 */
public class ExpandableListAdapter<T extends ParseObject> extends BaseExpandableListAdapter {

    private Context _context;
    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<FitActivity, ArrayList<Exercise>> _listDataChild;
    private int textViewId;
    private int resourceId;
    private int button1;
    private int button2;
    private int button3;
    private int button4;
    private int icon;

    public ExpandableListAdapter(Context context, int resource, int textViewResourceId, int update,
                                 int delete, int activate, List<T> objects, HashMap<FitActivity,
                                 ArrayList<Exercise>> listDataChild, int icon)  {
        this._context = context;
        this._listDataHeader = objects;
        this._listDataChild = listDataChild;
        textViewId = textViewResourceId;
        resourceId = resource;
        button1 = delete;
        button2 = update;
        button4 = activate;
        this.icon = icon;

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


    /**
     * Method that shows the content at the inflation of a group
     * and configure it,
     * the child has two types of contents, the layout for gym
     * and the other is for general activities.
     * If it is a gym activity, the button go there will open
     * the maps application with the activity location pinned
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
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

    /**
     * Header inflater
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_exercise, null);
        }
        T obj = (T) getGroup(groupPosition);
        String id = obj.getObjectId();
        ImageView icone = (ImageView) convertView
                .findViewById(icon);

        ImageButton delete = (ImageButton) convertView.findViewById(button1);
        delete.setFocusable(false);
        ImageButton update = (ImageButton) convertView.findViewById(button2);
        update.setFocusable(false);
        if(obj instanceof  Other){
            headerTitle = ((Other) getGroup(groupPosition)).getName();
            icone.setImageResource(R.drawable.sprint);
        }else{
            headerTitle = ((Gym) getGroup(groupPosition)).getName();
            icone.setImageResource(R.drawable.dumbbell);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.list_item_name_exercise);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        if(update != null){
            update.setTag(id);
        }
        if(delete != null){
            delete.setTag(id);
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