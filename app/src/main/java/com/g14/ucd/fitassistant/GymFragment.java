package com.g14.ucd.fitassistant;

import android.annotation.TargetApi;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import bolts.Continuation;
import bolts.Task;

/**
 * Method used to create a Gym Model, which has Exercises.
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

    /**
     * Setup the Layout components, and start the count variable, that will inform how many rows
     * has at tableExercisesGym
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        count = 1;
        super.onViewCreated(view, savedInstanceState);
        exercises = new ArrayList<Exercise>();
        tableExercisesGym = (TableLayout) getActivity().findViewById(R.id.table_exercises_gym);
        description = (EditText) getActivity().findViewById(R.id.editText_description_other);
        save = (ButtonRectangle) getActivity().findViewById(R.id.button_save_exercise_gym);
        newOption = (ImageButton) getActivity().findViewById(R.id.button_new_opt_gym_exercise);

        save.setOnClickListener(new View.OnClickListener() {
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
        addNewOption(view);
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

    /**
     * Save the Gym model at Parse, but only if the input is correct.
     */
    public void saveActivity() {
        Log.d("checkinput", "" + checkInput());
        if (!checkInput()) {
            return;
        } else {
            EditText name = (EditText) getActivity().findViewById(R.id.edittext_name_exercise_gym);
            final Gym activity = new Gym();
            activity.setName(name.getText().toString());
            activity.setUser(ParseUser.getCurrentUser());

            activity.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        try {
                            saveArrayExercises(activity);
                        } catch (Exception e1) {
                            Log.d("FITASSISTANT", "Error updating exercise " + e1.getMessage());
                        }


                    } else {
                        Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                    }
                }

            });
        }
    }

    /**
     * After save the Gym object, this method will save the exercises linked to the Gym object.
     * @param activity
     */
        private void saveArrayExercises(Gym activity){
            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle(getString(R.string.progress_saving_exercise));
            dialog.show();
            int size = tableExercisesGym.getChildCount();
            for(int i = 1; i<size; i+=2){
                TableRow row = (TableRow) tableExercisesGym.getChildAt(i);
                EditText name = (EditText) row.getChildAt(1);
                TableRow row2 = (TableRow) tableExercisesGym.getChildAt(i+1);
                Spinner sections = (Spinner) row2.getChildAt(0);
                Spinner repetitions = (Spinner) row2.getChildAt(1);

                Exercise newExercise = new Exercise();
                newExercise.setUser(ParseUser.getCurrentUser());
                newExercise.setName(name.getText().toString());
                if (sections.getSelectedItemPosition() == 0|| sections.getSelectedItemPosition()==9) {
                    newExercise.setSections(0);
                } else {
                    newExercise.setSections(Integer.parseInt(sections.getSelectedItem().toString()));
                }
                if (repetitions.getSelectedItemPosition() == 0 || repetitions.getSelectedItemPosition() == 8) {
                    newExercise.setRepetitions(0);
                }else{
                    newExercise.setRepetitions(Integer.parseInt(repetitions.getSelectedItem().toString()));
                }
                newExercise.setActivityID(activity);
                exercises.add(newExercise);

            }
            try {
                ParseObject.saveAll(exercises);
                Intent intent = new Intent(getActivity(), ExerciseActivity.class );
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dialog.dismiss();
                startActivity(intent);
            }catch (Exception parseE){
                Log.d("FITASSISTANT", "Error saving exercises " + parseE.getMessage()) ;
            }
        }


    /**
     * Check if the input is correct before save.
     * @return
     */
    private boolean checkInput() {
        EditText name = (EditText) getActivity().findViewById(R.id.edittext_name_exercise_gym);
        Log.d("äff", ""+name.getText().toString().trim());
        if(name.getText().toString().trim().length() == 0){
            Toast.makeText(this.getContext(), "Gym must have a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        int size = tableExercisesGym.getChildCount();
        for(int i = 1; i<size; i+=2){
            TableRow row = (TableRow) tableExercisesGym.getChildAt(i);
            EditText namerow = (EditText) row.getChildAt(1);

            TableRow row2 = (TableRow) tableExercisesGym.getChildAt(i+1);
            Spinner sections = (Spinner) row2.getChildAt(0);
            if (namerow.getText().toString().length() == 0){
                deleteOption(namerow, sections);
                checkInput();
                break;
            }
        }
        Log.d("äff", ""+tableExercisesGym.getChildCount());
        if(tableExercisesGym.getChildCount() <= 1){
            Toast.makeText(this.getContext(), "Gym must have at least one exercise", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Add new row to the tableExercisesGym
     * @param v
     */
    @TargetApi(16)
    public void addNewOption(View v){

        TableRow newRow = new TableRow(getActivity().getBaseContext());
        TableRow newRow2 = new TableRow(getActivity().getBaseContext());
        TextView exe = new TextView(getActivity().getBaseContext());
        final EditText nameExercise = new EditText(getActivity().getBaseContext());
        Spinner series = new Spinner(getActivity().getBaseContext());
        Spinner repetitions= new Spinner(getActivity().getBaseContext());



        exe.setText("" + count + ".");
        exe.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        exe.setTextSize(20);
        nameExercise.setHint("name");
        nameExercise.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        nameExercise.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.primary));

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 3; //amount of columns you will span
        nameExercise.setLayoutParams(params);

        series.setPrompt("Series");
        ArrayAdapter<CharSequence> ser_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.series_array, android.R.layout.simple_spinner_item);
        repetitions.setPrompt("Repetitions");
        ArrayAdapter<CharSequence> rep_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.repetition_array, android.R.layout.simple_spinner_item);

        ser_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        series.setPopupBackgroundResource(R.drawable.spinner_background);
        series.setAdapter(ser_adapter);
        rep_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repetitions.setPopupBackgroundResource(R.drawable.spinner_background);
        repetitions.setAdapter(rep_adapter);




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

    /**
     * Delete a row from the table ExercisesGym
     * @param v
     * @param v2
     */
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
