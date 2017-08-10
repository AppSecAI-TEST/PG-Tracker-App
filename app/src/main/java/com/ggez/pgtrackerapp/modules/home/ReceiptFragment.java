package com.ggez.pgtrackerapp.modules.home;


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

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.models.Food;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiptFragment extends Fragment {
    private final String TAG = "ReceiptFragment";

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_receipt, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        if (getArguments().containsKey(Constants.BUNDLE_MEAL)) {
            Food foodSelected = (Food) getArguments().getSerializable(Constants.BUNDLE_MEAL);

            System.out.println("FOOD NAME: " + foodSelected.getName());

            ImageView foodSelectedImg = (ImageView) view.findViewById(R.id.foodImgReceipt);
            Picasso.with(mainActivity).load(foodSelected.getPhotoUrl()).into(foodSelectedImg);

            TextView foodSelectedName = (TextView) view.findViewById(R.id.foodNameReceipt);
            foodSelectedName.setText(foodSelected.getName());

        }

        return view;
    }



}
