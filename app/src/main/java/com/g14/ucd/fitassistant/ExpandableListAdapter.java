package com.g14.ucd.fitassistant;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rodrigofarias on 11/19/15.
 */
public class ExpandableListAdapter<T extends ParseObject> extends BaseExpandableListAdapter {

    private Context _context;
    private List<T> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Exercise>> _listDataChild;
    private int textViewId;
    private int resourceId;
    private int button1;
    private int button2;
    private int button3;
    private int button4;
    private int icon;

    public ExpandableListAdapter(Context context, int resource, int textViewResourceId, int update,
                                 int delete, int activate, List<T> objects, HashMap<String,
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

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.exercise_list_group, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListHeader);

        txtListChild.setText(childText);
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
            convertView = infalInflater.inflate(R.layout.list_item_diet, null);
        }
        T obj = (T) getGroup(groupPosition);
        String id = obj.getObjectId();
        ImageView icone = (ImageView) convertView
                .findViewById(icon);
        ImageButton delete = (ImageButton) convertView.findViewById(button1);
        ImageButton update = (ImageButton) convertView.findViewById(button2);

        if(obj instanceof  Other){
            headerTitle = ((Other) getGroup(groupPosition)).getDescription();
            icone.setImageResource(R.drawable.sprint);
        }else{
            headerTitle = ((Gym) getGroup(groupPosition)).getName();
            icone.setImageResource(R.drawable.dumbbell);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.list_item_name_diet);
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