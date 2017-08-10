package com.ggez.pgtrackerapp.modules.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.firebase.DynamicLinkHelper;
import com.ggez.pgtrackerapp.models.Food;
import com.ggez.pgtrackerapp.modules.QRActivity;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.dynamiclinks.DynamicLink;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by katleen on 8/9/17.
 */
public class CreateMenuFragment extends Fragment {
    private final String TAG = "CreateMenuFragment";
    MainActivity mainActivity;

    private FirebaseListAdapter<Food> mAdapter;
    private ProgressBar mProgressBar;

    private String dateToday;
    private String mealSelected;


    @BindView(R.id.btn_add_food)
    Button tvUserName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_create_menu, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        dateToday = df.format(c.getTime());

        Spinner spinner = view.findViewById(R.id.meal_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mainActivity, R.array.meals_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mealSelected = (String) adapterView.getItemAtPosition(i);
                System.out.print("Type of Meal Selected: " + mealSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                System.out.print("No meal type selected");
            }
        });

        final ListView foodView = view.findViewById(R.id.listFood);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query foodRefSortbyName = ref.child("foodmenu").orderByChild("name");

        mAdapter = new FirebaseListAdapter<Food>(mainActivity, Food.class, android.R.layout.simple_list_item_multiple_choice, foodRefSortbyName) {
            @Override
            protected void populateView(View view, Food food, int i) {
                ((TextView) view.findViewById(android.R.id.text1)).setText(food.getName());
                /*ImageView foodImg = (ImageView) view.findViewById(R.id.foodImg);
                Picasso.with(getApplicationContext()).load(food.getPhotoUrl()).into(foodImg);*/

                mProgressBar.setVisibility(ProgressBar.GONE);
            }

        };

        foodView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        foodView.setAdapter(mAdapter);

        Button createMenu = view.findViewById(R.id.createMenu);
        createMenu.setOnClickListener(view1 -> {

            SparseBooleanArray checked = foodView.getCheckedItemPositions();

            if (checkSelectedItems(checked)) {
                for (int i = 0; i < checked.size(); i++) {
                    if (checked.valueAt(i)) {
                        int position = checked.keyAt(i);
                        System.out.println("POSITION SELECTED: " + position);
                        System.out.println("DATABASE REF OF SELECTED: " + mAdapter.getRef(position).getKey());

                        if (getMealSelected(mealSelected) != null) {
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            rootRef.child("dailymenu").child(dateToday).child(getMealSelected(mealSelected))
                                    .child(mAdapter.getRef(position).getKey()).setValue(true, (databaseError, databaseReference) -> {
                                if (databaseError != null) {
                                    Toast.makeText(mainActivity, "Error in saving daily menu", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mainActivity, "Successfully saved!", Toast.LENGTH_SHORT).show();
                                    generateQRCode(dateToday, getMealSelected(mealSelected));
                                }
                            });
                        } else {
                            Toast.makeText(mainActivity, "Select meal type", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            } else {
                Toast.makeText(mainActivity, "Select food in the list", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }

    @OnClick(R.id.btn_add_food)
    void onClickAddFood(){
        mainActivity.changeFragment(new CreateFoodFragment(),false);
    }


    private String getMealSelected(String selectedMeal) {

        if (selectedMeal.equalsIgnoreCase("Morning Bites")) {
            return "morn_bites";
        } else if (selectedMeal.equalsIgnoreCase("Breakfast")) {
            return "breakfast";
        } else if (selectedMeal.equalsIgnoreCase("Lunch")) {
            return "lunch";
        } else if (selectedMeal.equalsIgnoreCase("Afternoon Bites")) {
            return "aft_bites";
        } else if (selectedMeal.equalsIgnoreCase("Dinner")) {
            return "dinner";
        }

        return null;

    }

    public boolean checkSelectedItems(SparseBooleanArray checkedItems) {

        boolean containsTrue = false;
        for (int i = 0; i < checkedItems.size(); i++) {
            if (checkedItems.valueAt(i) == true) {
                containsTrue = true;
                break;
            }
        }
        return containsTrue;

    }

    void generateQRCode(String date, String meal){
        DynamicLink.Builder fdlBuilder = new DynamicLinkHelper().dynamicLinkBuilder(getActivity(), "https://pgtrackerapp.com/menu/" + date + "/" + meal);
        String longFDL = fdlBuilder.buildDynamicLink().getUri().toString();
        Log.i(TAG, "dynamicLinkBuilder long FDL: " + longFDL);
        fdlBuilder.buildShortDynamicLink().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();
                        Log.i(TAG, "dynamicLinkBuilder short FDL: " + shortLink.toString());
                        Log.i(TAG, "dynamicLinkBuilder preview FDL: " + flowchartLink.toString());

                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.BUNDLE_FBDL, shortLink.toString());
                        startActivity(new Intent(getActivity(), QRActivity.class).putExtras(bundle));
                    }
                }).addOnFailureListener(e -> Log.e(TAG, "dynamicLinkBuilder Error: " + e));
    }
}
