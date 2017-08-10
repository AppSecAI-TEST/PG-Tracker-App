package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.models.User;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew on 8/9/17.
 * Modified by Kathleen Santiago
 * Home Fragment
 */

public class HomeFragment extends Fragment{
    private final String TAG = "HomeFragment";

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
        Bundle bundle = getActivity().getIntent().getExtras();
        mFirebaseUser  = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            Log.i(TAG, "user " + mFirebaseUser.getUid());
            if (bundle != null && getActivity().getIntent().hasExtra(Constants.BUNDLE_USER))
                getUserDetails(bundle.getParcelable(Constants.BUNDLE_USER));
            else getUserDetailsLogin(mFirebaseUser);
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

    void getUserDetails(User user){
        Log.i(TAG, "onDataChange user ldap: " + user.getLdap());
        Log.i(TAG, "onDataChange user email: " + user.getEmail());
        Log.i(TAG, "onDataChange user employeeType: " + user.getEmployeeType());
        Log.i(TAG, "onDataChange user photoURL: " + user.getPhotoUrl());
        tvUserName.setText(user.getLdap());
        switch (user.getEmployeeType()) {
            case 0:
                btnEat.setVisibility(View.GONE);
                btnHistory.setVisibility(View.GONE);
                btnMenu.setVisibility(View.VISIBLE);
                btnQr.setVisibility(View.GONE);
                break;
            case 1:
                btnEat.setVisibility(View.VISIBLE);
                btnHistory.setVisibility(View.GONE);
                btnMenu.setVisibility(View.GONE);
                btnQr.setVisibility(View.GONE);
                break;
            case 2:
                btnEat.setVisibility(View.VISIBLE);
                btnHistory.setVisibility(View.GONE);
                btnMenu.setVisibility(View.GONE);
                btnQr.setVisibility(View.GONE);
                break;
        }
    }

    public void getUserDetailsLogin(FirebaseUser mFirebaseUser) {
        final User user = new User();
        Query mQueryMatchMaker = FirebaseDatabase.getInstance().getReference().child(Constants.FBDB_USERS).child(mFirebaseUser.getUid());
        mQueryMatchMaker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.setLdap(dataSnapshot.child(Constants.FBDB_LDAP).getValue().toString());
                user.setEmail(dataSnapshot.child(Constants.FBDB_EMAIL).getValue().toString());
                user.setEmployeeType(Integer.parseInt(dataSnapshot.child(Constants.FBDB_EMPLOYEE_TYPE).getValue().toString()));
                user.setPhotoUrl(dataSnapshot.child(Constants.FBDB_PHOTO_URL).getValue().toString());
                getUserDetails(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "getUserDetails " + databaseError);
            }
        });
    }
}
