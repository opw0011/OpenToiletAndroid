package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;

public class WriteToiletReviewActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private SeekBar seekBar;
    private EditText titleText;
    private EditText contentText;
    private Button button;
    private String toiletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_toilet_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Write Toilet Review");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            toiletId = bundle.getString("ToiletID");
        }
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        seekBar = (SeekBar) findViewById(R.id.sb_waiting_time);
        titleText = (EditText) findViewById(R.id.input_name);
        contentText = (EditText) findViewById(R.id.et_review_content);
        button = (Button) findViewById(R.id.button2);
        final String finalToiletId = toiletId;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add the image URL
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String formattedDate = sdf.format(new Date());

                DatabaseReference mReviewRef = FirebaseDatabase.getInstance().getReference("review_items/"+ finalToiletId);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mReviewRef.push().setValue(new Review(user.getUid(), titleText.getText().toString(), contentText.getText().toString(), formattedDate, ratingBar.getRating(), seekBar.getProgress(), ""));

                    DatabaseReference mToiletRef = FirebaseDatabase.getInstance().getReference("toilet_items/"+ finalToiletId);
                    mToiletRef.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            Toilet toilet = mutableData.getValue(Toilet.class);
                            if (toilet == null) {
                                return Transaction.success(mutableData);
                            }

                            toilet.setCount(toilet.getCount() + 1);
                            toilet.setTotal_score(toilet.getTotal_score() + ratingBar.getRating());
                            toilet.setTotal_waiting_minute(toilet.getTotal_waiting_minute() + seekBar.getProgress());

                            // Set value and report transaction success
                            mutableData.setValue(toilet);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.d("Review", "postTransaction:onComplete:" + databaseError);
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.login_tips), Toast.LENGTH_SHORT);
                    toast.show();
                }

                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WriteToiletReviewActivity.this, ToiletDetailActivity.class);
        intent.putExtra("ToiletId", toiletId);
        setResult(RESULT_OK, intent);
        finish();
    }
}
