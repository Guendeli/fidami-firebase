package com.guendeli.fidami.mvp.presenters.impl;

import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;
import com.guendeli.fidami.mvp.presenters.AchievementsPresenter;
import com.guendeli.fidami.mvp.views.AchievementsView;

public class AchievementsPresenterImpl implements AchievementsPresenter {


    private boolean canceled;

    private AchievementsView achievementsView;

    public AchievementsPresenterImpl(AchievementsView achievementsView) {
        this.achievementsView = achievementsView;
    }

    @Override
    public void loadAchievements() {
        achievementsView.showProgress();
        User.getInstance().getUserDonations(new MyCommand() {
            @Override
            public void execute(int value) {
                setupAchievements(value);
            }
        });
    }

    private void setupAchievements(int value){
        if (canceled) {
            return;
        }
        achievementsView.hideProgress();
        boolean[] achievements = new boolean[6];
        achievements[0] = value >= 1;
        achievements[3] = value >= 5;
        achievements[4] = value >= 10;
        achievements[5] = value >= 15;

        achievementsView.showAchievements(achievements);
    }

    @Override
    public void cancel() {
        canceled = true;
    }
}
