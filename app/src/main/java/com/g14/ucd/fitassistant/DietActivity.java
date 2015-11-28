package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.Meal;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.Dialog;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class reprensenting the Diet Activity, the main screen of the Diet
 * that lists all the users diets.
 */

public class DietActivity extends AppCompatActivity {

	/**
	 * Method that is call everytime the activity is created
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        initialize();
    }

	/**
	 * method that initialize all the values and makes the inicial query
	 * */
    private void initialize(){
        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle(getString(R.string.progress_loading_diets));
        dialog.show();
        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Diet>() {
            @Override
            public void done(List<Diet> diets, ParseException exception) {
                if (exception == null) { // found diets
                    listDiets(diets);
                    if (diets.size() == 0) {
                        showButtons();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                    error_dialog.show();
                }
                dialog.dismiss();
            }
        });
    }

	/**
	 * Method to show the list of diets
	 * */
    private void listDiets(List<Diet> diets){

       ListAdapter mAdapter = new ListAdapter(
                this, // The current context (this activity)
                R.layout.list_item_diet, // The name of the layout ID.
                R.id.list_item_name_diet,-1, R.id.button_update,R.id.button_delete, -1,// The ID of the textview to populate.
                diets);

        ListView listView = (ListView) findViewById(R.id.listView_diets);
        listView.setAdapter(mAdapter);

    }
	
	/**
	 * Method that shows the buttons and text message when there's no diet registered
	 * */
    private void showButtons(){
        ButtonFloat addbutton = (ButtonFloat) findViewById(R.id.button_add_diet);
        addbutton.setVisibility(View.VISIBLE);
        TextView noDietMessage = (TextView) findViewById(R.id.no_diet_message);
        noDietMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet, menu);
        return true;
    }

	/**
	 * Method that says what will happen if select an item on the action bar
	 * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                //openSettings();
                return true;
            case R.id.add_diet:
                addNewDiet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	/**
	 * Method to called by the button to create a new diet 
	 * */
    public void addNewDiet(View v){
        addNewDiet();
    }

	/**
	 * Method called to create a new diet which opens a the NewDietActivity
	 * */
    private void addNewDiet(){
        Intent intent = new Intent(DietActivity.this, NewDietActivity.class);
        startActivity(intent);
    }

	/**
	 * Method called by the update button. Open the NewDietActivity with
	 * the selected diet in order to edit them.
	 * */
    public void update(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId", objectId);

        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Diet>() {
            @Override
            public void done(Diet diet, final ParseException exception) {
                if (exception == null) { // found diet
                    Intent intent = new Intent(DietActivity.this, NewDietActivity.class);
                    intent.putExtra("diet", objectId);
                    startActivity(intent);
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method called by the delete button. Delete the selected diet.
	 * */
    public void delete(View v){
        final String objectId = (String) v.getTag();
        Log.d("TAG: objectId",objectId);

        final ProgressDialog dialog  = new ProgressDialog(this);
        final Dialog error_dialog = new Dialog(this, "No connection detected", "ok");
        dialog.setTitle("Deleting diet");
        dialog.show();
        ParseQuery<Diet> query = ParseQuery.getQuery("Diet");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.getInBackground(objectId, new GetCallback<Diet>() {
            @Override
            public void done(Diet diet, final ParseException exception) {
                if (exception == null) { // found diets

                    ParseQuery<Meal> query = ParseQuery.getQuery("Meal");
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.whereEqualTo("dietId", diet.getObjectId());
                    try {
                        List<Meal> meals = query.find();
                        ParseObject.deleteAll(meals);
                        diet.delete();
                        dialog.dismiss();
                        initialize();
                    } catch (ParseException e) {
                        Log.d("FitAssistant", "Error deleting diet" + e.getMessage());
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error finding diet with id " + objectId + ": " + exception.getMessage());
                }
            }
        });
    }

	/**
	 * Method that is called everytime the activity is started.
	 * */
    @Override
    protected void onStart(){
        super.onStart();
        initialize();
    }
}
