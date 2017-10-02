package com.onevest.dev.tulung.main.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.NotificationHelper;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.utils.Constants;

public class WaitActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    int id;
    private static final String TAG = WaitActivity.class.getSimpleName();
    NotificationHelper notificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);
        notificationHelper = new NotificationHelper(this);
        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.ID, 0);
        Log.d(TAG, "" + id);
        databaseReference.child(Constants.POST).child(String.valueOf(id)).child(Constants.POST_STATUS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().toString().equals(Constants.POST_WAITING)) {
                            Log.d(TAG, dataSnapshot.getValue().toString());
                            notificationHelper.sendNotification("Bantuan datang untukmu! Tetaplah di lokasimu hingga bantuan datang");
                            Intent intent = new Intent(WaitActivity.this, ConfirmActivity.class);
                            intent.putExtra(Constants.ID, id);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.d(TAG, dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }
}
