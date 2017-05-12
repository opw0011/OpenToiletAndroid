package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.SOS;
import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class SOSListFragment extends Fragment {

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

        mAdapter = new FirebaseRecyclerAdapter<SOS, SOSViewHolder>(SOS.class, R.layout.sos_list_item, SOSViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(SOSViewHolder viewHolder, SOS model, int position) {

            }
        };
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
