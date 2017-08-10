package com.ggez.pgtrackerapp.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ggez.pgtrackerapp.models.Food;
import com.ggez.pgtrackerapp.qr.decoder.IntentIntegrator;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by katleen on 8/9/17.
 */
public class CreateMenuFragment extends Fragment {

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

        Spinner spinner = (Spinner) view.findViewById(R.id.meal_spinner);
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

        final ListView foodView = (ListView) view.findViewById(R.id.listFood);
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

        Button createMenu = (Button) view.findViewById(R.id.createMenu);
        createMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SparseBooleanArray checked = foodView.getCheckedItemPositions();

                if (checkSelectedItems(checked)) {
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i) == true) {
                            int position = checked.keyAt(i);
                            System.out.println("POSITION SELECTED: " + position);
                            System.out.println("DATABASE REF OF SELECTED: " + mAdapter.getRef(position).getKey());


                            if (getMealSelected(mealSelected) != null) {
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                rootRef.child("dailymenu").child(dateToday).child(getMealSelected(mealSelected)).child(mAdapter.getRef(position).getKey()).setValue(true, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(mainActivity, "Error in saving daily menu", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(mainActivity, "Successfully saved!", Toast.LENGTH_SHORT).show();
                                        }
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



}
