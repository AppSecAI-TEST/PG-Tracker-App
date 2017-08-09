package com.ggez.pgtrackerapp.modules.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ggez.pgtrackerapp.AppController;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.models.EmployeeType;
import com.ggez.pgtrackerapp.utils.Validator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Omar Matthew Reyes on 8/8/2017.
 */

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private final String TAG = "RegisterActivity";
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.til_password_confirm)
    TextInputLayout tilPasswordConfirm;
    @BindView(R.id.et_password_confirm)
    EditText etPasswordConfirm;
    private ProgressDialog mProgressDialog;

    @BindView(R.id.spinner_employee)
    Spinner spinnerEmployee;

    @Inject
    public FirebaseAuth mFirebaseAuth;
    @Inject
    public FirebaseAnalytics mFirebaseAnalytics;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<EmployeeType> employeeTypeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_register);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        AppController.getComponent(this).inject(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        employeeTypeList = new ArrayList<>();
        Query mQueryMatchMaker = databaseReference.child("employeeType");
        mQueryMatchMaker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "datasnapshot key: " + child.getKey() + ", value: " + child.getValue());
                    EmployeeType employeeType = new EmployeeType();
                    employeeType.setEmployeeTypeName(child.getKey());
                    employeeType.setEmployeeTypeId(Integer.parseInt(child.getValue().toString()));
                    employeeTypeList.add(employeeType);

                    ArrayAdapter<EmployeeType> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, employeeTypeList);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    populateSpinner(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "datasnapshot error " + databaseError);
            }
        });
    }

    @OnClick(R.id.btn_register)
    public void onClickRegister() {
        clearTilErrors();
        boolean error = false;
        if (!Validator.isEmailValid(etEmail.getText().toString())) {
            error = true;
            setUsernameError();
        }
        if (Validator.isPasswordValid(etPassword.getText().toString())) {
            if (!Validator.isPasswordConfirmed(etPassword.getText().toString(), etPasswordConfirm.getText().toString())) {
                error = true;
                setPasswordConfirmError();
            }
        } else {
            error = true;
            setPasswordError();
        }
        if (!error) {
            showProgress(getString(R.string.registering));
            mFirebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, task -> {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        hideProgress();
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registration Successful. " + etEmail.getText().toString(), Toast.LENGTH_SHORT).show();
                            if (mFirebaseAuth.getCurrentUser() != null) {
                                Bundle eventBundle = new Bundle();
                                eventBundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "password_auth");
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, eventBundle);
                                finish();
                            } else {
                                Toast.makeText(this, "FirebaseAuth user is NULL. " + Validator.getAuthErrorMessage(task), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failed " + etEmail.getText().toString() + "\nException: " + task.getException().toString());
                            Toast.makeText(this, "Registration Failed. " + Validator.getAuthErrorMessage(task), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void showProgress(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void setUsernameError() {
        tilEmail.setError(getString(R.string.error_invalid_email));
    }

    @Override
    public void setPasswordError() {
        tilPassword.setError(getString(R.string.error_invalid_password));
    }

    @Override
    public void setPasswordConfirmError() {
        tilPasswordConfirm.setError(getString(R.string.error_password_not_match));
    }

    @Override
    public void clearTilErrors() {
        tilEmail.setErrorEnabled(false);
        tilEmail.setError(null);
        tilPassword.setErrorEnabled(false);
        tilPassword.setError(null);
        tilPasswordConfirm.setErrorEnabled(false);
        tilPasswordConfirm.setError(null);
    }

    @Override
    public void populateSpinner(ArrayAdapter arrayAdapter) {
        spinnerEmployee.setAdapter(arrayAdapter);
    }
}