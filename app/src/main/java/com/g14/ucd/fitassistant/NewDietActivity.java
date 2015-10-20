package com.g14.ucd.fitassistant;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.MealEnum;

import java.util.ArrayList;

public class NewDietActivity extends AppCompatActivity {
    private Diet newDiet;
    private int countIdBreakfast;
    private int countIdLunch;
    private int countIdDinner;
    private int countIdSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_diet);

        newDiet = new Diet();
        countIdBreakfast = 0;
        countIdDinner = 0;
        countIdLunch = 0;
        countIdSnack = 0;
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

    private int generateIdOption(MealEnum type, int count){
        return type.getCode() * 100 + count;
    }

    /*
    **/
    public void addNewOption(View view){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.new_diet_layout);
        EditText editText = new EditText(getBaseContext());

        switch (view.getId()){
            case R.id.button_new_opt_breakfast:
                editText.setId(generateIdOption(MealEnum.BREAKFAST, countIdBreakfast));
                editText.setHint("Breakfast option " + (countIdBreakfast + 1));
                relativeLayout.addView(editText);
                changeLayout(MealEnum.BREAKFAST, R.id.textView_breakfast, countIdBreakfast, editText);
                countIdBreakfast++;
                break;
            case R.id.button_new_opt_lunch:
                editText.setId(generateIdOption(MealEnum.LUNCH, countIdLunch));
                editText.setHint("Lunch option " + (countIdLunch + 1));
                relativeLayout.addView(editText);
                changeLayout(MealEnum.LUNCH, R.id.textView_lunch, countIdLunch, editText);
                countIdLunch++;
                break;
            case R.id.button_new_opt_dinner:
                editText.setId(generateIdOption(MealEnum.DINNER, countIdDinner));
                editText.setHint("Dinner option " + (countIdDinner + 1));
                relativeLayout.addView(editText);
                changeLayout(MealEnum.DINNER, R.id.textView_dinner, countIdDinner, editText);
                countIdDinner++;
                break;
            case R.id.button_new_opt_snack:
                editText.setId(generateIdOption(MealEnum.SNACK, countIdSnack));
                editText.setHint("Snack option " + (countIdSnack + 1));
                relativeLayout.addView(editText);
                changeLayout(MealEnum.SNACK, R.id.textView_snack, countIdSnack, editText);
                countIdSnack++;
                break;
        }

        RelativeLayout.LayoutParams viewLayoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
        viewLayoutParams.addRule(RelativeLayout.BELOW, editText.getId());

    }

    private void changeLayout(MealEnum type, int textViewId,int count, EditText editText){
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText.getLayoutParams();
        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);
        if(count == 0){
            layoutParams.addRule(RelativeLayout.BELOW,textViewId);
        } else {
            layoutParams.addRule(RelativeLayout.BELOW,generateIdOption(type, count - 1));
        }
    }

    public void saveDiet(View view){


    }
}
