package com.ggez.pgtrackerapp.modules.qr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ggez.pgtrackerapp.R;

/**
 * Created by katleen on 8/9/17.
 */
public class QrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_qr);
    }
}