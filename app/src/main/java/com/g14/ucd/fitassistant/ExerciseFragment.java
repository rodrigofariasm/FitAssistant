package com.g14.ucd.fitassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.g14.ucd.fitassistant.notifications.CommonConstants;


public class ExerciseFragment extends Fragment {
    Intent mServiceIntent;
    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mServiceIntent = new Intent(getActivity(), MainActivity.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false);

    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, "Standard Notification");
        mServiceIntent.setAction(CommonConstants.ACTION_PING);
        mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, 10000);
        // Launches IntentService "PingService" to set timer.
        getActivity().startService(mServiceIntent);

    }

}
