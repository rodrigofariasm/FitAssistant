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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.MealEnum;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Task;

public class NewDietActivity extends AppCompatActivity {

    private Diet newDiet;
    private Meal meal;
    EditText descriptionField;
    EditText nameField;
    private Map<Integer,List<Integer>> idOptions;
    String newDietId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet);

        if(!isUpdate()){
            newDiet = new Diet();
            meal = null;
            newDietId = null;
            descriptionField = (EditText) findViewById(R.id.editText_description_diet);
            nameField = (EditText) findViewById(R.id.editText_name_diet);
            idOptions = new HashMap<Integer, List<Integer>>();
        } else {
            fillFields();
        }
    }

    private boolean isUpdate(){
        //Para recuperar os dados do Bundle em outra Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final String objectId = extras.getString("diet");
            if (objectId != null) {
                ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(objectId, new GetCallback<Diet>() {
                    @Override
                    public void done(Diet diet, final ParseException exception) {
                        if (exception == null) { // found diet
                            newDiet = diet;
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                        }
                    }
                });
            }
        }
        return false;
    }

    private void fillFields(){
        if(newDiet != null){
            EditText name = (EditText) findViewById(R.id.editText_name_diet);
            EditText description = (EditText) findViewById(R.id.editText_description_diet);

            for(final String idMeal : newDiet.getIdsMeals()){
                ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
                query.whereEqualTo("user", ParseUser.getCurrentUser());
                query.getInBackground(idMeal, new GetCallback<Meal>() {
                    @Override
                    public void done(Meal meal, final ParseException exception) {
                        if (exception == null) { // found diet
                            Button button = (Button) findViewById(getButtonOptionId(meal.getType()));
                            for(String option : meal.getOptions()){
                                addNewOption(button,option, idMeal);
                            }
                        } else if (exception != null) {
                            Log.d("FitAssistant", "Error finding meal: " + exception.getMessage());
                        }
                    }
                });
            }


        }
    }

    private int getButtonOptionId(int code){
        switch (code){
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

    private int generateIdOption(int code)    {
        if(!idOptions.containsKey(code) || idOptions.get(code).size() == 0){
            return code * 100;
        } else {
            int positionlast = idOptions.get(code).size();
            return idOptions.get(code).get(positionlast - 1) + 1;
        }

    }

    public void addNewOption(View view) {
        addNewOption(view,null,null);
    }
    /* Method to add new Edit text box for options in the categories of the diet
    **/
    public void addNewOption(View view, String option, String mealId){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.new_diet_layout);
        EditText editText = new EditText(getBaseContext());

        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);

        Button buttonDelete = new Button(getBaseContext());
        buttonDelete.setText("x");
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOption(view);
            }
        });
        relativeLayout.addView(buttonDelete);

        if(option != null && mealId != null){ // if is update diet the option will be filled already
            editText.setText(option);
            editText.setTag(mealId);
        } else{
            editText.setHint("option");
        }
        relativeLayout.addView(editText);

        switch (view.getId()){
            case R.id.button_new_opt_breakfast:
                editText.setId(generateIdOption(MealEnum.BREAKFAST.getCode()));
                buttonDelete.setId(generateIdOption(MealEnum.BREAKFAST.getCode()) * 10);
                addMealValue(MealEnum.BREAKFAST.getCode(), editText.getId());
                changeLayout(MealEnum.BREAKFAST.getCode(), R.id.textView_breakfast, editText, buttonDelete);
                break;
            case R.id.button_new_opt_lunch:
                editText.setId(generateIdOption(MealEnum.LUNCH.getCode()));
                buttonDelete.setId(generateIdOption(MealEnum.LUNCH.getCode()) * 10);
                addMealValue(MealEnum.LUNCH.getCode(), editText.getId());
                changeLayout(MealEnum.LUNCH.getCode(), R.id.textView_lunch, editText,buttonDelete);
                break;
            case R.id.button_new_opt_dinner:
                editText.setId(generateIdOption(MealEnum.DINNER.getCode()));
                buttonDelete.setId(generateIdOption(MealEnum.DINNER.getCode()) * 10);
                addMealValue(MealEnum.DINNER.getCode(), editText.getId());
                changeLayout(MealEnum.DINNER.getCode(), R.id.textView_dinner, editText,buttonDelete);
                break;
            case R.id.button_new_opt_snack:
                editText.setId(generateIdOption(MealEnum.SNACK.getCode()));
                buttonDelete.setId(generateIdOption(MealEnum.SNACK.getCode()) * 10);
                addMealValue(MealEnum.SNACK.getCode(), editText.getId());
                changeLayout(MealEnum.SNACK.getCode(), R.id.textView_snack, editText, buttonDelete);
                break;
        }

        buttonDelete.setTag(editText.getId());
        RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
        viewLayoutParams.addRule(RelativeLayout.BELOW, editText.getId());
    }

    private void addMealValue(int key, int option){
        if(!idOptions.containsKey(key)){
            idOptions.put(key, new ArrayList<Integer>());
        }
        idOptions.get(key).add(option);
    }

    private void changeLayout(int code, int textViewId, View view, Button button){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        RelativeLayout.LayoutParams layoutParamsButton = null;

        if(button != null) {
            layoutParamsButton = (RelativeLayout.LayoutParams) button.getLayoutParams();
            layoutParamsButton.addRule(RelativeLayout.ALIGN_RIGHT, R.id.editText_name_diet);
        }

        Integer id = new Integer(view.getId());
        List<Integer> ids = idOptions.get(code);
        int position = ids.indexOf(id);
        if(position == 0){
            layoutParams.addRule(RelativeLayout.BELOW,textViewId);
            if(button != null){
                layoutParamsButton.addRule(RelativeLayout.BELOW,textViewId);
            }
        } else {
            layoutParams.addRule(RelativeLayout.BELOW, ids.get(position - 1));
            if(button != null) {
                layoutParamsButton.addRule(RelativeLayout.BELOW, ids.get(position - 1));
            }
        }

    }

    public void deleteOption(View view){
        Object tag = view.getTag();
        int id = Integer.parseInt(tag.toString());
        EditText optionDelete = (EditText) findViewById(id);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.new_diet_layout);

        int textViewId = 0;
        int buttonOptionId = 0;
        int code = id/100;
        switch (code){
            case 1:
                textViewId = R.id.textView_breakfast;
                buttonOptionId = R.id.button_new_opt_breakfast;
                break;
            case 2:
                textViewId = R.id.textView_lunch;
                buttonOptionId = R.id.button_new_opt_lunch;
                break;
            case 3:
                textViewId = R.id.textView_dinner;
                buttonOptionId = R.id.button_new_opt_dinner;
                break;
            case 4:
                textViewId = R.id.textView_snack;
                buttonOptionId = R.id.button_new_opt_snack;
                break;
        }

        int positionDeleted = idOptions.get(code).indexOf(id);

        relativeLayout.removeView(view);
        relativeLayout.removeView(optionDelete);
        idOptions.get(code).remove(positionDeleted); // removing the id of the option deletede from de array of ids

        View viewBellow = null;
        if(positionDeleted == idOptions.get(code).size()){
            viewBellow = findViewById(buttonOptionId);
            RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams) viewBellow.getLayoutParams();
            if(positionDeleted ==  0){
                viewLayoutParams.addRule(RelativeLayout.BELOW, textViewId);
            }else{
                viewLayoutParams.addRule(RelativeLayout.BELOW, idOptions.get(code).get(positionDeleted-1));
            }
        }else{
            viewBellow = (View) findViewById(idOptions.get(code).get(positionDeleted));
            Button deleteButton = (Button) findViewById(viewBellow.getId()*10);
            changeLayout(code, textViewId, viewBellow, deleteButton);
        }
    }

    public void saveDiet(View view){
        newDiet.setUser(ParseUser.getCurrentUser());
        newDiet.setDescription(descriptionField.getText().toString());
        newDiet.setName(nameField.getText().toString());
        newDiet.setMeals(new ArrayList<String>());

        saveMeal();

        ParseACL acl = new ParseACL();
        acl.setPublicWriteAccess(true);
        acl.setPublicReadAccess(true);
        newDiet.setACL(acl);
        newDiet.pinInBackground();

        // Save the diet
        newDiet.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });

        Intent intent = new Intent(NewDietActivity.this,DietActivity.class);

    }

    private void saveMeal(){
        for (int mealType : idOptions.keySet()) {
            String mealId = null;
            ArrayList<String> options = new ArrayList<String>();
            for (int option : idOptions.get(mealType)) {
                EditText optionText = (EditText) findViewById(option);
                options.add(optionText.getText().toString());
                if(option % 100 == 0 && optionText.getTag() != null){
                    mealId = (String) optionText.getTag();
                }
            }

            if(mealId == null){
                meal = new Meal();
            } else {
                meal.setObjectId(mealId);
            }

            meal.addAllUnique("options", options);
            meal.setType(mealType);
            meal.setUser(ParseUser.getCurrentUser());

            // Save the meal
            meal.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    newDiet.addMeal(meal.getObjectId());
                }
            });
        }
    }
}
