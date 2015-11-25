package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.HistoricType;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.gc.materialdesign.views.CheckBox;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class DietFragment extends Fragment {

    public ArrayList<Meal> meals;
    public TreeSet<Integer> idx;
    public ArrayList<DietEvent> evento;
    HashMap<Meal, ArrayList<String>> opts;
    ArrayList<Date> dates;
    ExpandableListView listView;
    ExpandableListDietHistoryAdapter mAdapter;
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
        evento = new ArrayList<DietEvent>();
        initialize();
    }

    private void initialize(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.show();
        historicDiet = new Historic();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar today = new GregorianCalendar();
        List<Integer> todayOption = new ArrayList<>();
        int option = today.get(GregorianCalendar.DAY_OF_WEEK);
        todayOption.add(new Integer(option));
        ParseQuery<DietEvent> query = new ParseQuery<DietEvent>("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereContainsAll("weekDays", todayOption);
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvents, ParseException exception) {
                if (exception == null) { // found diets
                    if (dietEvents.size() > 0) {
                        evento.addAll(dietEvents);
                        findMeals();
                        pd.dismiss();
                    }else{
                        Toast.makeText(getActivity(), "Create a diet event", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });

    }

    public void findMeals(){
        idx = new TreeSet<Integer>();
        meals = new ArrayList<Meal>();
        opts = new HashMap<Meal, ArrayList<String>>();
        dates = new ArrayList<Date>();
        ParseQuery<Meal> querym = ParseQuery.getQuery("Meal");
        querym.whereEqualTo("user", ParseUser.getCurrentUser());
        querym.whereEqualTo("dietID", evento.get(0).getDietId());
        querym.findInBackground(new FindCallback<Meal>() {
            @Override
            public void done(List<Meal> meal, ParseException exception) {
                if (exception == null) { // found diets
                    if (meal.size() > 0) {
                        for (Meal m : meal) {
                            idx.add(m.getType());
                        }
                        for (int i = 0; i < idx.size(); i++) {
                            Meal aux = meal.get(idx.first());
                            meals.add(aux);
                            dates.add(evento.get(0).getTimes().get("" + idx.pollFirst()));
                            opts.put(aux, (ArrayList<String>) aux.getOptions());
                        }
                        createHistoricForDay();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }
    public void createHistoricForDay(){
        searchHistoricToday();
        //populateMeal(event);
        //List<DietEvent> events = new ArrayList<DietEvent>();
        //events.add(event);

        mAdapter = new ExpandableListDietHistoryAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_meal_historic, // The name of the layout ID.
                R.id.list_meal_name,R.id.checkbox_meal_done,
                dates,meals,// The ID of the textview to populate.
                opts, historicDiet);

        listView = (ExpandableListView) getActivity().findViewById(R.id.listView_dietSchedule);
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
            Map<String, Boolean> mapMealsAte = new HashMap<>();
            if(listView.getAdapter() != null) {
                int countMeals = listView.getAdapter().getCount();
                Log.d("cont", "" + countMeals);
                ExpandableListDietHistoryAdapter expandable = (ExpandableListDietHistoryAdapter) listView.getExpandableListAdapter();
                historicDiet.setType(HistoricType.DIET.getCode());
                historicDiet.setMealsAte(expandable.getMapCheckbox());
                historicDiet.setDate(dateFormatter.format(Calendar.getInstance().getTime()));
                historicDiet.setEventId(evento.get(0).getObjectId());
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


