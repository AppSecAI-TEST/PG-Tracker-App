package com.ggez.pgtrackerapp.modules.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.firebase.AppInviteHelper;
import com.ggez.pgtrackerapp.models.Food;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew Reyes on 8/10/17.
 * Modified by Mariz Salvador
 */

public class FoodPickerFragment extends Fragment {
    private final String TAG = "FoodPickerFragment";
    public static final int REQUEST_INVITE = 100;

    MainActivity mainActivity;
    @Inject
    FirebaseAuth mFirebaseAuth;

    private DatabaseReference rootRef;
    private ProgressBar mProgressBar;
    private Food foodSelected;
    private ListView dailyMenuList;
    private String dateToday;
    private String mealType;
    private String deepLink;

    @BindView(R.id.tv_select)
    TextView tvHeader;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_food_picker, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        String userId = mainActivity.mFirebaseAuth.getCurrentUser().getUid();

        if (getArguments().containsKey(Constants.BUNDLE_DATE) && getArguments().containsKey(Constants.BUNDLE_MEAL)) {
            Log.i(TAG, "Date: " + getArguments().getString(Constants.BUNDLE_DATE) + ", Meal: " + getArguments().getString(Constants.BUNDLE_MEAL));
            dateToday = getArguments().getString(Constants.BUNDLE_DATE);
            mealType = getArguments().getString(Constants.BUNDLE_MEAL);
            deepLink = getArguments().getString(Constants.BUNDLE_DEEPLINK);

            String header = getString(R.string.select_meal) + "\n" + mealType + " for " + dateToday;
            tvHeader.setText(header);

            mProgressBar = view.findViewById(R.id.progressBar);

            rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.child("foodhistory").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, "has user " + userId + " " + dataSnapshot.hasChild(userId));
                    Log.i(TAG, "has date " + dateToday + " " + dataSnapshot.child(userId).hasChild(dateToday));
                    Log.i(TAG, "has meal " + mealType + " " + dataSnapshot.child(userId).child(dateToday).hasChild(mealType));
                    if(dataSnapshot.hasChild(userId) && dataSnapshot.child(userId).hasChild(dateToday)
                            && dataSnapshot.child(userId).child(dateToday).hasChild(mealType)){
                        Toast.makeText(mainActivity, "You already ate " + mealType + " on " + dateToday, Toast.LENGTH_SHORT).show();
                        mainActivity.changeFragment(new HomeFragment(), true);
                    } else {
                        loadData(view);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Log.e(TAG, "No date and meal found!");
        }

        return view;
    }

    @OnClick(R.id.select_menu)
    void onMenuClicked(){
        if (foodSelected != null) {
            rootRef.child("foodhistory").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateToday).child(mealType).setValue(foodSelected.getId(), (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    Toast.makeText(mainActivity, "Error in saving daily menu", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Successfully saved!");

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_MEAL, foodSelected);
                    Fragment receiptFragment = new ReceiptFragment();
                    receiptFragment.setArguments(bundle);
                    mainActivity.changeFragment(receiptFragment, true);

                }
            });
        }

    }

    @OnClick(R.id.btn_share)
    void onShareClicked(){
        if(deepLink != null && !deepLink.equals(""))
            startActivityForResult(new AppInviteHelper().appInviteTemplate(mainActivity, deepLink), REQUEST_INVITE);
        else Toast.makeText(mainActivity, "Deeplink is empty", Toast.LENGTH_SHORT).show();
    }

    void loadData(View view){
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
    }

    public void populateMenu(List<Food> dailyFoodMenu, View view) {

        dailyMenuList = view.findViewById(R.id.listDailyMenu);
        CustomFoodAdapter adapter = new CustomFoodAdapter(mainActivity, R.layout.listset1, dailyFoodMenu);
        dailyMenuList.setAdapter(adapter);

        mProgressBar.setVisibility(ProgressBar.GONE);


        dailyMenuList.setOnItemClickListener((adapterView, view1, i, l) -> {

            foodSelected = (Food) adapterView.getAdapter().getItem(i);
            System.out.println("ITEM SELECTED KEY: " + foodSelected.getId());
            System.out.println("ITEM SELECTED NAME: " + foodSelected.getName());
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
        FoodHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new FoodHolder();
            holder.foodImage = row.findViewById(R.id.foodImg);
            holder.foodName = row.findViewById(R.id.foodName);

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
