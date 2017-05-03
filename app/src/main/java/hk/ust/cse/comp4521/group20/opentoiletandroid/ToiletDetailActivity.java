package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;


public class ToiletDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;

    WebView webView;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton fab;
    FloatingActionButton fabWriteReview;

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
        final String finalToiletId = toiletId;
        fabWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start write review activity
                Intent intent = new Intent(ToiletDetailActivity.this, WriteToiletReviewActivity.class);
                intent.putExtra("ToiletID", finalToiletId);
                startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
