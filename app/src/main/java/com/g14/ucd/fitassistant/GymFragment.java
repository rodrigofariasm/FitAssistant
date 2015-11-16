package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GymFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GymFragment} factory method to
 * create an instance of this fragment.
 */
public class GymFragment extends Fragment {
    TableLayout tableExercisesGym;
    EditText description;
    ImageButton newOption;
    Button save;
    List<Exercise> exercises;
    Gym gym;


    private OnFragmentInteractionListener mListener;


    public GymFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        exercises = new ArrayList<Exercise>();

        tableExercisesGym = (TableLayout) getActivity().findViewById(R.id.tableExercises);
        description = (EditText) getActivity().findViewById(R.id.editText_description_other);
        newOption = (ImageButton) getActivity().findViewById(R.id.button_new_opt_gym_exercise);
        save = (Button) getActivity().findViewById(R.id.button_save_exercise);
        gym = null;
        return inflater.inflate(R.layout.fragment_gym, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void saveActivity(View view) {
        // Is the button now checked?

        EditText name = (EditText) getActivity().findViewById(R.id.edittext_name_exercise_gym);
        FitActivity activity = new FitActivity();
        activity.setName(name.getText().toString());
        Gym gym = (Gym) activity;
        gym.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    saveArrayExercises();
                } else {
                    Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                }
            }
        });
    }
    private void saveArrayExercises(){

        int size = tableExercisesGym.getChildCount();
        for(int i = 1; i<size; i++){
            TableRow row = (TableRow) tableExercisesGym.getChildAt(i);
            EditText name = (EditText) row.getChildAt(0);
            EditText sections = (EditText) row.getChildAt(1);
            EditText repetitions = (EditText) row.getChildAt(2);

            Exercise newExercise = new Exercise();
            newExercise.setName(name.getText().toString());
            newExercise.setSections(Integer.parseInt(sections.getText().toString()));
            newExercise.setRepetitions(Integer.parseInt(repetitions.getText().toString()));
            newExercise.setActivityID(gym.getObjectId());
            exercises.add(newExercise);
        }
        try {
            ParseObject.saveAll(exercises);
        }catch (ParseException parseE){
            gym.deleteInBackground();
            Log.d("FITASSISTANT", "Error saving exercises " + parseE.getMessage()) ;
        }

    }
    public void addNewOption(View v){

        TableRow newRow = new TableRow(getActivity().getBaseContext());
        EditText nameExercise = new EditText(getActivity().getBaseContext());
        EditText series = new EditText(getActivity().getBaseContext());
        EditText repetitions = new EditText(getActivity().getBaseContext());

        nameExercise.setHint("repetitions");
        series.setHint("series");
        repetitions.setHint("name");

        ImageButton deleteOption = new ImageButton(getActivity().getBaseContext());

        deleteOption.setImageResource(R.drawable.ic_trash_blue);
        deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOption(view);
            }
        });

        newRow.addView(nameExercise);
        newRow.addView(series);
        newRow.addView(repetitions);
        newRow.addView(deleteOption);


        tableExercisesGym.addView(newRow);
    }

    public void deleteOption(View v){
        View rootView = (View) v.getParent();
        tableExercisesGym.removeView(rootView);
    }



}
