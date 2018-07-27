package com.guendeli.fidami.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.guendeli.fidami.mvp.interactors.MyCommand;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;



public class User {
    public static final String NAME = "name";

    public static final String SURNAME = "surname";

    public static final String ADDRESS = "address";

    public static final String BLOOD_TYPE = "bloodType";

    public static final String SEX = "sex";

    public static final String ADDITIONAL = "additional";

    public static final String USER_SAVED = "userSaved";

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

    public int donations;
    public String myString;
    public static User getInstance(){
        return new User(FirebaseAuth.getInstance().getCurrentUser());
    }

    public User(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser;
    }

    public void getUserData(){

    }

    public String getUserDonationAsString(){
        return String.valueOf(donations);
    }

    public void isUserNew(final MyCommand cmd){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String key = postSnapshot.getKey();
                    if(key.equals(User.USER_SAVED)){
                        String keyValue = (String)postSnapshot.getValue();
                        if(keyValue.equals("true")){
                            cmd.execute(1);
                        } else {
                            cmd.execute(0);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getUserDonations(final MyCommand cmd){
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
                cmd.execute(donations);
            }
        });
    }

    public void createNewUser(final MyCommand postExecute){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fidami-97e80.firebaseio.com");
        if(user != null) {
            Map<String, String> map = new HashMap<String, String>();
            map.put(User.NAME,"");
            map.put(User.SURNAME, "");
            map.put(User.ADDRESS, "");
            map.put(User.BLOOD_TYPE, "O+");
            map.put(User.SEX, "M");
            map.put(User.ADDITIONAL, "");
            map.put(User.AGE, "");
            map.put(User.WEIGHT, "");

            map.put(User.USER_SAVED,"false");
            map.put(User.USER_DONATION,"0");
            ref.child("users").child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Log.e("Database Users", "New User Added");
                        postExecute.execute(0);
                    }
                }
            });
        }
    }

}
