package com.guendeli.fidami.mvp.presenters.impl;

import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;
import com.guendeli.fidami.mvp.presenters.ProfileOverviewPresenter;
import com.guendeli.fidami.mvp.views.ProfileOverviewView;

import java.text.DecimalFormat;

public class ProfileOverviewPresenterImpl implements ProfileOverviewPresenter {

    boolean canceled;

    private ProfileOverviewView profileOverviewView;

    private int donationsNumber;

    private static DecimalFormat decimalFormat = new DecimalFormat("0.##");

    public ProfileOverviewPresenterImpl(ProfileOverviewView profileOverviewView) {
        this.profileOverviewView = profileOverviewView;
    }

    @Override
    public void loadProfileData() {
        canceled = false;
        profileOverviewView.showProgress();
        User.getInstance().getUserDonations(new MyCommand() {
            @Override
            public void execute(int value) {
                setupProfileData(value);
            }
        });

    }

    private  void setupProfileData(int value){
        int donations = value;
        float liters = donations * User.BLOOD_PER_DONATION_LITERS;
        float litersNeededForTicket = liters % User.BLOOD_FOR_TICKET_LITERS;

        int points = donations * User.POINTS_PER_DONATION;
        int daysToNext = 73; // TODO

        int achievementNum = 0;

        if (value >= 15) {
            achievementNum = 4;
        } else if (value >= 10) {
            achievementNum = 3;
        } else if (value >= 5) {
            achievementNum = 2;
        } else if (value >= 1) {
            achievementNum = 1;
        }
        profileOverviewView.hideProgress();
        profileOverviewView.showData(decimalFormat.format(liters), String.valueOf(daysToNext),
                String.valueOf(achievementNum), String.valueOf(points));
    }



    @Override
    public void cancel() {
        canceled = true;
    }
}
