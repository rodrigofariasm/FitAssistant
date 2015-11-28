package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Meal;
import com.gc.materialdesign.views.ButtonFloat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Fragment representing the diet tab of the MainScreen of the Application
 * the variables meals, evento, opts and dates are necessary to pass the information
 * to the ExpandableListView listView and its adapter mAdapter
 */
public class DietFragment extends Fragment {

    public ArrayList<Meal> meals;
    public ArrayList<DietEvent> evento;
    HashMap<Meal, ArrayList<String>> opts;
    HashMap<String, Date> dates;
    ExpandableListView listView;
    ExpandableListDietHistoryAdapter mAdapter;



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

    /**
     * After the view be created this method configs the button to be used
     * if there's nothig related to diet scheduled for today
     * @param view
     * @param savedInstanceState
     */

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

    /**
     * initialize methods is always called at the Start of the activity
     * This setup the fragment showing either the listView if has something
     * to show or show the buttons.
     */
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

    /**
     * Hide the buttons if there is some meal scheduled for today.
     */
    public void hideButtons(){
        ButtonFloat buttonFloat = (ButtonFloat) getActivity().findViewById(R.id.create_schedule_button);
        buttonFloat.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) getActivity().findViewById(R.id.create_schedule_text);
        textView.setVisibility(View.INVISIBLE);
    }

    /**
     * Method that creates the ExpandableListView
     */

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
    }

}


