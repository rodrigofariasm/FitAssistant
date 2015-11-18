package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Natália on 12/11/2015.
 */
public class ListAdapter<T extends  ParseObject> extends ArrayAdapter {

    private Context context = null;
    private List<T> objects = null;
    private int textViewId;
    private int resourceId;
    private int buttonDelete;
    private int buttonUpdate;
    private int buttonView;

    public ListAdapter(Context context, int resource, int textViewResourceId, int view, int update,
                       int delete, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context  = context;
        this.objects = objects;
        textViewId = textViewResourceId;
        resourceId = resource;
        buttonDelete = delete;
        buttonUpdate = update;
        buttonView = view;
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
            ImageButton delete = (ImageButton) v.findViewById(buttonDelete);
            ImageButton update = (ImageButton) v.findViewById(buttonUpdate);
            ImageButton view = (ImageButton) v.findViewById(buttonView);

            String id = obj.getObjectId();

            if(name != null){
                name.setText(obj.getString("name"));
            }
            if(update != null){
                update.setTag(id);
            }
            if(delete != null){
                delete.setTag(id);
            }
            if(view != null){
                view.setTag(id);
            }
        }


        return v;
    }
}
