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
import com.google.firebase.database.Query;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private RecyclerView mPopularToiletsRecyclerView;
    private LinearLayoutManager mPopularToiletsLayoutManager;
    private RecyclerView.Adapter mPopularToiletsAdapter;

    private static final int MAX_TOILETS_ON_LIST = 10;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mPopularToiletsRecyclerView = (RecyclerView) view.findViewById(R.id.main_most_reviewed_toilet_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mPopularToiletsRecyclerView.hasFixedSize();
        mPopularToiletsRecyclerView.setNestedScrollingEnabled(false);

        // use a linear layout manager
        mPopularToiletsLayoutManager = new LinearLayoutManager(getContext());
        mPopularToiletsLayoutManager.setReverseLayout(true);
        mPopularToiletsLayoutManager.setStackFromEnd(true);

        mPopularToiletsRecyclerView.setLayoutManager(mPopularToiletsLayoutManager);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");
        Query popularQuery = mRef.orderByChild("count").limitToLast(MAX_TOILETS_ON_LIST);

        // display data to the user
        mPopularToiletsAdapter = new FirebaseRecyclerAdapter<Toilet, ToiletViewHolder>(Toilet.class, R.layout.toilet_list_item, ToiletViewHolder.class, popularQuery) {
            @Override
            protected void populateViewHolder(ToiletViewHolder toiletViewHolder, Toilet toilet, int position) {
                // create list items
                toiletViewHolder.setName(toilet.getName());
                String liftString = toilet.getLiftList().toString();
                toiletViewHolder.setToilet(toilet);
                toiletViewHolder.setToiletId(getRef(position).getKey());
                toiletViewHolder.setText(String.format("lift: %s rating: %.1f", liftString.substring(1, liftString.length() - 1), (double) toilet.getTotal_score() / toilet.getCount()));
            }


        };

        mPopularToiletsRecyclerView.setAdapter(mPopularToiletsAdapter);
        return view;

    }

}
