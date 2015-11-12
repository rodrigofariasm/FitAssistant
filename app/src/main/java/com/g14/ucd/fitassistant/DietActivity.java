package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g14.ucd.fitassistant.models.Diet;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");

        query.findInBackground(new FindCallback<Diet>() {
            @Override
            public void done(List<Diet> diets, ParseException exception) {
                if (exception == null && diets.size() > 0) { // found diets
                    hideButtons();
                    listDiets(diets);
                    Log.d("FitAssistant", "êêê + " + diets.size());
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }


    private TableRow createButtons(String objId){
        Button delete = new Button(getBaseContext());
        delete.setText("x");
        delete.setTag(objId);
        Button update = new Button(getBaseContext());
        update.setText("update");
        delete.setTag(objId);
        Button view = new Button(getBaseContext());
        view.setText("view");
        delete.setTag(objId);
        TableRow buttons = new TableRow(getBaseContext());
        buttons.addView(delete);
        buttons.addView(update);
        buttons.addView(view);

        return buttons;
    }

    private void listDiets(List<Diet> diets){
        List<String> viewDiets = new ArrayList<String>();
        TableLayout tableButtons = (TableLayout) findViewById(R.id.buttons_table);

        for(Diet diet : diets){
            String name = diet.getString("name");
            System.out.print("Diet: " + name);
            viewDiets.add(name);
            tableButtons.addView(createButtons(diet.getObjectId()));
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_diet, // The name of the layout ID.
                R.id.list_item_name_diet, // The ID of the textview to populate.
                viewDiets);

        ListView listView = (ListView) findViewById(R.id.listView_diets);
        listView.setAdapter(mAdapter);


    }



    private void hideButtons(){
        Button addbutton = (Button) findViewById(R.id.button_add_diet);
        addbutton.setVisibility(View.INVISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_diet_message);
        noDietMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                //openSettings();
                return true;
            case R.id.logout_option:
                logout();
                return true;
            case R.id.diet_option:
                openDietActivity();
                return true;
            case R.id.add_diet:
                addNewDiet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(DietActivity.this, DispatchActivity.class);
        startActivity(intent);
    }

    private void openDietActivity() {
        Intent intent = new Intent(DietActivity.this, DietActivity.class);
        startActivity(intent);
    }

    public void addNewDiet(View v){
        addNewDiet();
    }

    private void addNewDiet(){
        Intent intent = new Intent(DietActivity.this, NewDietActivity.class);
        startActivity(intent);
    }
}
