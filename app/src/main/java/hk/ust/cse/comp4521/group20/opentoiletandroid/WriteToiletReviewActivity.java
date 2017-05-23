package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;

public class WriteToiletReviewActivity extends AppCompatActivity {
    protected static final int SELECT_IMAGE = 1;
    private static final String TAG = "WriteToiletReview";
    private RatingBar ratingBar;
    private SeekBar seekBar;
    private EditText titleText;
    private EditText contentText;
    private Button button;
    private String toiletId;
    private TextView tvWaitingTime;
    private Button uploadImageButton;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private Uri selectedImageUri;
    private static final int SEEK_BAR_WAITING_TIME_INTERVAL = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_toilet_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up Firebase services
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

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
        tvWaitingTime = (TextView) findViewById(R.id.tv_waiting_time);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvWaitingTime.setText(String.format("%s minutes", seekBar.getProgress() * SEEK_BAR_WAITING_TIME_INTERVAL));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button.setOnClickListener((View v) -> {
            button.setEnabled(false);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String formattedDate = sdf.format(new Date());

            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                // Create a review object
                Review review = new Review(user.getUid(), titleText.getText().toString(), contentText.getText().toString(), formattedDate,
                        ratingBar.getRating(), seekBar.getProgress() * SEEK_BAR_WAITING_TIME_INTERVAL, "");
                // if there photo attached, upload it
                if (selectedImageUri != null) {
                    StorageReference imgRef = storageReference.child(getFilename(toiletId, selectedImageUri));

                    imgRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        // Suppressed due to bugs with FirebaseStorage
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            review.setImage_url(taskSnapshot.getDownloadUrl().toString());
                            reviewUpdate(review);
                            onBackPressed();
                        }
                    });
                } else {
                    reviewUpdate(review);
                    onBackPressed();
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), getText(R.string.login_tips), Toast.LENGTH_SHORT);
                toast.show();
                button.setEnabled(true);
            }

        });

        uploadImageButton = (Button) findViewById(R.id.btn_upload_image);
        uploadImageButton.setOnClickListener((View v) -> {
            Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
            selectImageIntent.setType("image/*");
            selectImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

            startActivityForResult(selectImageIntent, SELECT_IMAGE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_IMAGE) {
            if(resultCode == RESULT_OK) {
                selectedImageUri = data.getData();
            }
        }
    }

    protected String getFilename(String toiletId, Uri imageUri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return "image_items/"+ toiletId + "/" + UUID.randomUUID()+ "." + mime.getExtensionFromMimeType(cR.getType(imageUri));
    }

    protected void reviewUpdate (Review review) {
        final String finalToiletId = toiletId;
        DatabaseReference mReviewRef = FirebaseDatabase.getInstance().getReference("review_items/"+ finalToiletId);
        mReviewRef.push().setValue(review);

        DatabaseReference mToiletRef = FirebaseDatabase.getInstance().getReference("toilet_items/"+ finalToiletId);
        mToiletRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Toilet toilet = mutableData.getValue(Toilet.class);
                if (toilet == null) {
                    return Transaction.success(mutableData);
                }

                toilet.setCount(toilet.getCount() + 1);
                toilet.setTotal_score(toilet.getTotal_score() + review.getScore());
                toilet.setTotal_waiting_minute(toilet.getTotal_waiting_minute() + review.getWaiting_minute());

                // Set value and report transaction success
                mutableData.setValue(toilet);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("Review", "postTransaction:onComplete:" + databaseError);
            }
        });
    }
}