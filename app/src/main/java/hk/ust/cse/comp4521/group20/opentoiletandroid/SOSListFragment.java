package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.SOS;


/**
 * A simple {@link Fragment} subclass.
 */
public class SOSListFragment extends Fragment {

    private static final String TAG = "SOSListFragment";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;

    public SOSListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soslist, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_SOSList);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // get data from Firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("sos_items");
        // filter only the active sos request
        Query queryRef = mRef.orderByChild("_active").equalTo(true);

        mAdapter = new FirebaseRecyclerAdapter<SOS, SOSViewHolder>(SOS.class, R.layout.sos_list_item, SOSViewHolder.class, queryRef) {
            @Override
            protected void populateViewHolder(SOSViewHolder viewHolder, SOS model, int position) {
                viewHolder.setTextViewTitle(model.getTitle());
                viewHolder.setTextViewMessage(model.getMessage());
                viewHolder.setTextViewTimestamp(model.getCreated_at());

                // Retrieve toilet detail using toilet id
                // http://stackoverflow.com/questions/36235919/how-to-use-a-firebaserecycleradapter-with-a-dynamic-reference-in-android
                String toiletID = model.getToilet_id();
                FirebaseDatabase.getInstance().getReference("toilet_items").child(toiletID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String toiletName = dataSnapshot.child("name").getValue(String.class);
                        viewHolder.setTextViewToiletName(toiletName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, databaseError.getDetails());
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        // Floating button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_sos);
        Drawable icon = new IconicsDrawable(view.getContext())
                .icon(GoogleMaterial.Icon.gmd_add_alert)
                .color(Color.WHITE)
                .sizeDp(24);
        fab.setImageDrawable(icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SendSOSActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
