package com.ggez.pgtrackerapp.modules.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Omar Matthew Reyes on 8/10/17.
 */

public class FoodPickerFragment extends Fragment {
    private final String TAG = "FoodPickerFragment";

    MainActivity mainActivity;

    private DatabaseReference rootRef;
    private ProgressBar mProgressBar;
    private Food foodSelected;
    private ListView dailyMenuList;
    private String dateToday;
    private String mealType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_food_picker, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        if (getArguments().containsKey(Constants.BUNDLE_DATE) && getArguments().containsKey(Constants.BUNDLE_MEAL)) {
            Log.i(TAG, "Date: " + getArguments().getString(Constants.BUNDLE_DATE) + ", Meal: " + getArguments().getString(Constants.BUNDLE_MEAL));
            dateToday = getArguments().getString(Constants.BUNDLE_DATE);
            mealType = getArguments().getString(Constants.BUNDLE_MEAL);

            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("dailymenu").child(dateToday).child(mealType).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("Data: " + dataSnapshot.getValue());

                    final List<Food> dailyMenuList = new ArrayList<Food>();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        System.out.println("Data key: " + snap.getKey());

                        rootRef.child("foodmenu").child(snap.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Food foodDetails = dataSnapshot.getValue(Food.class);
                                System.out.println("FOOD NAME: " + foodDetails.getName());
                                dailyMenuList.add(foodDetails);
                                populateMenu(dailyMenuList, view);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("onCancelled foodmenu");
                            }
                        });

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("onCancelled dailymenu");
                }
            });

            Button selectMenu = (Button) view.findViewById(R.id.selectMenu);
            selectMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (foodSelected != null) {
                        rootRef.child("foodhistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateToday).child(mealType).setValue(foodSelected.getId(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(mainActivity, "Error in saving daily menu", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mainActivity, "Successfully saved!", Toast.LENGTH_SHORT).show();

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(Constants.BUNDLE_MEAL, foodSelected);
                                    Fragment receiptFragment = new ReceiptFragment();
                                    receiptFragment.setArguments(bundle);
                                    mainActivity.changeFragment(receiptFragment, true);

                                }
                            }
                        });
                    }

                }
            });


        } else {
            Log.e(TAG, "No date and meal found!");
        }

        return view;
    }

    public void populateMenu(List<Food> dailyFoodMenu, View view) {

        dailyMenuList = (ListView) view.findViewById(R.id.listDailyMenu);
        CustomFoodAdapter adapter = new CustomFoodAdapter(mainActivity, R.layout.listset1, dailyFoodMenu);
        dailyMenuList.setAdapter(adapter);

        mProgressBar.setVisibility(ProgressBar.GONE);


        dailyMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                foodSelected = (Food) adapterView.getAdapter().getItem(i);
                System.out.println("ITEM SELECTED KEY: " + foodSelected.getId());
                System.out.println("ITEM SELECTED NAME: " + foodSelected.getName());
            }
        });


    }
}

class CustomFoodAdapter extends ArrayAdapter<Food> {

    Context context;
    int layoutResourceId;
    List<Food> foodlist = null;

    public CustomFoodAdapter(Context context, int layoutResourceId, List<Food> foodlist) {
        super(context, layoutResourceId, foodlist);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.foodlist = foodlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        FoodHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FoodHolder();
            holder.foodImage = (ImageView) row.findViewById(R.id.foodImg);
            holder.foodName = (TextView) row.findViewById(R.id.foodName);

            row.setTag(holder);
        } else {
            holder = (FoodHolder) row.getTag();
        }

        Food food = foodlist.get(position);
        holder.foodName.setText(food.getName());
        Picasso.with(getContext()).load(food.getPhotoUrl()).into(holder.foodImage);

        return row;
    }

    static class FoodHolder {
        ImageView foodImage;
        TextView foodName;
    }
}
