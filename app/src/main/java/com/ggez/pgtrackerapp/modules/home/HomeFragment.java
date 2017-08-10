package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ggez.pgtrackerapp.AppController;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.firebase.FirebaseDbHelper;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.iv_user_image)
    ImageView ivUserImage;
    @BindView(R.id.btn_history)
    Button btnHistory;
    @BindView(R.id.btn_eat)
    Button btnEat;
    @BindView(R.id.btn_menu)
    Button btnMenu;

    private User user;

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
            if (bundle != null && getActivity().getIntent().hasExtra(Constants.BUNDLE_USER)) {
                user = bundle.getParcelable(Constants.BUNDLE_USER);
                loadUserDetails(user);
            }
            else getUserDetailsLogin(mFirebaseUser);
        }

        ivUserImage.setOnClickListener(view1 -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Constants.RC_SELECT_PHOTO);
        });

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

    @OnClick(R.id.btn_menu)
    void onClickMenu(){
        mainActivity.changeFragment(new CreateMenuFragment(),false);
    }

    @OnClick(R.id.btn_logout)
    void logout(){
        mainActivity.userLogout();
    }

    void setUserProfileImage(Uri photo) {
        Glide.with(this).load(photo).centerCrop().into(ivUserImage);
    }

    void setUserProfileImage(String photo) {
        Glide.with(this).load(photo).centerCrop().into(ivUserImage);
    }

    void loadUserDetails(User user){
        this.user = user;
        Log.i(TAG, "onDataChange user ldap: " + user.getLdap());
        Log.i(TAG, "onDataChange user email: " + user.getEmail());
        Log.i(TAG, "onDataChange user employeeType: " + user.getEmployeeType());
        Log.i(TAG, "onDataChange user photoURL: " + user.getPhotoUrl());
        tvUserName.setText(user.getLdap());
        tvUserEmail.setText(user.getEmail());
        setUserProfileImage(user.getPhotoUrl());
        switch (user.getEmployeeType()) {
            case 0:
                btnEat.setVisibility(View.VISIBLE);
                btnHistory.setVisibility(View.VISIBLE);
                btnMenu.setVisibility(View.VISIBLE);
                break;
            case 1:
                btnEat.setVisibility(View.VISIBLE);
                btnHistory.setVisibility(View.GONE);
                btnMenu.setVisibility(View.GONE);
                break;
            case 2:
                btnEat.setVisibility(View.VISIBLE);
                btnHistory.setVisibility(View.GONE);
                btnMenu.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SELECT_PHOTO) {
            if (data != null)
                try {
                    Uri imageUri = data.getData();
                    setUserProfileImage(imageUri);
                    mainActivity.showProgress(getString(R.string.processing));
                    new FirebaseDbHelper().uploadUserProfilePhoto(imageUri, mFirebaseUser.getUid()).
                            observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {
                                    mainActivity.hideProgress();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "uploadUserProfilePhoto " + e);
                                    ivUserImage.setImageResource(R.drawable.ic_user);
                                    mainActivity.hideProgress();
                                }

                                @Override
                                public void onNext(String url) {
                                    Log.e(TAG, "uploadUserProfilePhoto success " + url);
                                    mainActivity.hideProgress();
                                    new FirebaseDbHelper().updateUserDetails(
                                            mFirebaseUser.getUid(), user.getLdap(),
                                            url, user.getEmployeeType(), user.getEmail());
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(AppController.getsInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void getUserDetailsLogin(FirebaseUser mFirebaseUser) {
        mainActivity.showProgress(getString(R.string.loading));
        final User user = new User();
        Query mQueryMatchMaker = FirebaseDatabase.getInstance().getReference().child(Constants.FBDB_USERS).child(mFirebaseUser.getUid());
        mQueryMatchMaker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.setLdap(dataSnapshot.child(Constants.FBDB_LDAP).getValue().toString());
                user.setEmail(dataSnapshot.child(Constants.FBDB_EMAIL).getValue().toString());
                user.setEmployeeType(Integer.parseInt(dataSnapshot.child(Constants.FBDB_EMPLOYEE_TYPE).getValue().toString()));
                user.setPhotoUrl(dataSnapshot.child(Constants.FBDB_PHOTO_URL).getValue().toString());
                loadUserDetails(user);
                mainActivity.hideProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadUserDetails " + databaseError);
                mainActivity.hideProgress();
            }
        });
    }
}
