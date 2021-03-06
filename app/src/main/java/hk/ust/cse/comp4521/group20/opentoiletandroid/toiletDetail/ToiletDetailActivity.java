/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.toiletDetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hk.ust.cse.comp4521.group20.opentoiletandroid.R;
import hk.ust.cse.comp4521.group20.opentoiletandroid.models.Review;
import hk.ust.cse.comp4521.group20.opentoiletandroid.models.Toilet;
import hk.ust.cse.comp4521.group20.opentoiletandroid.pathAdvisor.PathAdvisorActivity;
import hk.ust.cse.comp4521.group20.opentoiletandroid.toiletReview.ReviewViewHolder;
import hk.ust.cse.comp4521.group20.opentoiletandroid.toiletReview.WriteToiletReviewActivity;


/**
 * The type Toilet detail activity.
 */
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
    private SharedPreferences bookmarkPreference;

    private static String toiletId;
    private static Toilet mToilet;

    /**
     * The constant LOGIN.
     */
    public static final int LOGIN = 0;

    /**
     * The constant BOOKMARK_FILE.
     */
    public static final String BOOKMARK_FILE = "BookmarkFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get the models passed by the intent
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
                    ((TextView) findViewById(R.id.tv_floor)).setText(String.format("%s", mToilet.getFloor()));
                    ((TextView) findViewById(R.id.tv_lift)).setText(String.format("%s", TextUtils.join("," , mToilet.getLiftList())));
                    ((TextView) findViewById(R.id.tv_avg_score)).setText(String.format("%.1f", mToilet.obtainAverageScore()));
                    ((TextView) findViewById(R.id.tv_waiting_time)).setText(String.format("%.1f minutes", mToilet.obtainAverageWaitingTime()));
                    ((TextView) findViewById(R.id.tv_count)).setText(String.format("%d", mToilet.getCount()));
                    ((TextView) findViewById(R.id.tv_has_accessible_toilet)).setText(mToilet.isHas_accessible_toilet() ? "✔" : "❌");
                    ((TextView) findViewById(R.id.tv_has_changing_room)).setText(mToilet.isHas_changing_room()? "✔" : "❌");
                    ((TextView) findViewById(R.id.tv_has_shower)).setText(mToilet.isHas_shower() ? "✔" : "❌");
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
                if (review.getImage_url().length() != 0) {
                    reviewViewHolder.setImageView(review.getImage_url());
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        bookmarkPreference = getSharedPreferences(BOOKMARK_FILE,0);

        // Switch the button to minus when the toilet is added to bookmark
        fab = (FloatingActionButton) findViewById(R.id.fab);
        updateFabState();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit preference
                SharedPreferences.Editor editor = bookmarkPreference.edit();
                Set<String> bookmarks = bookmarkPreference.getStringSet("bookmarks", new HashSet<>());
                if(bookmarks.contains(toiletId)){
                    bookmarks.remove(toiletId);
                } else {
                    bookmarks.add(toiletId);
                }
                editor.putStringSet("bookmarks", bookmarks);
                editor.commit();


                if(bookmarkPreference.getStringSet("bookmarks", new HashSet<>()).contains(toiletId)) {
                    fab.setImageResource(R.drawable.ic_bookmark_white_minus);
                    Snackbar.make(view, R.string.bookmark_added, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    fab.setImageResource(R.drawable.ic_bookmark_white_plus);
                    Snackbar.make(view, R.string.bookmark_removed, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                updateFabState();
            }
        });


        fabWriteReview = (FloatingActionButton) findViewById(R.id.fab_write_review);
        fabWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // Start write review activity
                    Intent intent = new Intent(ToiletDetailActivity.this, WriteToiletReviewActivity.class);
                    intent.putExtra("ToiletID", toiletId);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, getText(R.string.login_tips), Snackbar.LENGTH_SHORT)
                            .setAction("Login", v -> {
                                startActivityForResult(
                                        AuthUI.getInstance()
                                                .createSignInIntentBuilder()
                                                .setIsSmartLockEnabled(false)
                                                .setTheme(R.style.AppTheme)
                                                .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                                                .build(),
                                        0);
                            }).show();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve results from log in screen
        if (requestCode == LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // Start write review activity
                    Intent intent = new Intent(ToiletDetailActivity.this, WriteToiletReviewActivity.class);
                    intent.putExtra("ToiletID", toiletId);
                    startActivity(intent);
                }
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(findViewById(R.id.fab), R.string.login_cancelled, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.d(TAG, "No network");
                    Snackbar.make(findViewById(R.id.fab), R.string.login_no_internet_connection, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.d(TAG, "Unknown Error");
                    Snackbar.make(findViewById(R.id.fab), R.string.login_unknown_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * On click.
     *
     * @param v the v
     */
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//        mAdapter.notifyDataSetChanged();
//        mRecyclerView.invalidate();
//    }

    /**
     * Update fab state.
     */
    protected void updateFabState() {
        Set<String> bookmarks = bookmarkPreference.getStringSet("bookmarks", new HashSet<>());
        if(bookmarks.contains(toiletId)){
            fab.setImageResource(R.drawable.ic_bookmark_white_minus);
        } else {
            fab.setImageResource(R.drawable.ic_bookmark_white_plus);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }
}
