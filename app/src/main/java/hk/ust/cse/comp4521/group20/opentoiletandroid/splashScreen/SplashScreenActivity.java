/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.splashScreen;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hk.ust.cse.comp4521.group20.opentoiletandroid.MainActivity;
import hk.ust.cse.comp4521.group20.opentoiletandroid.R;

/**
 * The type Splash screen activity.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set fullscreen mode
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        if (actionBar != null)
            actionBar.hide();

        // set Content
        setContentView(R.layout.activity_splash_screen);

        // Persist toilet information
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");
        mRef.keepSynced(true);

        // Runnable to start the main activity
        Runnable startMainActivityRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        };

        // Create a new handler to start the main activity
        // and close the splash screen after a few second
        new Handler().postDelayed(startMainActivityRunnable, SPLASH_SCREEN_DISPLAY_LENGTH);
    }
}
