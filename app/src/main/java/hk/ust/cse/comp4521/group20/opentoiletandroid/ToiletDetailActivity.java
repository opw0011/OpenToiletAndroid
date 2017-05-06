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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


public class ToiletDetailActivity extends AppCompatActivity {
    private static final String TAG = "ToiletDetailActivity";

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private FloatingActionButton fabWriteReview;
    private DatabaseReference mReviewRef;
    private DatabaseReference mToiletRef;
    private static String toiletId;
    private static Toilet mToilet;

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

        // Get the data passed by the intent
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.primary_text_light));
            toiletId = bundle.getString("ToiletId");
            Log.d(TAG, "ToiletId: " + toiletId);
            mToiletRef = FirebaseDatabase.getInstance().getReference("toilet_items/" + toiletId);
            mToiletRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mToilet = dataSnapshot.getValue(Toilet.class);
                    collapsingToolbarLayout.setTitle(mToilet.getName());
                    ((TextView) findViewById(R.id.tv_header)).setText(String.format("Avg Score: %.1f", (double) mToilet.getTotal_score() / mToilet.getCount()));
                    ((TextView) findViewById(R.id.textView6)).setText(String.format("Count: %d", mToilet.getCount()));
                    ((TextView) findViewById(R.id.description)).setText(String.format("Details:\n%s/F, Lift: %s", mToilet.getFloor(), mToilet.getLift().toString()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.reviewList);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mReviewRef = FirebaseDatabase.getInstance().getReference("review_items/"+toiletId);
        mAdapter = new FirebaseRecyclerAdapter<Review, ReviewViewHolder>(Review.class, R.layout.review_list_item, ReviewViewHolder.class, mReviewRef) {
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
        fabWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start write review activity
                Intent intent = new Intent(ToiletDetailActivity.this, WriteToiletReviewActivity.class);
                intent.putExtra("ToiletID", toiletId);
                startActivity(intent);
            }
        });
    }

    public void onClick(View v) {
        if(v.getId() == R.id.btn_view_location) {
            Intent intent = new Intent(this, PathAdvisorActivity.class);
            intent.putExtra("ToiletID", toiletId);
            if(mToilet != null && mToilet.getPa_pos_y() > 0 && mToilet.getPa_pos_x() > 0) {
                intent.putExtra("name", mToilet.getName());
                intent.putExtra("pos_x", mToilet.getPa_pos_x());
                intent.putExtra("pos_y", mToilet.getPa_pos_y());
                intent.putExtra("floor", mToilet.getFloor());
            }
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
