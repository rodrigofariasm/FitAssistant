package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonFloatSmall;
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
    HashMap<String, Date> dates;
    ExpandableListView listView;
    ExpandableListDietHistoryAdapter mAdapter;
    SimpleDateFormat dateFormatter;


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
        ButtonFloat buttonFloat = (ButtonFloat) getActivity().findViewById(R.id.create_schedule_button);
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DietScheduleActivity.class);
                startActivity(intent);

            }
        });
        evento = MainActivity.events;

    }

    @Override
    public void onStart(){
        super.onStart();
        initialize();
    }

    private void initialize(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.show();
        MainActivity.initialize = true;
        if( MainActivity.events.size() > 0){
            dates = MainActivity.dates;
            opts = MainActivity.opts;
            meals = MainActivity.meals;
            hideButtons();
            createHistoricForDay();



            // mostra dietas
        }else{
        }
        pd.dismiss();





    }

    public void hideButtons(){
        ButtonFloat buttonFloat = (ButtonFloat) getActivity().findViewById(R.id.create_schedule_button);
        buttonFloat.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) getActivity().findViewById(R.id.create_schedule_text);
        textView.setVisibility(View.INVISIBLE);
    }

    public void createHistoricForDay(){
        mAdapter = new ExpandableListDietHistoryAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_meal_historic, // The name of the layout ID.
                R.id.list_meal_name,R.id.checkbox_meal_done,
                dates,meals,// The ID of the textview to populate.
                opts, MainActivity.history_today);

        listView = (ExpandableListView) getActivity().findViewById(R.id.listView_dietSchedule);
        listView.setAdapter(mAdapter);
        MainActivity.initialize = false;
    }x  







    @Override
    public void onPause() {
        super.onPause();

    }

}


