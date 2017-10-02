package com.onevest.dev.tulung.main.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.utils.Constants;
import com.onevest.dev.tulung.utils.PrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    //region BindView
    @BindView(R.id.detail_account_name)
    TextView tvName;
    @BindView(R.id.detail_description)
    TextView tvContent;
    @BindView(R.id.detail_category)
    TextView tvCategory;
    @BindView(R.id.detail_address)
    TextView tvAddress;
    @BindView(R.id.detail_status)
    TextView tvStatus;

    @BindView(R.id.help_button)
    Button helpBtn;

    @OnClick(R.id.help_button)
    public void helpHandler() {
        databaseReference.child(Constants.POST).child(String.valueOf(id)).child(Constants.POST_STATUS).setValue(Constants.POST_WAITING);
        databaseReference.child(Constants.POST).child(String.valueOf(id)).child(Constants.HELPER)
                .setValue(prefsManager.getUuid()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Uri gmmIntentUri = Uri.parse(
                        "http://maps.google.com/maps?saddr=" + coordinates.latitude + "," + coordinates.longitude
                                + "&daddr=" + latitude + "," + longitude
                );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
    //endregion

    private Location location;
    private GoogleApiClient googleApiClient;
    private LatLng coordinates;
    private String latitude, longitude;
    private long id;
    private String name;
    private String uuid;

    private DatabaseReference databaseReference;

    private static final String TAG = DetailsActivity.class.getSimpleName();
    PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        prefsManager = new PrefsManager(this);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);

        Intent intent = getIntent();
        name = intent.getStringExtra(Constants.NAME);
        String desc = intent.getStringExtra(Constants.POST_DESCRIPTION);
        String address = intent.getStringExtra(Constants.POST_ADDRESS);
        String category = intent.getStringExtra(Constants.POST_CATEGORY);
        latitude = intent.getStringExtra(Constants.POST_LAT);
        longitude = intent.getStringExtra(Constants.POST_LONG);
        String people = intent.getStringExtra(Constants.POST_PEOPLE);
        String status = intent.getStringExtra(Constants.POST_STATUS);
        uuid = intent.getStringExtra(Constants.POST_UUID);
        id = intent.getLongExtra(Constants.ID, 0);

        if (id != 0) {
            tvName.setText(name);
            tvContent.setText(desc);
            tvAddress.setText(address);
            tvCategory.setText(category);
            if (status.equals(Constants.POST_HELP)) {
                tvStatus.setText("Status: masih membutuhkan bantuan");
            } else if (status.equals(Constants.POST_SOLVED)) {
                tvStatus.setText("Status: masalah sudah selesai");
                helpBtn.setEnabled(false);
            } else if (status.equals(Constants.POST_WAITING)){
                tvStatus.setText("Status: sudah ada yang membantu");
            } else {
                tvStatus.setText("Status: masalah sudah selesai");
                helpBtn.setEnabled(false);
            }

            if (latitude != null || longitude != null || !latitude.equals("0") || !longitude.equals("0")
                    || !latitude.equals("0.0") || !longitude.equals("0.0")) {
                helpBtn.setEnabled(true);
            } else {
                helpBtn.setEnabled(false);
            }
        }

        buildGoogleApiClient();
    }

    private synchronized void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "connected");
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        coordinates = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.getErrorMessage());
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child(Constants.POST).child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, dataSnapshot.getValue().toString());
                        if (dataSnapshot.child(Constants.POST_STATUS).getValue().toString().equals(Constants.POST_SOLVED)
                                && dataSnapshot.child(Constants.HELPER).getValue().toString().equals(prefsManager.getUuid())) {
                            Intent solvedIntent = new Intent(DetailsActivity.this, SolvedActivity.class);
                            solvedIntent.putExtra(Constants.NAME, name);
                            solvedIntent.putExtra(Constants.POST_UUID, uuid);
                            solvedIntent.putExtra(Constants.ID, id);
                            Log.d(TAG, String.valueOf(id));
                            startActivity(solvedIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getMessage());
                    }
                });
    }
}
