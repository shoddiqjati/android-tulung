package com.onevest.dev.tulung.main.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onevest.dev.tulung.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningFragment extends Fragment {


    public WarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_warning, container, false);
    }

}