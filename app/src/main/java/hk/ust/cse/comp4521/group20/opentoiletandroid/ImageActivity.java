/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * The type Image activity.
 */
public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("");
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String ImageURL  = bundle.getString("ImageURL");
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(ImageURL)
                    .error(R.drawable.ic_error_black_24dp)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            findViewById(R.id.loading).setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
