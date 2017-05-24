/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by opw on 6/5/2017.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
