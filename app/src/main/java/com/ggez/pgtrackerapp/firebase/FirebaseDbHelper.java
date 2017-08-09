package com.ggez.pgtrackerapp.firebase;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.ggez.pgtrackerapp.AppController;
import com.ggez.pgtrackerapp.R;
import com.ggez.pgtrackerapp.models.User;
import com.ggez.pgtrackerapp.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import rx.Observable;

/**
 * Created by Omar Matthew Reyes on 8/8/16.
 * FirebaseDatabase helper
 */
public class FirebaseDbHelper {
    private final String TAG = "FirebaseDbHelper";
    DatabaseReference databaseReference;
    FirebaseStorage storage;

    public FirebaseDbHelper() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    /**
     * Write new user
     *
     * @param name   String
     * @param email  String
     */
    public void writeNewUser(String userId, String name, String email) {
//        User user = new User(name, email);
        User user = new User(name, email, "");
        databaseReference.child(Constants.FBDB_USERS).child(userId).setValue(user);
    }

    /**
     * Upload user profile photo
     *
     * @param uri Uri
     * @return Observable<String>
     */
    public Observable<String> uploadUserProfilePhoto(Uri uri) {
        return Observable.create(subscriber -> {
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReferenceFromUrl(
                    AppController.getsInstance().getString(R.string.storage_bucket));

            // Create a reference to "mountains.jpg"
            StorageReference imageRef = storageRef.child("images/" + uri.getLastPathSegment());

            Log.i(TAG, "uploadUserProfilePhoto Uri to file path " + uri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(uri);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                subscriber.onError(exception);
                Toast.makeText(AppController.getsInstance(), "Profile photo upload error", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "uploadUserProfilePhoto upload error " + exception);
            }).addOnSuccessListener(taskSnapshot -> {
                Log.i(TAG, "uploadUserProfilePhoto upload success");
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) subscriber.onNext(downloadUrl.toString());
                else
                    Toast.makeText(AppController.getsInstance(), "Profile photo URL is null", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.i(TAG, "Upload progress " + progress +"% done");
            });
        });
    }
}
