package com.g14.ucd.fitassistant;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;

import com.g14.ucd.fitassistant.models.Exercise;
import com.g14.ucd.fitassistant.models.FitActivity;
import com.g14.ucd.fitassistant.models.Gym;
import com.g14.ucd.fitassistant.models.Other;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GeneralActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class GeneralActivityFragment extends android.support.v4.app.Fragment {
    TableLayout tableExercisesGym;
    EditText description;
    ImageButton newOption;
    Button save;
    List<Exercise> exercises;
    Gym gym;

    private OnFragmentInteractionListener mListener;

    public GeneralActivityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_activity, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public void saveActivity(View view) {
        // Is the button now checked?
        EditText name = (EditText) getActivity().findViewById(R.id.edittext_name_general_activity);
        FitActivity activity = new FitActivity();
        activity.setName(name.getText().toString());
        EditText description = (EditText) getActivity().findViewById(R.id.editText_description_other);
        Other other = (Other) activity;
        other.setDescription(description.getText().toString());
        other.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("FITASSISTANT", "Error saving other exercise " + e.getMessage());
                }
            }
        });


    }
}