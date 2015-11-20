package com.g14.ucd.fitassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Goal;
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
    private int button1;
    private int button2;
    private int button3;
    private int button4;

    public ListAdapter(Context context, int resource, int textViewResourceId, int view, int update,
                       int delete, int activate, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context  = context;
        this.objects = objects;
        textViewId = textViewResourceId;
        resourceId = resource;
        button1 = delete;
        button2 = update;
        button3 = view;
        button4 = activate;
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
            ImageButton delete = (ImageButton) v.findViewById(button1);
            ImageButton update = (ImageButton) v.findViewById(button2);
            ImageButton view = (ImageButton) v.findViewById(button3);
            Switch activated = (Switch) v.findViewById(button4);

            String id = obj.getObjectId();

            if(update != null){
                update.setTag(id);
            }
            if(delete != null){
                delete.setTag(id);
            }
            if(view != null){
                view.setTag(id);
            }
            if(activated != null){
                activated.setTag(id);
            }

            String itemName = null;
            if(name != null){
                if(obj instanceof Diet || obj instanceof FitActivity){
                    itemName = obj.getString("name");
                } else if (obj != null && obj instanceof Goal){
                    if(((Goal) obj).isActive()){
                        activated.setChecked(true);
                    }else{
                        activated.setChecked(false);
                    }
                    String goalType = obj.getString("type");
                    switch (goalType){
                         case("Lose fat"):
                            itemName = "Lose " + Integer.toString(obj.getInt("actual") - obj.getInt("desired")) + "% of fat";
                            break;
                         case("Lose weight"):
                             itemName = "Lose " + Integer.toString(obj.getInt("actual") - obj.getInt("desired")) + "kg";
                             break;
                         case("Gain weight"):
                             itemName = "Lose " + Integer.toString(obj.getInt("actual") - obj.getInt("desired")) + "kg";
                             break;
                    }
                }
                name.setText(itemName);
            }
        }
        return v;
    }
}
