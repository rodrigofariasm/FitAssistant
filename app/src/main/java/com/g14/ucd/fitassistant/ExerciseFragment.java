package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class ExerciseFragment extends Fragment {
    Intent mServiceIntent;
    List<FitActivity> exercises;
    Historic historicExercise;
    SimpleDateFormat dateFormatter;

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
        initialize();
    }

    private void initialize(){
        historicExercise = new Historic();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        exercises = new ArrayList<FitActivity>();

        GregorianCalendar today = new GregorianCalendar();
        List<Integer> todayOption = new ArrayList<>();
        int option = today.get(GregorianCalendar.DAY_OF_WEEK);
        todayOption.add(new Integer(option));

        ParseQuery<ExerciseEvent> query = ParseQuery.getQuery("ExerciseEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereContainsAll("weekdays", todayOption);
        query.findInBackground(new FindCallback<ExerciseEvent>() {
            @Override
            public void done(List<ExerciseEvent> events, ParseException exception) {
                if (exception == null) { // found diets
                    if (events.size() > 0) {
                        createHistoricForDay(events);
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

    public void createHistoricForDay(List<ExerciseEvent> events){
        populateExercises(events);
        ListAdapterHistoric mAdapter = new ListAdapterHistoric(
                getActivity(), // The current context (this activity)
                R.layout.list_item_exercises_historic, // The name of the layout ID.
                R.id.list_exercise_name,R.id.list_exercise_time,
                R.id.checkbox_exercise_done,// The ID of the textview to populate.
                events,exercises,null);

        ListView listView = (ListView) getActivity().findViewById(R.id.listView_exercise_Schedule);
        listView.setAdapter(mAdapter);
    }

    public void populateExercises(List<ExerciseEvent> events){
        for(ExerciseEvent event : events){
            ParseQuery<Other> queryOther = ParseQuery.getQuery("Other");
            queryOther.whereEqualTo("user", ParseUser.getCurrentUser());
            queryOther.whereEqualTo("objectId", event.getExerciseID());

            ParseQuery<Gym> queryGym = ParseQuery.getQuery("Gym");
            queryGym.whereEqualTo("user", ParseUser.getCurrentUser());
            queryGym.whereEqualTo("objectId", event.getExerciseID());

            try{
                List<Gym> gyms = queryGym.find();
                List<Other> others = queryOther.find();
                if(gyms.size() > 0){
                    exercises.add(gyms.get(0));
                } else if(others.size() > 0) {
                    exercises.add(others.get(0));
                }
            } catch (ParseException e){
                Log.d("FITASSISTANT", "Error" + e.getMessage());
            }
        }
    }
}
