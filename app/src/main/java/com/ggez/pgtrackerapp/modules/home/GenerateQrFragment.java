package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.qr.encoder.QREncoder;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by katleen on 8/9/17.
 * Modified by Omar Matthew Reyes
 */
public class GenerateQrFragment extends Fragment {

    private final String TAG = "GenerateQrFragment";


    MainActivity mainActivity;

    @BindView(R.id.imageView_qr)
    ImageView imageViewQR;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_generate_qr, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        try {
            imageViewQR.setImageBitmap(new QREncoder().encodeAsBitmap(getArguments().getString(Constants.BUNDLE_FBDL)));
        } catch (WriterException e) {
            Log.e(TAG, "QR Encode " + e);
        }

        return view;
    }

    @OnClick(R.id.btn_ok)
    void onClickOk() {
        mainActivity.changeFragment(new HomeFragment(), false);
    }
}
