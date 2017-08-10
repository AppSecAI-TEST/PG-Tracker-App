package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggez.pgtrackerapp.R;

import butterknife.ButterKnife;

/**
 * Created by katleen on 8/10/17.
 */
public class CreateFoodFragment extends Fragment {

    MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_create_food, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        return view;

    }
}