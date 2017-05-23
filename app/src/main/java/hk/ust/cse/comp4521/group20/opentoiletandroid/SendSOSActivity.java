package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.SOS;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;

public class SendSOSActivity extends AppCompatActivity {

    private AutoCompleteTextView title;
    private EditText message;
    protected FirebaseAuth firebaseAuth;
    private Spinner spinnerLocation;
    protected ArrayAdapter<Toilet> locationAdapter;
    private String selectedToiletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get reference to FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Bind the UI Elements
        spinnerLocation = (Spinner) findViewById(R.id.spinner_sos_location);
        title = (AutoCompleteTextView) findViewById(R.id.input_sos_title);
        message = (EditText) findViewById(R.id.input_sos_message);


        // Set common titles for users to choose
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.sos_common_title_values));
        title.setAdapter(adapter);

        spinnerLocation.setAdapter(adapter);
        // TODO: Allow the user to choose a toilet instead of typing it directly
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");
        FirebaseListAdapter<Toilet> firebaseListAdapter = new FirebaseListAdapter<Toilet>(this, Toilet.class, android.R.layout.simple_spinner_dropdown_item, mRef) {
            @Override
            protected void populateView(View v, Toilet toilet, int position) {
                ((TextView) v.findViewById(android.R.id.text1)).setText(toilet.getName());
            }
        };

        spinnerLocation.setAdapter(firebaseListAdapter);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String toiletId = firebaseListAdapter.getRef(position).getKey();
                selectedToiletId = toiletId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().trim().isEmpty()){
                    title.setError(getString(R.string.sos_empty_title));
                    return;
                }
                if (firebaseAuth.getCurrentUser() != null ) {
                    fab.setEnabled(false);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    String formattedDate = sdf.format(new Date());

                    String authorId = firebaseAuth.getCurrentUser().getUid();
                    String sosLocation = selectedToiletId;
                    String sosMessage = message.getText().toString();
                    String sosTitle = title.getText().toString();

                    SOS sos = new SOS(sosLocation, authorId, sosMessage, formattedDate, sosTitle);
                    DatabaseReference mSOSRef = FirebaseDatabase.getInstance().getReference("sos_items");
                    mSOSRef.push().setValue(sos);

                    onBackPressed();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.sos_login_tips, Toast.LENGTH_SHORT);
                    toast.show();
                    fab.setEnabled(true);
                }

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

}
