package com.guendeli.fidami.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guendeli.fidami.R;
import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;
import com.guendeli.fidami.mvp.presenters.ProfilePresenter;
import com.guendeli.fidami.mvp.presenters.impl.ProfilePresenterImpl;
import com.guendeli.fidami.mvp.views.ProfileView;

import java.util.concurrent.locks.Lock;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements ProfileView {

    @BindView(R.id.input_name)
    EditText inputName;

    @BindView(R.id.input_surname)
    EditText inputSurname;

    @BindView(R.id.input_age)
    EditText inputAge;

    @BindView(R.id.input_weight)
    EditText inputWeight;

    @BindView(R.id.spinner_sex)
    Spinner spinnerSex;

    @BindView(R.id.input_address)
    EditText inputAddress;

    @BindView(R.id.spinner_blood_type)
    Spinner spinnerBloodType;

    @BindView(R.id.spinner_rh)
    Spinner spinnerRh;

    @BindView(R.id.input_additional)
    EditText inputAdditional;

    @BindView(R.id.layout_blood_type)
    View layoutBloodType;

    @BindView(R.id.layout_sex)
    View layoutSex;

    @BindView(R.id.layout_rh)
    View layoutRh;

    private ProfilePresenter profilePresenter;

    private boolean isEdit;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        User.getInstance().isUserNew(new MyCommand() {
            @Override
            public void execute(int value) {
                if(value == 1){
                    // old user saved, should lock profile
                    LockUser();
                }
            }
        });


        profilePresenter = new ProfilePresenterImpl(this);
        return view;
    }

    private void LockUser(){
        Log.e("Database"," Fetching database");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    if(postSnapshot.getKey().equals(User.NAME)){
                        inputName.setText((String)postSnapshot.getValue());
                        inputName.setClickable(false);
                        inputName.setFocusable(false);
                        inputName.setTextColor(Color.parseColor("#b7b7b7"));
                    } else if (postSnapshot.getKey().equals(User.SURNAME)){
                        inputSurname.setText((String)postSnapshot.getValue());
                        inputSurname.setClickable(false);
                        inputSurname.setFocusable(false);
                        inputSurname.setTextColor(Color.parseColor("#b7b7b7"));
                    } else if (postSnapshot.getKey().equals(User.AGE)){
                        inputAge.setText((String)postSnapshot.getValue());
                        inputAge.setClickable(false);
                        inputAge.setFocusable(false);
                        inputAge.setTextColor(Color.parseColor("#b7b7b7"));
                    } else if (postSnapshot.getKey().equals(User.WEIGHT)){
                        inputWeight.setText((String)postSnapshot.getValue());
                        inputWeight.setClickable(false);
                        inputWeight.setFocusable(false);
                        inputWeight.setTextColor(Color.parseColor("#b7b7b7"));
                    } else if(postSnapshot.getKey().equals(User.SEX)){
                        spinnerSex.setSelection(getSpinnerIndex(spinnerSex,(String)postSnapshot.getValue()));

                    } else if(postSnapshot.getKey().equals(User.BLOOD_TYPE)){
                        String bloodType = (String)postSnapshot.getValue();
                        spinnerBloodType.setSelection(getSpinnerIndex(spinnerBloodType, removeLastChar(bloodType)));

                        spinnerRh.setSelection(getSpinnerIndex(spinnerRh, bloodType.substring(bloodType.length()-1)));

                    } else if(postSnapshot.getKey().equals(User.ADDRESS)){
                        inputAddress.setText((String)postSnapshot.getValue());
                    } else if (postSnapshot.getKey().equals(User.ADDITIONAL)){
                        inputAdditional.setText((String)postSnapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Databse Error: ", databaseError.toString());
                Toast.makeText(getActivity(),databaseError.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.button_save)
    protected void save() {
        final String name = inputName.getText().toString().trim();
        final String surname = inputSurname.getText().toString().trim();
        final String address = inputAddress.getText().toString().trim();
        final String bloodType = (String) spinnerBloodType.getSelectedItem();
        final String sex = (String) spinnerSex.getSelectedItem();
        final String additional = inputAdditional.getText().toString().trim();
        final String rhType = (String) spinnerRh.getSelectedItem();
        final String age = inputAge.getText().toString().trim();
        final String weight = inputWeight.getText().toString().trim();

        if (isEdit) {
            //profilePresenter.saveData(address, additional);
            profilePresenter.saveData(name, surname, address, bloodType, sex, additional, rhType,age,weight);
        } else {
            if (validate(name, surname)) {
                profilePresenter
                        .saveData(name, surname, address, bloodType, sex, additional, rhType,age,weight);
                SharedPreferences.Editor prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).edit();
                prefs.putBoolean("saved", true);
                prefs.commit();
            }
        }
    }

    private boolean validate(String name, String surname) {
        boolean valid = true;

        if (surname.isEmpty()) {
            valid = false;
            inputSurname.setError(getString(R.string.field_required));
            inputSurname.requestFocus();
        }

        if (name.isEmpty()) {
            valid = false;
            inputName.setError(getString(R.string.field_required));
            inputName.requestFocus();
        }

        return valid;
    }

    private int getSpinnerIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_LONG).show();

    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();
    }

    @Override
    public void showError(String message) {
        showDialog(message);
    }

    @Override
    public void onDestroyView() {
        profilePresenter.cancel();
        super.onDestroyView();
    }

    public static String removeLastChar(String str){
        return str.substring(0,str.length()-1);
    }

}
