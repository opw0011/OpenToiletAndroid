package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private NavigationView navigationView;

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertBuilder;

    private SimpleDraweeView profilePic;
    private TextView profileEmail;
    private TextView profileName;

    private FirebaseAuth auth;

    public static final int LOGIN = 0;

    private static final float SHAKE_FORCE = (float) 1.25;
    private static final int SHAKE_TIMES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Set the initial fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MainFragment());
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get references to header
        View header = navigationView.getHeaderView(0);
        profilePic = (SimpleDraweeView) header.findViewById(R.id.profile_pic);
        profileEmail = (TextView) header.findViewById(R.id.profile_email);
        profileName = (TextView) header.findViewById(R.id.profile_name);

        // Check whether the user is logged in
        FirebaseUser user = auth.getCurrentUser();
        setHeader(user);
        if(user != null) {
            // if yes, set the menu and the header
            navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);
        }

        // Set up a shake detector
        alertBuilder = new AlertDialog.Builder(this);
        ShakeDetector.create(this, () -> {
            Log.d(TAG, "Shake Detected");
            if(alertDialog == null || !alertDialog.isShowing()) {
                alertDialog = alertBuilder
                        .setTitle(R.string.shake_alert_title)
                        .setMessage(R.string.shake_alert_message)
                        .setPositiveButton(R.string.shake_alert_send, (DialogInterface dialog, int which) -> {
                            dialog.dismiss();

                            // Go to the send SOS activity
                            Intent intent = new Intent(this, SendSOSActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton(R.string.shake_alert_cancel, (DialogInterface dialog, int which) -> dialog.dismiss())
                        .show();
            }
        });

        ShakeDetector.updateConfiguration(SHAKE_FORCE, SHAKE_TIMES);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        findViewById(R.id.search_view_container).setVisibility(View.GONE);
        if (id == R.id.nav_home) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MainFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_find_toilets) {
            findViewById(R.id.search_view_container).setVisibility(View.VISIBLE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ToiletListFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_bookmark) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new BookmarkListFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_alarm) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new SOSListFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_account) {
            if (auth.getCurrentUser() != null) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // show a logout snackbar
                                showSnackbar(getText(R.string.logout_successful), Snackbar.LENGTH_SHORT);

                                // user is now signed out
                                navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_login);

                                // reset the header
                                setHeader(null);
                            }
                        });
        } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setTheme(R.style.AppTheme)
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                .build(),
                        0);
            }
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve results from log in screen
        if (requestCode == LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                // Show a login success snackbar
                showSnackbar(getText(R.string.login_success), Snackbar.LENGTH_SHORT);

                // Update the user name and email
                FirebaseUser user = auth.getCurrentUser();
                navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);

                setHeader(user);
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(getText(R.string.login_cancelled), Snackbar.LENGTH_SHORT);
                     return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG, "No network");
                    showSnackbar(getText(R.string.login_no_internet_connection), Snackbar.LENGTH_SHORT);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.d(TAG, "Unknown Error");
                    showSnackbar(getText(R.string.login_unknown_error), Snackbar.LENGTH_SHORT);
                    return;
                }
            }

        }

    }

    protected void setHeader(FirebaseUser user) {
        if (user != null) {
            String username = user.getDisplayName();
            String email = user.getEmail();
            Uri imageURL = user.getPhotoUrl();

            // Set the user email
            profileEmail.setText(email);

            // Set the User name
            if(username != null) {
                profileName.setText(username);
            }

            //  Display the Profile Pic
            if (imageURL != null) {
                profilePic.setImageURI(imageURL);
            } else {
                profilePic.setImageURI(getText(R.string.default_profile_pic).toString());
            }

        } else {
            profileName.setText("");
            profileEmail.setText(getText(R.string.not_logged_in));
            profilePic.setImageURI(getText(R.string.default_profile_pic).toString());
        }
    }

    protected void showSnackbar(CharSequence msg, int duration) {
        Snackbar.make(findViewById(R.id.nav_view), msg, duration)
                .setAction("Action", null).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
        
        // Check whether the user is logged in
        FirebaseUser user = auth.getCurrentUser();
        setHeader(user);
        if(user != null) {
            // if yes, set the menu and the header
            navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);
        }

    }

    @Override
    protected void onStop() {
        if(alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        ShakeDetector.destroy();
    }

    public Toolbar getToolbar() {
        toolbar =  (Toolbar) findViewById(R.id.toolbar);
        return toolbar;
    }
}
