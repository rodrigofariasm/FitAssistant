package com.g14.ucd.fitassistant;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
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
import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.widget.TextView;

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
    ButtonRectangle save;
    List<Exercise> exercises;
    int count;
    Gym gym;


    private OnFragmentInteractionListener mListener;


    public GymFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {  super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gym, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        count = 1;
        super.onViewCreated(view, savedInstanceState);
        exercises = new ArrayList<Exercise>();
        tableExercisesGym = (TableLayout) getActivity().findViewById(R.id.table_exercises_gym);
        description = (EditText) getActivity().findViewById(R.id.editText_description_other);
        save = (ButtonRectangle) getActivity().findViewById(R.id.button_save_exercise_gym);
        newOption = (ImageButton) getActivity().findViewById(R.id.button_new_opt_gym_exercise);

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });
        newOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOption(v);
            }
        });
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

     public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public void saveActivity() {

        EditText name = (EditText) getActivity().findViewById(R.id.edittext_name_exercise_gym);
        Gym activity = new Gym();
        activity.setName(name.getText().toString());
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
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getString(R.string.progress_saving_exercise));
        dialog.show();
        int size = tableExercisesGym.getChildCount();
        for(int i = 1; i<size; i+=2){
            TableRow row = (TableRow) tableExercisesGym.getChildAt(i);
            EditText name = (EditText) row.getChildAt(0);
            TableRow row2 = (TableRow) tableExercisesGym.getChildAt(i+1);
            EditText sections = (EditText) row2.getChildAt(0);
            EditText repetitions = (EditText) row2.getChildAt(1);

            Exercise newExercise = new Exercise();
            newExercise.setName(name.getText().toString());
            newExercise.setSections(Integer.parseInt(sections.getText().toString()));
            newExercise.setRepetitions(Integer.parseInt(repetitions.getText().toString()));
            newExercise.setActivityID(gym.getObjectId());
            exercises.add(newExercise);
        }
        try {
            ParseObject.saveAll(exercises);
            dialog.dismiss();
            Intent intent = new Intent(getActivity(), ExerciseActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }catch (ParseException parseE){
            gym.deleteInBackground();
            Log.d("FITASSISTANT", "Error saving exercises " + parseE.getMessage()) ;
        }

    }
    public void addNewOption(View v){

        TableRow newRow = new TableRow(getActivity().getBaseContext());
        TableRow newRow2 = new TableRow(getActivity().getBaseContext());
        TextView exe = new TextView(getActivity().getBaseContext());
        final EditText nameExercise = new EditText(getActivity().getBaseContext());
        EditText series = new EditText(getActivity().getBaseContext());
        final EditText repetitions = new EditText(getActivity().getBaseContext());
        InputFilter[] filter = new InputFilter[3];
        InputFilter[] filtername = new InputFilter[1];
        filter[0] = new InputFilter.LengthFilter(2);
        filter[2] = new InputFilter.LengthFilter(2);
        filter[1] = new InputFilter.LengthFilter(2);
        filtername[0] = new InputFilter.LengthFilter(40);

        exe.setText("" + count+ ".");
        exe.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        exe.setTextSize(20);
        nameExercise.setHint("name");
        nameExercise.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        nameExercise.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        nameExercise.setFilters(filtername);

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 3; //amount of columns you will span
        nameExercise.setLayoutParams(params);

        series.setHint("series");
        series.setInputType(InputType.TYPE_CLASS_NUMBER);
        series.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        series.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        series.setFilters(filter);
        repetitions.setHint("repetitions");
        repetitions.setInputType(InputType.TYPE_CLASS_NUMBER);
        repetitions.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        repetitions.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        repetitions.setFilters(filter);



        ImageButton deleteOption = new ImageButton(getActivity().getBaseContext());

        deleteOption.setImageResource(R.drawable.ic_trash_blue);
        deleteOption.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.transparency));
        deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOption(view, nameExercise);
            }
        });
        count++;
        newRow.addView(exe);
        newRow.addView(nameExercise);

        newRow2.addView(series);
        newRow2.addView(repetitions);
        newRow2.addView(deleteOption);


        tableExercisesGym.addView(newRow);
        tableExercisesGym.addView(newRow2);
    }

    public void deleteOption(View v, View v2){
        View rootView = (View) v.getParent();
        View rootView2 = (View) v2.getParent();
        tableExercisesGym.removeView(rootView);
        tableExercisesGym.removeView(rootView2);
        int size = tableExercisesGym.getChildCount();
        if(size > 1){
            for(int i = 1; i<size; i+=2) {
                TableRow row = (TableRow) tableExercisesGym.getChildAt(i);
                TextView tv = (TextView) row.getVirtualChildAt(0);
                tv.setText(""+ ((i/2) +1) + ".");
            }
        }
        count--;


    }



}
