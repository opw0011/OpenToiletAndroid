package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hk.ust.cse.comp4521.group20.opentoiletandroid.R;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;

public class ToiletDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    WebView webView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String toiletId ="";
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
            ((TextView) findViewById(R.id.tv_header)).setText(String.format("Avg Score: %.1f", (double) bundle.getInt("ToiletTotalScore") / bundle.getInt("ToiletReviewCount")));
            ((TextView) findViewById(R.id.textView6)).setText(String.format("Count: %d", bundle.getInt("ToiletReviewCount")));
            String liftString = bundle.getString("ToiletLift");
            ((TextView) findViewById(R.id.description)).setText(String.format("Details:\n%d/F, Lift: %s", bundle.getInt("ToiletFloor"), liftString.substring(1, liftString.length()-1)));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.primary_text_light));
            toiletId = bundle.getString("ToiletId");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Bookmark toilet into local storage
                Snackbar.make(view, "Successfully bookmarked the toilet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Load web view
        webView = (WebView) findViewById(R.id.wv_main);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://pathadvisor.ust.hk/m.interface.html");

        // http://stackoverflow.com/questions/32304237/android-webview-loading-data-performance-very-slow
        if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.reviewList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("review_items/"+toiletId);
        mAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class, R.layout.review_list_item, ReviewViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ReviewViewHolder reviewViewHolder, Review review, int position) {
                reviewViewHolder.setTitle(review.getTitle());
                reviewViewHolder.setDesc(review.getContent());
                reviewViewHolder.setRating(review.getScore());
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyWebViewClient  extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.equals("http://pathadvisor.ust.hk/m.interface.html")) {
                // clickFillInItem({roomName:'LIFT 19', floor:'4' , coor: { x:'2080', y:'1810'} , roomId:'p228'})
                webView.loadUrl("javascript:clickFillInItem({roomName:'LIFT 19', floor:'4' , coor: { x:'2080', y:'1810'} , roomId:'p228'});");
            }
        }
    }
}
