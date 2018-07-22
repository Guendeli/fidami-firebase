package com.guendeli.fidami.mvp.views;

public interface BaseView {

    public void showProgress();

    public void hideProgress();

    public void showError(String message);
}
