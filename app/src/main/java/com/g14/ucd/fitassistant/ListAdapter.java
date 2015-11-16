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

    private final Activity context = null;
    private List<T> objects = null;
    private int textViewId;
    private int resource;

    public ListAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        context = this.context;
        objects = this.objects;
        textViewId = textViewResourceId;
        resource = this.resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item_diet, null);
        }

        T obj = (T) getItem(position);
        if(obj != null){
            TextView name = (TextView) v.findViewById(R.id.list_item_name_diet);
            ImageButton delete = (ImageButton) v.findViewById(R.id.button_delete);
            ImageButton update = (ImageButton) v.findViewById(R.id.button_update);
            ImageButton view = (ImageButton) v.findViewById(R.id.button_view);

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
