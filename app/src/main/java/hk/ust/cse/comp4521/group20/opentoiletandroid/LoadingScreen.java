/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * The type Loading screen.
 */
public class LoadingScreen extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.loading, container, false);

        return view;
    }
}
