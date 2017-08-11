package com.ggez.pgtrackerapp.modules.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by katleen on 8/9/17.
 */
public class UserHistoryFragment extends Fragment {

    private final String TAG = "UserHistoryFragment";


    private MainActivity mainActivity;

    private Calendar myCalendar;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    EditText etDate;
    ImageButton ibSelectDate;
    ListView lvFoodHistory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_user_history, container, false);
        mainActivity = (MainActivity) getActivity();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser  = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser != null) {
            Log.i(TAG, "user " + mFirebaseUser.getUid());
        }



        myCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateToday = df.format(myCalendar.getTime());

        etDate = view.findViewById(R.id.et_date);
        etDate.setText(dateToday);

        ibSelectDate = view.findViewById(R.id.ib_select_date);
        ibSelectDate.setOnClickListener(view1 -> {
            Log.d(TAG, "onclick date");
            new DatePickerDialog(mainActivity, (view11, year, monthOfYear, dayOfMonth) -> {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateList();
            }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        lvFoodHistory = view.findViewById(R.id.lv_food_history);



        return view;

    }


    private void updateList() {
        String myFormat = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDate.setText(sdf.format(myCalendar.getTime()));

        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.FBDB_FOOD_HISTORY).child(mFirebaseUser.getUid()).child(etDate.getText().toString());
//        Query query = FirebaseDatabase.getInstance().getReference().child(Constants.FBDB_FOOD_HISTORY).child("PBAmSTciWIfT4gaXPpuukgg4mqe2").child(historyDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "datasnapshot: " + dataSnapshot.getValue());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

}
