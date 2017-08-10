package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by katleen on 8/9/17.
 */
public class CreateMenuFragment extends Fragment {

    MainActivity mainActivity;

    @BindView(R.id.btn_add_food)
    Button tvUserName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_create_menu, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        return view;

    }

    @OnClick(R.id.btn_add_food)
    void onClickAddFood(){
        mainActivity.changeFragment(new CreateFoodFragment(),false);
    }



}
