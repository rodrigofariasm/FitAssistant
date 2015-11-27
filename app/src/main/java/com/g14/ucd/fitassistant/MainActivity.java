package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.facebook.appevents.AppEventsLogger;
import com.g14.ucd.fitassistant.models.Diet;
import com.g14.ucd.fitassistant.models.DietEvent;
import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.ExerciseEvent;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Historic;
import com.g14.ucd.fitassistant.models.Meal;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] mDrawerTitles;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;
    static Intent mServiceIntent;
    public static ArrayList<DietEvent> events;
    public static ArrayList<ExerciseEvent> exeEvents;
    public static Historic history_today;
    public static Map<String, Boolean> mapMealsAte;
    public static Map<String, Boolean> exercisesPerformed;
    public static ArrayList<Meal> meals;
    public static TreeSet<Integer> idx;
    public static Exception e;
    public static ArrayList<FitActivity> exercises;
    public static HashMap<Meal, ArrayList<String>> opts;
    public static HashMap<FitActivity, ArrayList<Exercise>> gym_exes;
    public static HashMap<String, Date> dates;
    public static HashMap<String, Date> fitsdates;
    public ProgressDialog pd;
    public static boolean initialize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        setSupportActionBar(toolbar);
        mServiceIntent = new Intent(getApplicationContext(), NotificationFitAssistant.class);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerTitles = getResources().getStringArray(R.array.drawer_string_array)  ;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list_view);
        // Set the adapter for the list view
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerTitles);
        mDrawerList.setAdapter(mAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mActivityTitle = getTitle().toString();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pd = new ProgressDialog(this);
        pd.setTitle("Loading Meals");
        pd.show();


    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        final ProgressDialog pd = new ProgressDialog(this);

        findTodayHistoric();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        saveHistory();
        AppEventsLogger.deactivateApp(this);
    }

    private void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(MainActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openDietActivity() {
        Intent intent = new Intent(MainActivity.this, DietActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openExerciseActivity() {
        Intent intent = new Intent(MainActivity.this, ExerciseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void openGoalActivity() {
        Intent intent = new Intent(MainActivity.this, GoalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openScheduleExerciseActivity() {
        Intent intent = new Intent(MainActivity.this, ExerciseScheduleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openScheduleDietActivity() {
        Intent intent = new Intent(MainActivity.this, DietScheduleActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DietFragment(), "Diet");
        adapter.addFragment(new ExerciseFragment(), "Exercises");
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch(position){
            case(0)://diet
                openDietActivity();
                break;
            case(1)://exercise
                openExerciseActivity();
                break;
            case(2)://schedule diet
                openScheduleDietActivity();
                break;
            case(3):// schedule exercise
                openScheduleExerciseActivity();
                break;
            case(4): // goal
                openGoalActivity();
                break;
            case(5): // logout
                logout();
                break;
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    public void findTodayHistoric(){
        fitsdates = new HashMap<String, Date>();
        events = new ArrayList<DietEvent>();
        exercises = new ArrayList<FitActivity>();
        exeEvents = new ArrayList<ExerciseEvent>();
        mapMealsAte = new HashMap<>();
        gym_exes = new HashMap<FitActivity, ArrayList<Exercise>>();
        exercisesPerformed = new HashMap<>();
        Calendar day = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        ParseQuery<Historic> query = ParseQuery.getQuery("Historic");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("date", Application.singleton_date);
        query.getFirstInBackground(new GetCallback<Historic>() {
            @Override
            public void done(Historic object, ParseException e2) {
                if (e2 == null) {
                    history_today = object;
                    mapMealsAte = history_today.getMealsAte();
                    exercisesPerformed = history_today.getExercisesDone();
                    setupHistory(false);
                } else {
                    history_today = new Historic();
                    try {
                        history_today.setDate(Application.singleton_date);
                        setupHistory(true);
                    } catch (Exception e1) {
                        Log.d(CommonConstants.DEBUG_TAG, "Couldn't create history today");
                    }
                }
            }
        });

    }

    private Exception setupHistory(final boolean newMealsAte) {
        GregorianCalendar today = new GregorianCalendar();
        List<Integer> todayOption = new ArrayList<>();
        int option = today.get(GregorianCalendar.DAY_OF_WEEK);
        todayOption.add(new Integer(option));
        ParseQuery<DietEvent> query = new ParseQuery<DietEvent>("DietEvent");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereContainsAll("weekDays", todayOption);
        query.findInBackground(new FindCallback<DietEvent>() {
            @Override
            public void done(List<DietEvent> dietEvents, ParseException exception) {
                e = exception;
                if (exception == null) { // found diets
                    Log.d("events", "" + dietEvents.size());
                    if (dietEvents.size() > 0) {
                        events.addAll(dietEvents);
                        findMeals(newMealsAte);
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });

        ParseQuery<ExerciseEvent> query2 = ParseQuery.getQuery("ExerciseEvent");
        query2.whereEqualTo("user", ParseUser.getCurrentUser());
        query2.whereContainsAll("weekdays", todayOption);
        query2.findInBackground(new FindCallback<ExerciseEvent>() {
            @Override
            public void done(List<ExerciseEvent> events, ParseException exception) {
                if (exception == null) { // found diets
                    pd.dismiss();
                    if (events.size() > 0) {
                        exeEvents.addAll(events);
                        Log.d("size exeevents", "" +exeEvents.size());
                        populateExercises();
                    }
                } else if (exception != null) {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
        return  e;
    }
    public static void setCheckBox(com.gc.materialdesign.views.CheckBox checkBox, Integer meal){
        if(!initialize){
            checkBox.setChecked(MainActivity.history_today.getMealsAte().get(""+meal));
        }

    }
    public void findMeals( final boolean newMealsAte){
        pd.show();
        idx = new TreeSet<Integer>();
        meals = new ArrayList<Meal>();
        opts = new HashMap<Meal, ArrayList<String>>();
        dates = new HashMap<String, Date>();
        ParseQuery<Meal> querym = ParseQuery.getQuery("Meal");
        querym.whereEqualTo("user", ParseUser.getCurrentUser());
        querym.whereEqualTo("dietID", events.get(0).getDietId());
        querym.findInBackground(new FindCallback<Meal>() {
            @Override
            public void done(List<Meal> meal, ParseException exception) {
                if (exception == null) { // found diets
                    e = exception;
                    Log.d("Meal size", "" + meal.size());
                    if (meal.size() > 0) {
                        for (Meal m : meal) {
                            idx.add(m.getType());
                        }
                        for (int i = 0; i < meal.size(); i++) {
                            Meal aux = meal.get(idx.first()-1);
                            meals.add(aux);
                            dates.put("" + (idx.first()), events.get(0).getTimes().get("" + (idx.first())));
                            opts.put(aux, (ArrayList<String>) aux.getOptions());
                            if (newMealsAte) {
                                mapMealsAte.put("" + (idx.pollFirst()), false);
                                history_today.setMealsAte(mapMealsAte);
                            }else{
                                idx.pollFirst();
                            }

                            pd.dismiss();
                            ((ViewPagerAdapter) viewPager.getAdapter()).getItem(0).onStart();
                        }
                    }

                } else if (exception != null) {
                    e = exception;
                    Log.d("FitAssistant", "Error: " + exception.getMessage());

                }
            }
        });
    }


    public void populateExercises(){

        for(final ExerciseEvent event : exeEvents){
            pd.show();
            history_today.setEventExercises(exeEvents);
            ParseQuery<Other> queryOther = ParseQuery.getQuery("Other");
            queryOther.whereEqualTo("user", ParseUser.getCurrentUser());
            queryOther.whereEqualTo("objectId", event.getExerciseID());

            ParseQuery<Gym> queryGym = ParseQuery.getQuery("Gym");
            queryGym.whereEqualTo("user", ParseUser.getCurrentUser());
            queryGym.whereEqualTo("objectId", event.getExerciseID());
            queryGym.findInBackground(new FindCallback<Gym>() {
                @Override
                public void done(List<Gym> gyms, ParseException e) {
                    if (gyms.size() > 0) {
                        exercises.add(gyms.get(0));
                        findExercises(gyms.get(0));
                        fitsdates.put(exercises.get(exercises.size() - 1).getObjectId(), event.getTime());
                        pd.dismiss();
                        ((ViewPagerAdapter) viewPager.getAdapter()).getItem(1).onStart();
                    }
                }
            });
            queryOther.findInBackground(new FindCallback<Other>() {
                @Override
                public void done(List<Other> others, ParseException e) {
                    if (others.size() > 0) {
                        exercises.add(others.get(0));
                        ArrayList<Exercise> gen = new ArrayList<>();
                        gen.add(new Exercise());
                        gym_exes.put(others.get(0), gen);
                        fitsdates.put(exercises.get(exercises.size() - 1).getObjectId(), event.getTime());
                        pd.dismiss();
                        ((ViewPagerAdapter) viewPager.getAdapter()).getItem(1).onStart();
                    }
                }
            });



        }



    }

    private void findExercises(final Gym gym) {

        ParseQuery<Exercise> queryExercise = ParseQuery.getQuery("Exercise");
        queryExercise.whereEqualTo("user", ParseUser.getCurrentUser());
        queryExercise.whereEqualTo("activityID", gym);
        queryExercise.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> activities, ParseException exception) {
                if (exception == null) {
                    gym_exes.put(gym, (ArrayList<Exercise>) activities);
                } else {
                    Log.d("FitAssistant", "Error: " + exception.getMessage());
                }
            }
        });
    }

    public void saveHistory(){
        if(events.size() > 0)  history_today.setEventId(events.get(0).getObjectId());
        if(exeEvents.size() > 0) history_today.setEventExercises(exeEvents);
        try{
            history_today.setUser(ParseUser.getCurrentUser());
            history_today.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                }
            });
        }catch (Exception e1){
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }

    }


}
