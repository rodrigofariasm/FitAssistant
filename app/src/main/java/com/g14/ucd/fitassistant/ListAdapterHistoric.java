package com.g14.ucd.fitassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.gc.materialdesign.views.CheckBox;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Natália on 12/11/2015.
 */
public class ListAdapterHistoric<T extends  ParseObject> extends ArrayAdapter {

    private Context context = null;
    private List<T> objects = null;
    private int textViewId;
    private int resourceId;
    private SimpleDateFormat dateFormatter;
    private List<T> objetos2;
    private Historic historic;
    TextView name;
    TextView timeT;
    TextView mealOptions;
    CheckBox checkBoxDone;
    int checkbox;
    int timeId;

    public ListAdapterHistoric(Context context, int resource, int textViewResourceId,int timeId, int checkbox,List<T> objects, List<T> objetos2, Historic historic) {
        super(context, resource, textViewResourceId, objects);
        this.context  = context;
        this.objects = objects;
        textViewId = textViewResourceId;
        resourceId = resource;
        dateFormatter = new SimpleDateFormat("H:mm");
        this.objetos2 = objetos2;
        this.historic = historic;
        this.checkbox = checkbox;
        this.timeId = timeId;
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
             name = (TextView) v.findViewById(textViewId);
             timeT = (TextView) v.findViewById(timeId);
             mealOptions = (TextView) v.findViewById(R.id.list_meal_options);
             checkBoxDone = (CheckBox) v.findViewById(checkbox);

            if(obj instanceof Meal){
                meal(obj);
            } else {
                exercise(obj);
            }
        }
        return v;

    }

    private void exercise(T obj) {

        if(name != null){
            if(findExercise(obj) != null) {
                name.setText(findExercise(obj).getString("name"));
            }
        }

        if(timeT != null){
            timeT.setText(dateFormatter.format(obj.getDate("time")));
        }

        if(checkBoxDone != null){
            checkBoxDone.setTag(obj.getObjectId());
        }
    }


    public T findExercise(T obj){// obj é um evento
        for(T objeto : objetos2){ // procura nos exerccios
            if(objeto.getObjectId().equals(obj.getString("exerciseID"))){
                return objeto;
            }
        }
        return null;
    }

    public void meal(T obj){
        T event = objetos2.get(0);

        int typeInt = obj.getInt("type");
        if(name != null) {
            name.setText(MealEnum.fromCode(typeInt).getValue());
            name.setTag(event.getObjectId());
        }

        String type = ""+ typeInt;
        if(timeT != null) {
            Date date = (Date) event.getMap("times").get(type);
            timeT.setText(dateFormatter.format(date));
        }

        if(mealOptions != null) {
            String optionsString = "";
            List<String> options = obj.getList("options");
            for (String opt : options) {
                optionsString += opt + ", ";
            }
            mealOptions.setText(optionsString);
        }

        if(checkBoxDone != null){
            checkBoxDone.setTag(obj.getInt("type"));
            if(historic.getMealsAte()!= null) {
                boolean checked = historic.getMealsAte().get(type);
                if(checked) {
                    checkBoxDone.setChecked(checked);
                }
            }
        }

    }
}
