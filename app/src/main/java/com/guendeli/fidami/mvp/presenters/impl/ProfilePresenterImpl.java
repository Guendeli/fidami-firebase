package com.guendeli.fidami.mvp.presenters.impl;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;
import com.guendeli.fidami.mvp.presenters.ProfilePresenter;
import com.guendeli.fidami.mvp.views.ProfileView;

import java.util.HashMap;
import java.util.Map;

public class ProfilePresenterImpl implements ProfilePresenter {
    private boolean canceled;

    private ProfileView profileView;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public ProfilePresenterImpl(ProfileView profileView) {
        this.profileView = profileView;
    }

    @Override
    public void saveData(final String name, final String surname, final String address,
                         final String bloodType, final String sex,
                         final String additional, final String rhType, final String age, final String weight) {
        profileView.showProgress();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fidami-97e80.firebaseio.com");
        if(user != null){

            User.getInstance().getUserDonations(new MyCommand() {
                @Override
                public void execute(int value) {
                    User.getInstance().donations = value;
                }
            });

            Map<String,String> map = new HashMap<String,String>();
            map.put(User.NAME,name);
            map.put(User.SURNAME, surname);
            map.put(User.ADDRESS, address);
            map.put(User.BLOOD_TYPE, bloodType + rhType);
            map.put(User.SEX, sex);
            map.put(User.ADDITIONAL, additional);
            map.put(User.AGE, age);
            map.put(User.WEIGHT, weight);
            map.put(User.USER_DONATION, String.valueOf(User.getInstance().donations));
            map.put(User.USER_SAVED,"true");
            ref.child("users").child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        profileView.hideProgress();
                    }
                }
            });

        }
    }

    @Override
    public void saveData(final String address, final String additional) {
        profileView.showProgress();
    }

    @Override
    public void cancel() {
        canceled = true;
    }
}
