/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.content.SharedPreferences;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import hk.ust.cse.comp4521.group20.opentoiletandroid.models.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkListFragment extends Fragment {

    /**
     * The constant TAG.
     */
    public static final String TAG = "BookmarkListFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Map<String, ToiletViewHolder> toiletMap = new HashMap<>();

    private SharedPreferences bookmarkPreference;
    private static final String BOOKMARK_FILE = "BookmarkFile";

    /**
     * Instantiates a new Bookmark list fragment.
     */
    public BookmarkListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ToiletListFragment constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.lvBookmarkToilet);

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

        // get models from Firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");

        // models ready event listener
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // remove loading screen when models is ready
                    getActivity().getFragmentManager().beginTransaction()
                            .remove(loadingScreen).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // show error message
                Snackbar.make(container, "Database error", 3000)
                        .setAction("OK", v -> getActivity().getFragmentManager().beginTransaction()
                                .remove(loadingScreen).commit())
                        .show();
            }
        });

        bookmarkPreference = getActivity().getSharedPreferences(BOOKMARK_FILE,0);
        Set bookmarks = bookmarkPreference.getStringSet("bookmarks", new HashSet<>());


//         display models to the user
        mAdapter = new FirebaseRecyclerAdapter<Toilet, ToiletViewHolder>(Toilet.class, R.layout.toilet_list_item, ToiletViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ToiletViewHolder toiletViewHolder, Toilet toilet, int position) {
                // create list items
                toiletViewHolder.setName(toilet.getName());
                String liftString = toilet.getLiftList().toString();
                toiletViewHolder.setToilet(toilet);
                toiletViewHolder.setToiletId(getRef(position).getKey());
                toiletViewHolder.setText(String.format("lift: %s rating: %.1f", liftString.substring(1, liftString.length() - 1), (toilet.getCount() != 0)?(double) toilet.getTotal_score() / toilet.getCount():0));

                toiletMap.put(getRef(position).getKey(), toiletViewHolder);

                if(bookmarks.contains(getRef(position).getKey())){
                    toiletViewHolder.show();
                } else {
                    toiletViewHolder.hide();
                }
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        bookmarkPreference = getActivity().getSharedPreferences(BOOKMARK_FILE,0);
        Set bookmarks = bookmarkPreference.getStringSet("bookmarks", new HashSet<>());

        for (String id : toiletMap.keySet()) {
            ToiletViewHolder toiletViewHolder = toiletMap.get(id);
            if (bookmarks.contains(id)) {
                toiletViewHolder.show();
            } else {
                toiletViewHolder.hide();
            }
        }
    }
}
