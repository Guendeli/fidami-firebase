package com.guendeli.fidami.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guendeli.fidami.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;

    public void showProgressBar() {
        if (progressDialog == null || !progressDialog.isShowing() && !getActivity().isFinishing()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading_message));
            progressDialog.show();
        }
    }

    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing() && !getActivity().isFinishing()) {
            progressDialog.hide();
        }
    }

    public void showDialog(String message) {
        if (!getActivity().isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.app_name);
            builder.setMessage(message);
            builder.setNeutralButton(android.R.string.ok, null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
