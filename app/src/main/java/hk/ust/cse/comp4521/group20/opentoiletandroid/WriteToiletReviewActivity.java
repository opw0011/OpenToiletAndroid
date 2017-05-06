package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;

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

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("review_items/"+ finalToiletId);
                // TODO: push user id, update the total_score and count
                mRef.push().setValue(new Review("123", titleText.getText().toString(), contentText.getText().toString(), formattedDate, ratingBar.getRating(), seekBar.getProgress(), ""));
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
