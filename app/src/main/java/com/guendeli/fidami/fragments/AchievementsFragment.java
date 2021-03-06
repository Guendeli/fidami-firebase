package com.guendeli.fidami.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guendeli.fidami.R;
import com.guendeli.fidami.mvp.presenters.AchievementsPresenter;
import com.guendeli.fidami.mvp.presenters.impl.AchievementsPresenterImpl;
import com.guendeli.fidami.mvp.views.AchievementsView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class AchievementsFragment extends BaseFragment implements AchievementsView {


    @BindView(R.id.achievement_1st_time)
    ImageView achievement1stTime;

    @BindView(R.id.achievement_2_year)
    ImageView achievement2Year;

    @BindView(R.id.achievement_3_year)
    ImageView achievement3Year;

    @BindView(R.id.achievement_5_times)
    ImageView achievement5Times;

    @BindView(R.id.achievement_10_times)
    ImageView achievement10Times;

    @BindView(R.id.achievement_heart)
    ImageView achievementHeart;

    @BindViews({R.id.achievement_1st_time, R.id.achievement_2_year, R.id.achievement_3_year,
            R.id.achievement_5_times, R.id.achievement_10_times, R.id.achievement_heart})
    List<ImageView> achievementImages;

    private AchievementsPresenter achievementsPresenter;

    public AchievementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        ButterKnife.bind(this, view);


        achievementsPresenter = new AchievementsPresenterImpl(this);
        achievementsPresenter.loadAchievements();

        return view;
    }

    @Override
    public void showAchievements(boolean[] achievements) {
        for (int i = 0; i < achievements.length && i < achievementImages.size(); i++) {
            if (!achievements[i]) {
                achievementImages.get(i).setImageAlpha(128);
            }
        }
    }

    @OnClick(R.id.achievement_1st_time)
    void testClick(){
        Toast.makeText(getActivity(), "Click on Achievement", Toast.LENGTH_SHORT);
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
        super.onDestroyView();
    }

}
