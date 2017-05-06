package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by opw on 6/5/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
