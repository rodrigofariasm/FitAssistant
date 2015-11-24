package com.g14.ucd.fitassistant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.HistoricType;
import com.g14.ucd.fitassistant.models.Meal;
import com.gc.materialdesign.views.CheckBox;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DietFragment extends Fragment {

    List<Meal> meals;
    SimpleDateFormat dateFormatter;
    private Historic historicDiet;

    public DietFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet_framgent, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initialize();
    }

    private void initialize(){
        historicDiet = new Historic();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar today = new GregorianCalendar();
        List<Integer> todayOption = new ArrayList<>();
        int option = today.get(GregorianCalendar.DAY_OF_WEEK);
        todayOption.add(new Integer(option));

        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereContainsAll("weekDays", todayOption);
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvents, ParseException exception) {
                if (exception == null) { // found diets
                    if (dietEvents.size() > 0) {
                        createHistoricForDay(dietEvents.get(0));
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

    public void createHistoricForDay(DietEvent event){
        searchHistoricToday();
        populateMeal(event);
        List<DietEvent> events = new ArrayList<DietEvent>();
        events.add(event);

        ListAdapterHistoric mAdapter = new ListAdapterHistoric(
                getActivity(), // The current context (this activity)
                R.layout.list_item_meal_historic, // The name of the layout ID.
                R.id.list_meal_name,R.id.list_meal_time,
                R.id.checkbox_meal_done,// The ID of the textview to populate.
                meals,events,historicDiet);

        ListView listView = (ListView) getActivity().findViewById(R.id.listView_dietSchedule);
        listView.setAdapter(mAdapter);
    }

    public void populateMeal(DietEvent event){
        ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("dietID", event.getDietId());
        try {
            meals = (ArrayList<Meal>) query.find();
        } catch (ParseException e) {
            Log.d("FitAssistant", "Error" + e.getMessage());
        }
    }


    public void searchHistoricToday(){
        ParseQuery<Historic> query = ParseQuery.getQuery("Historic");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("date", dateFormatter.format(Calendar.getInstance().getTime()));
        query.whereEqualTo("type",0);
        try {
            List<Historic> historics = query.find();
            if(historics.size() > 0) {
                historicDiet = historics.get(0);
            }
        }catch (ParseException e){
            Log.d("FITASSISTANT", "Error searching events");
        }
    }

    public void saveHistoric(){
        FrameLayout dietFragment = (FrameLayout) getActivity().findViewById(R.id.diet_fragment);
        if(dietFragment != null) {
            ListView listView = (ListView) dietFragment.findViewById(R.id.listView_dietSchedule);
            String eventId = null;
            Map<String, Boolean> mapMealsAte = new HashMap<>();
            if(listView.getAdapter() != null) {
                int countMeals = listView.getAdapter().getCount();
                for (int count = 0; count < countMeals; count++) {
                    RelativeLayout relativeLayout = (RelativeLayout) listView.getAdapter().getView(count, null, null);
                    CheckBox checkBox = (CheckBox) relativeLayout.findViewById(R.id.checkbox_meal_done);
                    String typeMeal = "" + checkBox.getTag();
                    mapMealsAte.put(typeMeal, checkBox.isCheck());
                    if (count == 0) {
                        eventId = (String) relativeLayout.findViewById(R.id.list_meal_name).getTag();
                    }
                }

                historicDiet.setType(HistoricType.DIET.getCode());
                historicDiet.setMealsAte(mapMealsAte);
                historicDiet.setDate(dateFormatter.format(Calendar.getInstance().getTime()));
                historicDiet.setEventId(eventId);
                historicDiet.setUser(ParseUser.getCurrentUser());
                historicDiet.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                        } else {
                            Log.d("FITASSISTANT", "ERROR saving historic " + e.getMessage());
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveHistoric();
    }
}


