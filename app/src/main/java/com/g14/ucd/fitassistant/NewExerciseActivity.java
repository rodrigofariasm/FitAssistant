package com.g14.ucd.fitassistant;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.g14.ucd.fitassistant.models.Activity;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class NewExerciseActivity extends AppCompatActivity {
    TableLayout tableExercisesGym;
    EditText description;
    ImageButton newOption;
    Button save;
    List<Exercise> exercises;
    Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        exercises = new ArrayList<Exercise>();

        tableExercisesGym = (TableLayout) findViewById(R.id.tableExercises);
        description = (EditText) findViewById(R.id.editText_description_other);
        newOption = (ImageButton) findViewById(R.id.button_new_opt_gym_exercise);
        save = (Button) findViewById(R.id.button_save_exercise);
        gym = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewOption(View v){

        TableRow newRow = new TableRow(getBaseContext());
        EditText nameExercise = new EditText(getBaseContext());
        EditText series = new EditText(getBaseContext());
        EditText repetitions = new EditText(getBaseContext());

        nameExercise.setHint("repetitions");
        series.setHint("series");
        repetitions.setHint("name");

        ImageButton deleteOption = new ImageButton(getBaseContext());

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

    public void saveActivity(View view){
        // Is the button now checked?
        RadioGroup rd = (RadioGroup) findViewById(R.id.radio_group_type);
        int checked = rd.getCheckedRadioButtonId();

        EditText name  = (EditText) findViewById(R.id.editText_name_exercise);
        Activity activity = new Activity();
        activity.setName(name.getText().toString());

        // Check which radio button was clicked
        switch(checked) {
            case R.id.radioButtonGym:
                Gym gym = (Gym) activity;
                gym.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            saveArrayExercises();
                        } else {
                            Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                        }
                    }
                });
            break;
            case R.id.radioButtonOther:
                EditText description  = (EditText) findViewById(R.id.editText_description_other);
                Other other = (Other) activity;
                other.setDescription(description.getText().toString());
                other.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null){
                            Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                        }
                    }
                });
            break;
        }
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

    public void onRadioButtonClicked(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonGym:
                if (checked) {
                    tableExercisesGym.setVisibility(View.VISIBLE);
                    newOption.setVisibility(View.VISIBLE);
                    description.setVisibility(View.INVISIBLE);
                    save.setVisibility(View.VISIBLE);
                }
                    break;
            case R.id.radioButtonOther:
                if (checked) {
                    tableExercisesGym.setVisibility(View.INVISIBLE);
                    newOption.setVisibility(View.INVISIBLE);
                    description.setVisibility(View.VISIBLE);
                    save.setVisibility(View.VISIBLE);
                }
                    break;
        }
    }
}
