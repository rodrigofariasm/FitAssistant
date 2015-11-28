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
 * Adpater to show the options for event TODAY for the user on MainActivity
 */
public class ListAdapterHistoric<T extends  ParseObject> extends ArrayAdapter {

    private Context context = null;
    private List<T> objects = null;
    private int textViewId;// id of the view to fill with the name of the object
    private int resourceId;  //id of the xml used to create a item of the list
    private SimpleDateFormat dateFormatter;
    private List<T> objetos2;
    private Historic historic; //object historic for this day of meals
    TextView name; //texteview in the resource that should be filled with the type of meal (Breakfast, lunch..)
    TextView timeT; //texteview in the resource that should be filled with the time for each task
    TextView mealOptions; //texteview in the resource that should be filled with the options for each meal
    CheckBox checkBoxDone; // checkbox in the xml where the user informs if completed the task
    int checkbox;// id of checkbox in the xml where the user informs if completed the task
    int timeId; //id of the texteview in the resource that should be filled with the time for each task

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


    /**Method that fiels the itens of the list
     * */
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
             name = (TextView) v.findViewById(textViewId); // texteView to show on the adapter
             timeT = (TextView) v.findViewById(timeId); // texteView to show the time on the adapter
             mealOptions = (TextView) v.findViewById(R.id.list_meal_options); // texteView to show the options for each meal on the adapter
             checkBoxDone = (CheckBox) v.findViewById(checkbox); // checkbox that indicates if this meal/task was done

            if(obj instanceof Meal){ // if the object is meal
                meal(obj);
            } else { // if is one FITactivit
                exercise(obj);
            }
        }
        return v;

    }

    private void exercise(T obj) {

        if(name != null){ // if found the texteview name in the xml
            if(findExercise(obj) != null) { // if find any exercise in the list
                name.setText(findExercise(obj).getString("name")); // se the name of the exercise found in the textview
            }
        }

        if(timeT != null){ // if found the texteview for time in the xml
            timeT.setText(dateFormatter.format(obj.getDate("time"))); // set the time of the event in the view
        }

        if(checkBoxDone != null){ // if found the checkbox in the xml
            checkBoxDone.setTag(obj.getObjectId()); // set as a tag the id for the event
        }
    }

    /**Méthod to return
     * */
    public T findExercise(T obj){// obj instance of an event
        for(T objeto : objetos2){ // search for exercises in the array objectos2
            if(objeto.getObjectId().equals(obj.getString("exerciseID"))){ // if one object in the array has the same id of the current event in the adapter list
                return objeto; // return this object
            }
        }
        return null;
    }

    public void meal(T obj){
        T event = objetos2.get(0); // the event is the first item of the list because meal only has one event a day

        int typeInt = obj.getInt("type"); // get the type of the meal
        if(name != null) {  // if found the texteview name in the xml
            name.setText(MealEnum.fromCode(typeInt).getValue()); // set the tipe of the meal as text
            name.setTag(event.getObjectId());//set the objectId of the meal as tag
        }

        String type = ""+ typeInt; // parsing type int to string
        if(timeT != null) {  // if found the texteview for time in the xmk
            Date date = (Date) event.getMap("times").get(type);
            timeT.setText(dateFormatter.format(date));
        }

        if(mealOptions != null) { // if found the texteview for meal options in the xml
            String optionsString = "";
            List<String> options = obj.getList("options"); // get list of options for the meal
            for (String opt : options) { //iterates in the array of options
                optionsString += opt + ", "; //and concat each options in only one string
            }
            mealOptions.setText(optionsString); // set the string concat in the textview for meal options
        }

        if(checkBoxDone != null){ // if found the checkbox in the xml
            checkBoxDone.setTag(obj.getInt("type")); // set the type of each meal in its checkbox
            if(historic.getMealsAte()!= null) { // if the current meal in the adapater has one historic
                boolean checked = historic.getMealsAte().get(type); // get the booelan from the historic
                if(checked) {
                    checkBoxDone.setChecked(checked);
                }
            }
        }

    }
}
