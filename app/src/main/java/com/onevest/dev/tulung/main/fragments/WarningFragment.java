package com.onevest.dev.tulung.main.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.main.adapter.WarningAdapter;
import com.onevest.dev.tulung.models.Warning;
import com.onevest.dev.tulung.utils.Constants;
import com.onevest.dev.tulung.utils.PrefsManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningFragment extends Fragment {

    @BindView(R.id.panic_button)
    Button panicBtn;

    @BindView(R.id.warning_recycler_view)
    RecyclerView recyclerView;

    @SuppressWarnings("MissingPermission")
    @OnClick(R.id.panic_button)
    public void panicHandler() {
        panic = prefsManager.getPanic();
        Log.d(TAG, "PANIK");
        if (panic == null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + "110"));
            getContext().startActivity(intent);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + panic));
            getContext().startActivity(intent);
        }
    }

    List<Warning> warningList;
    DatabaseReference databaseReference;
    WarningAdapter adapter;
    String panic;
    PrefsManager prefsManager;

    private static final String TAG = WarningFragment.class.getSimpleName();


    public WarningFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warning, container, false);
        ButterKnife.bind(this, view);
        updateUi();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.WARNING);
        prefsManager = new PrefsManager(getContext());
    }

    private void updateUi() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                warningList = new ArrayList<>();
                Warning warning;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    warning = snapshot.getValue(Warning.class);
                    warningList.add(warning);
                }
                adapter = new WarningAdapter(getContext(), warningList);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
