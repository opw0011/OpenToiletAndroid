package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
