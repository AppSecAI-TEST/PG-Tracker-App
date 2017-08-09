package com.ggez.pgtrackerapp.modules.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.modules.eat.EatActivity;
import com.ggez.pgtrackerapp.modules.history.HistoryActivity;
import com.ggez.pgtrackerapp.modules.menu.MenuActivity;
import com.ggez.pgtrackerapp.modules.qr.QrActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by katleen on 8/9/17.
 */
public class ProfileActivity extends AppCompatActivity {

    private final String TAG = "LoginActivity";
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity_profile);
        ButterKnife.bind(ProfileActivity.this);
    }

    @OnClick(R.id.btn_history)
    void onClickHistory(){
        startActivity(new Intent(this, HistoryActivity.class));
    }

    @OnClick(R.id.btn_eat)
    void onClickEat(){
        startActivity(new Intent(this, EatActivity.class));
    }

    @OnClick(R.id.btn_qr)
    void onClickQr(){
        startActivity(new Intent(this, QrActivity.class));
    }

    @OnClick(R.id.btn_menu)
    void onClickMenu(){
        startActivity(new Intent(this, MenuActivity.class));
    }
}
