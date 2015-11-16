package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Task;

public class NewDietActivity extends AppCompatActivity {

    private Diet newDiet;
    private List<Meal> meals;
    EditText descriptionField;
    EditText nameField;
    String newDietId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet);

        if (!isUpdate()) {
            newDiet = new Diet();
            meals = new ArrayList<Meal>();
            newDietId = null;
            descriptionField = (EditText) findViewById(R.id.editText_description_diet);
            nameField = (EditText) findViewById(R.id.editText_name_diet);
        }
    }

    private boolean isUpdate() {
        //Para recuperar os dados do Bundle em outra FitActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String objectId = extras.getString("diet");
            Log.d("FitAssistant: ", "ObjectID: " + objectId);
            if (objectId != null) {
                ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(objectId, new GetCallback<Diet>() {
                    @Override
                    public void done(Diet diet, final ParseException exception) {
                        if (exception == null) { // found diet
                            newDiet = diet;
                            fillFields();
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
        return false;
    }

    private void fillFields() {
        if (newDiet != null) {
            EditText name = (EditText) findViewById(R.id.editText_name_diet);
            EditText description = (EditText) findViewById(R.id.editText_description_diet);
            name.setText(newDiet.getName());
            description.setText(newDiet.getDescription());

            ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            query.whereEqualTo("dietId", newDiet.getObjectId());
            query.findInBackground(new FindCallback<Meal>() {
                @Override
                public void done(List<Meal> meals, ParseException exception) {
                    if (exception == null) { // found meals
                        for (Meal meal : meals) {
                            Button button = (Button) findViewById(getButtonOptionId(meal.getType()));
                            for (String option : meal.getOptions()) {
                                addNewOption(button, option, meal.getObjectId());
                            }
                        }
                    } else if (exception != null) {
                        Log.d("FitAssistant", "Error finding meals of diets: " + exception.getMessage());
                    }

                }
            });
        }
    }

    private int getButtonOptionId(int code) {
        switch (code) {
            case 1:
                return R.id.button_new_opt_breakfast;
            case 2:
                return R.id.button_new_opt_lunch;
            case 3:
                return R.id.button_new_opt_dinner;
            case 4:
                return R.id.button_new_opt_snack;
        }
        return code;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_diet, menu);
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


    public void addNewOption(View view) {
        addNewOption(view, null, null);
    }

    /* Method to add new Edit text box for options in the categories of the diet
    **/
    public void addNewOption(View view, String option, String mealId) {
        TableLayout tableoptions = null;
        TableRow newRow = new TableRow(getBaseContext());

        EditText editText = new EditText(getBaseContext());
        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);
        newRow.addView(editText);

        ImageButton buttonDelete = new ImageButton(getBaseContext());
        buttonDelete.setImageResource(R.drawable.ic_trash_blue);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOption(view);
            }
        });
        newRow.addView(buttonDelete);


        if (option != null && mealId != null) { // if is update diet the option will be filled already
            editText.setText(option);
            editText.setTag(mealId);
        } else {
            editText.setHint("option");
        }

        switch (view.getId()) {
            case R.id.button_new_opt_breakfast:
                tableoptions = (TableLayout) findViewById(R.id.table_breakfast);
                break;
            case R.id.button_new_opt_lunch:
                tableoptions = (TableLayout) findViewById(R.id.table_lunch);
                break;
            case R.id.button_new_opt_dinner:
                tableoptions = (TableLayout) findViewById(R.id.table_dinner);
                break;
            case R.id.button_new_opt_snack:
                tableoptions = (TableLayout) findViewById(R.id.table_snack);
                break;
        }

        buttonDelete.setTag(editText.getId());
        tableoptions.addView(newRow);
    }

    public void deleteOption(View view) {
        View row = (View) view.getParent();
        TableLayout table = (TableLayout) row.getParent();
        table.removeView(row);
    }

    public void saveDiet(View view) {
        newDiet.setUser(ParseUser.getCurrentUser());
        newDiet.setDescription(descriptionField.getText().toString());
        newDiet.setName(nameField.getText().toString());
        newDiet.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    createMeal(R.id.table_breakfast);
                    createMeal(R.id.table_lunch);
                    createMeal(R.id.table_dinner);
                    createMeal(R.id.table_snack);
                    try {
                        ParseObject.saveAll(meals);
                    } catch (ParseException parseE) {
                        newDiet.deleteInBackground();
                        Log.d("FITASSISTANT", "Error saving meals " + parseE.getMessage());
                    }
                }
            }
        });
        Intent intent = new Intent(NewDietActivity.this, DietActivity.class);

    }


    private void createMeal(int id) {
        Log.d("FITASSISTANT.saveMeal", "newDietId:" + newDietId);
        Log.d("FITASSISTANT.saveMeal", "newDietId:" + newDiet.getObjectId());

        String mealId = null;
        Meal meal = new Meal();

        TableLayout table = (TableLayout) findViewById(id);
        int size = table.getChildCount();
        if(size > 1) { // if there's any option registred for the meal . proceed to save. 1 = textView with the type

            for (int i = 1; i < size; i++) {
                TableRow row = (TableRow) table.getChildAt(i);
                EditText optionText = (EditText) row.getChildAt(0);
                meal.addOption(optionText.getText().toString());
                if (i == 1 && optionText.getTag() != null) {
                    mealId = (String) optionText.getTag();
                }
            }

            if (mealId != null) {
                meal.setObjectId(mealId);
            }

            meal.setType((Integer) table.getTag());
            meal.setUser(ParseUser.getCurrentUser());
            meal.setDietID(newDiet.getObjectId());
            meals.add(meal);

        }
    }
}