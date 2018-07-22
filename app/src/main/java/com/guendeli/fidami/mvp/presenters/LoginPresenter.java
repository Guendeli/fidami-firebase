package com.guendeli.fidami.mvp.presenters;

import android.app.Activity;

public interface LoginPresenter extends BasePresenter {

    public void authenticateUser(String username, String password);

    public void authenticateUserFb(Activity activity);

    public void authenticateUserTwitter(Activity activity);

}
