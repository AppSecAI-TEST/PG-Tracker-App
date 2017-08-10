package com.ggez.pgtrackerapp.modules.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.models.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Created by katleen on 8/10/17.
 */
public class CreateFoodFragment extends Fragment {

    MainActivity mainActivity;

    private static final int REQUEST_IMAGE = 2;

    private Uri filePath;


    private EditText txtName;

    private ImageView imgFood;
    private Button browse;
    private Button upload;
    private Button done;

    private ProgressDialog pd;

    private FirebaseStorage storage;
    private FirebaseDatabase database;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_create_food, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();

        pd = new ProgressDialog(mainActivity);
        pd.setMessage("Uploading....");

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        txtName = (EditText) view.findViewById(R.id.eTxt_Name);

        imgFood = (ImageView) view.findViewById(R.id.imgFood);
        browse = (Button) view.findViewById(R.id.btn_browse);
        upload = (Button) view.findViewById(R.id.btn_upload);
        done = (Button) view.findViewById(R.id.btn_done);

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (filePath != null) {

                    if (!TextUtils.isEmpty(txtName.getText().toString()) && txtName.getText().toString() != null) {
                        pd.show();

                        Food tmpFood = new Food(txtName.getText().toString(), null);

                        DatabaseReference foodMenuRef = database.getReference().child("foodmenu");
                        foodMenuRef.push().setValue(tmpFood, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Log.d("TEST", "Complete");


                                if (databaseError == null) {
                                    final String key = databaseReference.getKey();
                                    StorageReference storageReference = storage.getReference("foodmenu").child(key).child(txtName.getText().toString());

                                    storageReference.putFile(filePath).addOnCompleteListener(mainActivity,
                                            new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        Food foodToInsert = new Food(txtName.getText().toString(), task.getResult().getDownloadUrl()
                                                                .toString());

                                                        DatabaseReference foodMenuRef = database.getReference().child("foodmenu");
                                                        foodMenuRef.child(key).setValue(foodToInsert);

                                                        pd.dismiss();

                                                        imgFood.setImageBitmap(null);
                                                        imgFood.setImageResource(0);
                                                        txtName.setText(null);

                                                        Toast.makeText(mainActivity, "Successfully uploaded!", Toast.LENGTH_SHORT).show();


                                                    } else {

                                                        pd.dismiss();

                                                        Log.w("TEST", "Image upload task was not successful.",
                                                                task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.e("TEST", "Unable to write message to database.",
                                            databaseError.toException());
                                }

                            }
                        });
                    } else {
                        Toast.makeText(mainActivity, "Please enter the name of the food", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mainActivity, "Select an image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


            Log.d("TEST", "Uri: " + filePath.toString());

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), filePath);

                //Setting image to ImageView
                imgFood.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
