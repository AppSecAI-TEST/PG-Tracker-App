package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggez.pgtrackerapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew on 8/9/17.
 */

public class HomeFragment extends Fragment{
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        return view;
    }

    @OnClick(R.id.btn_logout)
    void logout(){
        mainActivity.userLogout();
    }
}
