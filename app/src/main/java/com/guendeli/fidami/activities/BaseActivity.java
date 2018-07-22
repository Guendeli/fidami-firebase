package com.guendeli.fidami.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.guendeli.fidami.R;

public class BaseActivity extends ActionBarActivity {

    private ProgressDialog progressDialog;

    public void showProgressBar() {
        if (progressDialog == null || !progressDialog.isShowing() && !isFinishing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.show();
        }
    }

    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
    }

    public void showDialog(String message) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(message);
            builder.setNeutralButton(android.R.string.ok, null);
            builder.show();
        }
    }
}
