package com.onevest.dev.tulung.main.fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.main.adapter.TimelineAdapter;
import com.onevest.dev.tulung.models.Post;
import com.onevest.dev.tulung.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    @BindView(R.id.timeline_recycler_view)
    RecyclerView recyclerView;

    List<Post> postList;
    DatabaseReference databaseReference;
    TimelineAdapter adapter;

    private static final String TAG = TimelineFragment.class.getSimpleName();

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);
        databaseReference.child(Constants.POST_COUNTER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                updateUi();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        updateUi();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POST);
    }

    private void updateUi() {
        try {
            databaseReference.child(Constants.POST).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    postList = new ArrayList<>();
                    Post post;
                    long count = dataSnapshot.getChildrenCount();
                    for (int i = 0; i < count; count--) {
                        post = new Post();
                        post.setAddress(dataSnapshot.child(String.valueOf(count)).child(Constants.ADDRESS).getValue().toString());
                        post.setCategory(dataSnapshot.child(String.valueOf(count)).child(Constants.CATEGORY).getValue().toString());
                        post.setDesc(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_DESCRIPTION).getValue().toString());
                        try {
                            post.setLatitude(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_LAT).getValue().toString());
                            post.setLongitude(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_LONG).getValue().toString());
                        } catch (NullPointerException e) {
                            post.setLatitude(null);
                            post.setLongitude(null);
                        }
                        post.setName(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_NAME).getValue().toString());
                        post.setPeople(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_PEOPLE).getValue().toString());
                        post.setStatus(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_STATUS).getValue().toString());
                        post.setUuid(dataSnapshot.child(String.valueOf(count)).child(Constants.POST_UUID).getValue().toString());
                        postList.add(post);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage());
                }
            });

            adapter = new TimelineAdapter(getContext(), postList);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } catch (NullPointerException e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
    }
}
