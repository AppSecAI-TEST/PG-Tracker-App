package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Omar Matthew Reyes on 8/10/17.
 */

public class FoodPickerFragment extends Fragment {
    private final String TAG = "FoodPickerFragment";

    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_meal)
    TextView tvMeal;

    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_food_picker, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        if (getArguments().containsKey(Constants.BUNDLE_DATE) && getArguments().containsKey(Constants.BUNDLE_MEAL)) {
            Log.i(TAG, "Date: " + getArguments().getString(Constants.BUNDLE_DATE) + ", Meal: " + getArguments().getString(Constants.BUNDLE_MEAL));
            tvDate.setText(getArguments().getString(Constants.BUNDLE_DATE));
            tvMeal.setText(getArguments().getString(Constants.BUNDLE_MEAL));

        } else {
            Log.e(TAG, "No date and meal found!");
        }

        return view;
    }
}
