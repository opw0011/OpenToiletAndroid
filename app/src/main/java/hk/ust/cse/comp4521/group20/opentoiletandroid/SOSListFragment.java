package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.SOS;


/**
 * A simple {@link Fragment} subclass.
 */
public class SOSListFragment extends Fragment {

    private static final String TAG = "SOSListFragment";
    private RecyclerView mRecyclerView;
    private CardView mEmptyCardView;
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

        // empty view
        mEmptyCardView = (CardView) view.findViewById(R.id.empty_view_SOSList);

        // get data from Firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("sos_items");
        // filter only the active sos request
        Query queryRef = mRef.orderByChild("is_active").equalTo(true);

        mAdapter = new FirebaseRecyclerAdapter<SOS, SOSViewHolder>(SOS.class, R.layout.sos_list_item, SOSViewHolder.class, queryRef) {

            @Override
            protected void populateViewHolder(SOSViewHolder viewHolder, SOS model, int position) {
                // Calculate the time that should be shown
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, -2);
                Date startingTime = calendar.getTime();

                viewHolder.setSosId(getRef(position).getKey());
                viewHolder.setTextViewTitle(model.getTitle());
                viewHolder.setTextViewMessage(model.getMessage());
                viewHolder.setTextViewTimestamp(model.getCreated_at());

                viewHolder.setButtonSOSResolve(model.getAuthor());

                try {
                    if (model.obtainCreatedAtDate().after(startingTime)) {
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
                    } else {
                        this.getRef(position).removeValue();
                    }
                } catch (ParseException e){
                    this.getRef(position).removeValue();
                }
            }
        };


        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                this.setView();

            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                this.setView();
            }

            private void setView() {
                if(mAdapter.getItemCount() != 0) {
                    mEmptyCardView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyCardView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        };
        mAdapter.registerAdapterDataObserver(observer);

        // Floating button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_sos);
        Drawable icon = new IconicsDrawable(view.getContext())
                .icon(GoogleMaterial.Icon.gmd_add_alert)
                .color(Color.WHITE)
                .sizeDp(24);
        fab.setImageDrawable(icon);
        fab.setOnClickListener(fabView -> {
            if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                Snackbar.make(fabView, getText(R.string.sos_login_tips), Snackbar.LENGTH_SHORT)
                        .setAction("Login", tipView -> {
                            MainActivity mainActivity = (MainActivity) getActivity();
                            mainActivity.startLoginActivity();
                        }).show();
            } else {
                Intent intent = new Intent(getContext(), SendSOSActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
