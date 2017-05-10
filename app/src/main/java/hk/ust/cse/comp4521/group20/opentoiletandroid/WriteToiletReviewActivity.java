package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Review;

public class WriteToiletReviewActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private SeekBar seekBar;
    private EditText titleText;
    private EditText contentText;
    private Button button;
    private String toiletId;
    private Button uploadImageButton;

    private StorageReference storageReference;
    private FirebaseAuth auth;

    private Uri selectedImageURI;


    private static final String TAG = "WriteToiletReview";


    protected static final int SELECT_IMAGE = 1;

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
        final String finalToiletId = toiletId;
        button.setOnClickListener(new View.OnClickListener() {

            //TODO: Refactor the entire block
            @Override
            public void onClick(View v) {
                //TODO: add the image URL
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String formattedDate = sdf.format(new Date());

                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("review_items/"+ finalToiletId);

                // if there photo attached, upload it
                if(selectedImageURI != null) {
                    ContentResolver cR = getApplicationContext().getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();

                    StorageReference imgRef = storageReference.child("image_items/"+ finalToiletId + "/" + UUID.randomUUID()+ "." + mime.getExtensionFromMimeType(cR.getType(selectedImageURI)));
                    imgRef.putFile(selectedImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        @SuppressWarnings("VisibleForTests")
                        // Suppressed due to bugs with FirebaseStorage
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mRef.push().setValue(new Review(auth.getCurrentUser().getUid(), titleText.getText().toString(), contentText.getText().toString(), formattedDate, ratingBar.getRating(), seekBar.getProgress(), taskSnapshot.getDownloadUrl().toString()));
                            onBackPressed();
                        }
                    });
                } else {
                    mRef.push().setValue(new Review(auth.getCurrentUser().getUid(), titleText.getText().toString(), contentText.getText().toString(), formattedDate, ratingBar.getRating(), seekBar.getProgress(), ""));

                    onBackPressed();
                }

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
                selectedImageURI = data.getData();
            }
        }
    }
}
