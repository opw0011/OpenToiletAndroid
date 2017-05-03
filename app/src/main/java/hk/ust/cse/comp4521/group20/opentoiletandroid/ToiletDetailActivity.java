package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ToiletDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    FloatingActionButton fabWriteReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get the data passed from by the intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            collapsingToolbarLayout.setTitle(bundle.getString("ToiletName"));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.primary_text_light));
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Bookmark toilet into local storage
                Snackbar.make(view, "Successfully bookmarked the toilet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        fabWriteReview = (FloatingActionButton) findViewById(R.id.fab_write_review);
        fabWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Initial write review activity
                Snackbar.make(view, "Write Review", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_view_location) {
            Intent intent = new Intent(this, PathAdvisorActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
