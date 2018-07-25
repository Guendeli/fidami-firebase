package com.guendeli.fidami.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guendeli.fidami.mvp.interactors.MyCommand;

import butterknife.ButterKnife;



public class User {
    public static final String NAME = "name";

    public static final String SURNAME = "surname";

    public static final String ADDRESS = "address";

    public static final String BLOOD_TYPE = "bloodType";

    public static final String SEX = "sex";

    public static final String ADDITIONAL = "additional";

    public static final String USER_DATA = "UserData";

    public static final String USER_OBJECT_ID = "userObjectId";

    public static final String USER_DONATION = "donations";

    public static final String DONOR = "donor";

    public static final String TYPE = "type";
    public static final String AGE = "age";
    public static final String WEIGHT = "weight";

    public static final float BLOOD_PER_DONATION_LITERS = 0.5f;

    public static final float BLOOD_FOR_TICKET_LITERS = 2f;

    public static final int POINTS_PER_DONATION = 100;

    private FirebaseUser firebaseUser;

    private int donations;

    public static User getInstance(){
        return new User(FirebaseAuth.getInstance().getCurrentUser());
    }

    public User(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
    }

    public void getUserData(){

    }

    public void getUserDonations(final MyCommand cmd){
        donations = 0;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    if(key.equals(User.USER_DONATION)){
                       donations = Integer.parseInt((String)postSnapshot.getValue());
                       cmd.execute(donations);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                donations = 0;
            }
        });


    }

}
