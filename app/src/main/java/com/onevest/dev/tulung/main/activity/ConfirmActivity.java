package com.onevest.dev.tulung.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfirmActivity extends AppCompatActivity {

    @BindView(R.id.done_button)
    Button doneBtn;
    @OnClick(R.id.done_button)
    public void doneHandler() {
        if (id != 0) {
            databaseReference.child(Constants.POST).child(String.valueOf(id)).child(Constants.POST_STATUS)
                    .setValue(Constants.POST_SOLVED).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(ConfirmActivity.this, RatingActivity.class);
                    intent.putExtra(Constants.ID, id);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private int id;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);

        Intent intent = getIntent();
        id = intent.getIntExtra(Constants.ID, 0);
        Log.d("TAG", String.valueOf(id));
    }
}
