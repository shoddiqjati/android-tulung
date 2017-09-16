package com.onevest.dev.tulung.main.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.onevest.dev.tulung.utils.PrefsManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends AppCompatActivity {

    //region BindView
    @BindView(R.id.post_account_name)
    TextView tvAccountName;
    @BindView(R.id.post_category)
    TextView tvPostCategory;
    @BindView(R.id.address_edittext)
    EditText etAddress;
    @BindView(R.id.post_description)
    EditText etDescription;
    @BindView(R.id.post_sum_people)
    EditText etPeople;

    @BindView(R.id.map_view)
    ImageView mapsView;
    @OnClick(R.id.map_view)
    public void mapViewHandler() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST);
    }

    @BindView(R.id.post_button)
    Button postBtn;
    @OnClick(R.id.post_button)
    public void postBtnHandler() {
        description = etDescription.getText().toString().trim();
        people = etPeople.getText().toString().trim();
        address = etAddress.getText().toString().trim();
        status = Constants.POST_HELP;
        databaseReference.child(Constants.POST_COUNTER).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = Integer.parseInt(dataSnapshot.getValue().toString());
                postData(counter + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });
    }
    //endregion

    private PrefsManager prefsManager;
    private double latitude, longitude;
    private String category, description, people, address, status;
    private int counter;

    private static final int MAP_REQUEST = 1009;
    private static final String TAG = PostActivity.class.getSimpleName();

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        category = intent.getStringExtra(Constants.CATEGORY);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);

        prefsManager = new PrefsManager(this);
        tvAccountName.setText(prefsManager.getName());
        tvPostCategory.setText(category);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST) {
            if (resultCode == RESULT_OK) {
                etAddress.setText(data.getStringExtra(Constants.ADDRESS));
                latitude = data.getDoubleExtra(Constants.LATITUDE, 0);
                longitude = data.getDoubleExtra(Constants.LONGITUDE, 0);
                Log.d(TAG, latitude + ", " + longitude);
            }
        }
    }

    private void postData(final int num) {
        Post post;
        if (latitude != 0 || latitude != 0.0 || longitude != 0 || longitude != 0.0) {
            post = new Post(
                    prefsManager.getUuid(), prefsManager.getName(), category, description, people,
                    status, address, String.valueOf(latitude), String.valueOf(longitude));
        } else {
            post = new Post(
                    prefsManager.getUuid(), prefsManager.getName(), category, description, people,
                    status, address, null, null);
        }
        databaseReference.child(Constants.POST).child(String.valueOf(num)).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Push Completed");
                        databaseReference.child(Constants.POST_COUNTER).setValue(num).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                });
    }
}
