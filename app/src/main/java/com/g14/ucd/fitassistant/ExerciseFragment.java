package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Other;
import com.gc.materialdesign.views.ButtonFloat;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment representing the exercise tab of the MainScreen of the Application
 * to the ExpandableListView listView and its adapter mAdapter
 */
public class ExerciseFragment extends Fragment {
    ExpandableListExerciseHistoryAdapter mAdapter;
    ExpandableListView listView;

    public ExerciseFragment() {
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
        return inflater.inflate(R.layout.fragment_exercise, container, false);

    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        ButtonFloat buttonFloat = (ButtonFloat) getActivity().findViewById(R.id.create_schedule_exe_button);
        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExerciseScheduleActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        initialize();
    }

    /*
    * Called to initialize the fragment and setup its components.
     */
    private void initialize(){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading");
        pd.show();
        if( MainActivity.exeEvents.size() > 0){
            hideButtons();
            createHistoricForDay();

        }else{

        }

        pd.dismiss();


    }

    /***
     * Setup expandablelistview
     */
    public void createHistoricForDay(){
        //populateExercises(events);
        mAdapter = new ExpandableListExerciseHistoryAdapter(
                getActivity(), // The current context (this activity)
                R.layout.list_item_meal_historic, // The name of the layout ID.
                R.id.list_meal_name,R.id.checkbox_meal_done,
                MainActivity.fitsdates, MainActivity.exercises, // The ID of the textview to populate.
                MainActivity.gym_exes, MainActivity.history_today);

        listView = (ExpandableListView) getActivity().findViewById(R.id.listView_exercise_schedule);
        listView.setAdapter(mAdapter);
    }

    /**
     * hide buttons that must not be shown if there is some activity scheduled
     */
    public void hideButtons(){
        ButtonFloat buttonFloat = (ButtonFloat) getActivity().findViewById(R.id.create_schedule_exe_button);
        buttonFloat.setVisibility(View.INVISIBLE);
        TextView textView = (TextView) getActivity().findViewById(R.id.create_schedule_exe_text);
        textView.setVisibility(View.INVISIBLE);
    }



}
