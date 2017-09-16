package com.onevest.dev.tulung.main.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.auth.LoginActivity;
import com.onevest.dev.tulung.utils.Constants;
import com.onevest.dev.tulung.utils.PrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    //region BindView
    @BindView(R.id.logout)
    Button logoutButton;

    @OnClick(R.id.logout)
    public void logoutHandler() {
        prefsManager.logout();
        LoginManager.getInstance().logOut();
        mAuth.signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }

    @BindView(R.id.account_name)
    TextView tv_name;
    @BindView(R.id.account_email)
    TextView tv_email;
    @BindView(R.id.account_phone)
    TextView tv_phone;
    @BindView(R.id.account_panic)
    TextView tv_panic;

    @BindView(R.id.phone_edit)
    TextView tv_phone_edit;
    @BindView(R.id.emergency_edit)
    TextView tv_emergency_edit;

    @OnClick(R.id.phone_edit)
    public void phoneEditHandler() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_phone_edit);
        Button save = (Button) dialog.findViewById(R.id.phone_edit_save);
        final EditText phoneET = (EditText) dialog.findViewById(R.id.panic_edit_edittext);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneET.getText().toString();
                String uuid = prefsManager.getUuid();
                databaseReference.child(uuid).child(Constants.PHONE).setValue(phone)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                prefsManager.setPhone(phone);
                                tv_phone.setText(phone);
                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }
    //endregion

    private PrefsManager prefsManager;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        tv_name.setText(prefsManager.getName());
        tv_email.setText(prefsManager.getEmail());
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsManager = new PrefsManager(getContext());
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USERS);
    }
}
