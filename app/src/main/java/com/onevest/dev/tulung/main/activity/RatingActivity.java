package com.onevest.dev.tulung.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.models.Post;
import com.onevest.dev.tulung.utils.Constants;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RatingActivity extends AppCompatActivity {

    DatabaseReference databaseReference, dbRef;
    int id;
    String name, uuid;
    Post post;

    @BindView(R.id.solver_name)
    TextView tvName;

    @BindView(R.id.send)
    Button button;

    @BindView(R.id.rating)
    RatingBar ratingBar;

    @OnClick(R.id.send)
    public void sendHandler() {
        int rate = ratingBar.getProgress();
        Log.d("TAG", "" + rate);
        Log.d("UUID", uuid.toString());
        dbRef.child(uuid).child(Constants.RATING).setValue(rate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.ID, 0);
        dbRef = FirebaseDatabase.getInstance().getReference(Constants.USERS);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);
        Log.d("ID", "" + id);
        if (id != 0) {
            databaseReference.child(Constants.POST).child(String.valueOf(id))
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child(Constants.POST_NAME).getValue().toString();
                    uuid = dataSnapshot.child(Constants.HELPER).getValue().toString();
                    Log.d("UUID", uuid);
                    tvName.setText("yonif fafan iriano");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
