package com.guendeli.fidami.mvp.presenters.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.guendeli.fidami.LoginActivity;
import com.guendeli.fidami.mvp.presenters.LoginPresenter;
import com.guendeli.fidami.mvp.views.LoginView;

public class LoginPresenterImpl implements LoginPresenter {
    private boolean canceled;

    private LoginView loginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void authenticateUser(String username, String password) {
        canceled = false;
        loginView.showProgress();
    }

    @Override
    public void authenticateUserFb(Activity activity) {
        canceled = false;
    }

    @Override
    public void authenticateUserTwitter(Activity activity) {
        canceled = false;

    }

    @Override
    public void cancel() {
        canceled = true;
    }

}
