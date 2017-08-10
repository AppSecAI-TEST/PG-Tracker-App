package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew on 8/9/17.
 */

public class HomeFragment extends Fragment{

    MainActivity mainActivity;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_eat)
    Button btnEat;
    @BindView(R.id.btn_qr)
    Button btnQr;
    @BindView(R.id.btn_menu)
    Button btnMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_home, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser  = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            tvUserName.setText(mFirebaseUser.getDisplayName());
        }

        return view;
    }

    @OnClick(R.id.btn_history)
    void onClickHistory(){
        mainActivity.changeFragment(new UserHistoryFragment(),false);
    }

    @OnClick(R.id.btn_eat)
    void onClickEat(){
        new IntentIntegrator(mainActivity).initiateScan(-1);
    }

    @OnClick(R.id.btn_qr)
    void onClickQr(){
        mainActivity.changeFragment(new GenerateQrFragment(),false);

    }

    @OnClick(R.id.btn_menu)
    void onClickMenu(){
        mainActivity.changeFragment(new CreateMenuFragment(),false);

    }

    @OnClick(R.id.btn_logout)
    void logout(){
        mainActivity.userLogout();
    }
}
