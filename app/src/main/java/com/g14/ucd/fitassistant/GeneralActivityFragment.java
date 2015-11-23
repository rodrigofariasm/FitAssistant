package com.g14.ucd.fitassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.g14.ucd.fitassistant.models.Other;
import com.g14.ucd.fitassistant.notifications.NotificationFitAssistant;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
public class GeneralActivityFragment extends android.support.v4.app.Fragment implements GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_PLACE_PICKER = 1;
    EditText name;
    EditText description;
    ButtonRectangle save;
    EditText goLocationEditText;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private OnFragmentInteractionListener mListener;
    private Intent mServiceIntent;

    public GeneralActivityFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_activity, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        
        super.onViewCreated(view, savedInstanceState);
        mServiceIntent = new Intent(getActivity().getApplicationContext(), NotificationFitAssistant.class);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        save = (ButtonRectangle) view.findViewById(R.id.button_save_exercise);
        goLocationEditText = (EditText) view.findViewById(R.id.general_activity_location);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveActivity();
            }
        });
        goLocationEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goLocation();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
       // mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        // The good stuff goes here.
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
        Toast.makeText(getActivity(), "Cannot connect with Internet, please check your connection", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            Toast.makeText(getActivity(), "Cannot connect with Internet, please check your connection", Toast.LENGTH_LONG).show();
            mResolvingError = true;
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

    public void saveActivity() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle(getString(R.string.progress_saving_exercise));
        dialog.show();
        Log.d("" + R.string.app_name, "trying Save");
        name = (EditText) getActivity().findViewById(R.id.edittext_name_general_activity);
        Other activity = new Other();
        activity.setName(name.getText().toString());
        description = (EditText) getActivity().findViewById(R.id.editText_description_other);
        if(!checkInput()){
            dialog.dismiss();
            return;
        }

        activity.setDescription(description.getText().toString().trim());
        activity.setLocation(goLocationEditText.getText().toString());
        activity.setUser(ParseUser.getCurrentUser());
        activity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e != null) {

                    Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                }else{
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), ExerciseActivity.class );
                    startActivity(intent);
                }
            }
        });
    }

    public void goLocation(){
        try{
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this.getActivity());
            getActivity().startActivityForResult(intent, REQUEST_PLACE_PICKER);
        }catch (Exception e){
            Log.d("FitAssitant", e.getMessage().toString());
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                goLocationEditText.setText(place.getName());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean checkInput(){
        if(name.getText().toString().trim().length() == 0 ){
            Toast.makeText(this.getContext(), "Activity must have a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}