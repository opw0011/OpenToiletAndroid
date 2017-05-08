package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private NavigationView navigationView;

    private SimpleDraweeView profilePic;
    private TextView profileEmail;
    private TextView profileName;

    private FirebaseAuth auth;

    public static final int LOGIN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        // TODO: for testing profile img only, remove this dummy photo url
        profilePic.setImageURI("http://ste.india.com/sites/default/files/2014/10/04/279580-kim-jong-un-700.jpg");

        // Check whether the user is logged in
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) {
            // if yes, set the menu and the header
            navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);
            Log.d(TAG, "User: " + user.getDisplayName() + user.getEmail() + user.getPhotoUrl());
            setHeader(user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
        }
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

        if (id == R.id.nav_home) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new MainFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_find_toilets) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ToiletListFragment());
            fragmentTransaction.commit();
        } else if (id == R.id.nav_bookmark) {

        } else if (id == R.id.nav_alarm) {

        } else if (id == R.id.nav_account) {
            if (auth.getCurrentUser() != null) {
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_login);

                                // reset the header
                                setHeader(null, null, null);
                            }
                        });
        } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
//                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve results from log in screen
        if (requestCode == LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);

                // Update the user name and email
                FirebaseUser user = auth.getCurrentUser();
                navigationView.getMenu().findItem(R.id.nav_account).setTitle(R.string.nav_account_logout);

                setHeader(user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                     return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG, "No network");
//                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.d(TAG, "Unknown Error");
//                    showSnackbar(R.string.unknown_error);
                    return;
                }
            }

        }

    }

    protected void setHeader(String username, String email, Uri imageURL){
        if (username != null || email != null) {
            profileName.setText(username);
            if (email != null) {
                profileEmail.setText(email);
            }

            //  Display the Profile Pic
            if (imageURL != null) {
                Log.d(TAG, imageURL.toString());
                profilePic.setImageURI(imageURL);
            }
        } else {
            profileName.setText("");
            profileEmail.setText(getText(R.string.not_logged_in));
        }
    }
}
