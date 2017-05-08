package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.database.ValueEventListener;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletListFragment extends Fragment {

    public static final String TAG = "ToiletListFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ToiletListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ToiletListFragment constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toilet_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.lvToilet);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // show loading screen
        LoadingScreen loadingScreen = new LoadingScreen();
        getActivity().getFragmentManager().beginTransaction()
                .add(R.id.drawer_layout, loadingScreen).commit();

        // get data from Firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");

        // remove loading screen when data is ready
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getActivity().getFragmentManager().beginTransaction()
                            .remove(loadingScreen).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(container, "Database error", 3000)
                        .setAction("OK", v -> getActivity().getFragmentManager().beginTransaction()
                                .remove(loadingScreen).commit())
                        .show();
            }
        });

        // display data to the user
        mAdapter = new FirebaseRecyclerAdapter<Toilet, ToiletViewHolder>(Toilet.class, R.layout.toilet_list_item, ToiletViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ToiletViewHolder toiletViewHolder, Toilet toilet, int position) {
                toiletViewHolder.setName(toilet.getName());
                String liftString = toilet.getLift().toString();
                toiletViewHolder.setToilet(toilet);
                toiletViewHolder.setToiletId(getRef(position).getKey());
                toiletViewHolder.setText(String.format("lift: %s rating: %.1f", liftString.substring(1, liftString.length() - 1), (double) toilet.getTotal_score() / toilet.getCount()));
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


}
