package com.example.tarun.uitsocieties.updates_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tarun.uitsocieties.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdatesFrag extends Fragment {


    public UpdatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_updates, container, false);
    }

}
