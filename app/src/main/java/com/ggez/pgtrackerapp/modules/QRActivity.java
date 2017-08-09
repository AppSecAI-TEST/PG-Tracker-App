package com.ggez.pgtrackerapp.modules;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.modules.login.LoginActivity;
import com.ggez.pgtrackerapp.qr.encoder.QREncoder;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by omarmatthew on 8/9/17.
 * QR Encoder Activity
 */

public class QRActivity extends AppCompatActivity {
    private final String TAG = "QRActivity";

    @BindView(R.id.imageView_qr)
    ImageView imageViewQR;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_qr_encoder);
        ButterKnife.bind(QRActivity.this);

        try {
            imageViewQR.setImageBitmap(new QREncoder().encodeAsBitmap("https://www.google.com"));
        } catch (WriterException e) {
            Log.e(TAG, "QR Encode " + e);
        }
    }

    @OnClick(R.id.btn_ok)
    void onClickOk(){
        finish();
    }
}
