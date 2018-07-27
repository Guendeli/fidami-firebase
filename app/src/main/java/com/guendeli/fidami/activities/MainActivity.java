package com.guendeli.fidami.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.guendeli.fidami.LoginActivity;
import com.guendeli.fidami.R;
import com.guendeli.fidami.fragments.AchievementsFragment;
import com.guendeli.fidami.fragments.ProfileFragment;
import com.guendeli.fidami.fragments.ProfileOverviewFragment;
import com.guendeli.fidami.models.MenuItem;
import com.guendeli.fidami.models.User;
import com.guendeli.fidami.mvp.interactors.MyCommand;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ProgressDialog progressDialog;

    @BindView(R.id.my_awesome_toolbar)
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        User.getInstance().isUserNew(new MyCommand() {
            @Override
            public void execute(int value) {
                if(value == 1){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ProfileOverviewFragment())
                            .addToBackStack(null).commit();
                } else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ProfileFragment())
                            .addToBackStack(null).commit();
                }
            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        MenuItem menuItem = null;

        if (mNavigationDrawerFragment != null) {
            menuItem = mNavigationDrawerFragment.getItem(position);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (menuItem != null) {
            switch (menuItem.getTitleResId()) {
                case R.string.menu_home:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ProfileOverviewFragment())
                            .commit();
                    break;
                case R.string.menu_profile:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new ProfileFragment())
                            .commit();
                    break;
                case R.string.menu_achievements:
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new AchievementsFragment())
                            .commit();
                    break;
                case R.string.menu_logout:
                    showLogoutConfirmation();
                    break;
            }
        }
    }

    private void showLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.confirm_logout);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, null);
        builder.show();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

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
}
