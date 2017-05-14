package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.SOS;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;

public class SendSOSActivity extends AppCompatActivity {

    private AutoCompleteTextView location;
    private AutoCompleteTextView title;
    private EditText message;

    protected ArrayAdapter<Toilet> locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back to home navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Bind the UI Elements
        location = (AutoCompleteTextView) findViewById(R.id.input_sos_location);
        title = (AutoCompleteTextView) findViewById(R.id.input_sos_title);
        message = (EditText) findViewById(R.id.input_sos_message);


        // Set common titles for users to choose
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.sos_common_title_values));
        title.setAdapter(adapter);

        // TODO: Allow the user to choose a toilet instead of typing it directly

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setEnabled(false);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String formattedDate = sdf.format(new Date());

                String sosLocation = location.getText().toString();
                String sosMessage = message.getText().toString();
                String sosTitle = title.getText().toString();

                SOS sos = new SOS(sosLocation, sosMessage, formattedDate, sosTitle);
                DatabaseReference mSOSRef = FirebaseDatabase.getInstance().getReference("sos_items");
                mSOSRef.push().setValue(sos);

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

}
