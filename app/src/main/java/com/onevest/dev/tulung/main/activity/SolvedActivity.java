package com.onevest.dev.tulung.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SolvedActivity extends AppCompatActivity {

    @BindView(R.id.problem_name)
    TextView tvName;

    @BindView(R.id.solved_rating)
    RatingBar ratingBar;

    @BindView(R.id.solved_send)
    Button sendBtn;

    @OnClick(R.id.solved_send)
    public void sendHandler() {
        int rating = ratingBar.getProgress();
        databaseReference.child(uuid).child(Constants.RATING).setValue(rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dbRef.child(Constants.POST).child(String.valueOf(id)).child(Constants.POST_STATUS)
                                .setValue(Constants.POST_DONE);
                        finish();
                    }
                });
        Log.d(TAG, String.valueOf(rating));
    }

    private static final String TAG = SolvedActivity.class.getSimpleName();
    private DatabaseReference databaseReference;
    private DatabaseReference dbRef;
    private String name, uuid;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solved);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS);
        dbRef = FirebaseDatabase.getInstance().getReference(Constants.POST);
        name = intent.getStringExtra(Constants.NAME);
        uuid = intent.getStringExtra(Constants.POST_UUID);
        id = intent.getLongExtra(Constants.ID, 0);
        Log.d(TAG, "" + id);
        tvName.setText(name);
    }
}
