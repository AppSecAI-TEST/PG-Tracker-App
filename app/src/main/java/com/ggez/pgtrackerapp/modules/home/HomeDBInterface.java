package com.ggez.pgtrackerapp.modules.home;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by omarmatthew on 8/10/17.
 */

public interface HomeDBInterface {
    void onStart();
    void onSuccess(DataSnapshot data);
    void onFailed(DatabaseError databaseError);
}
