package com.g14.ucd.fitassistant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.g14.ucd.fitassistant.models.DietEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class DietFragment extends Fragment {


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
        GregorianCalendar today = new GregorianCalendar();
        List<Integer> todayOption = new ArrayList<>();
        int option = today.get(GregorianCalendar.DAY_OF_WEEK);
        todayOption.add(new Integer(option));

        ParseQuery<DietEvent> query = ParseQuery.getQuery("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereContainsAll("weekDays",todayOption);
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvents, ParseException exception) {
                if (exception == null) { // found diets
                    createHistoricForDay(dietEvents);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

    public void createHistoricForDay(List<DietEvent> dietEvents){

    }

}
