package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletListFragment extends Fragment {

    public static final String TAG = "ToiletListFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Map<ToiletViewHolder, String> toiletStrings = new HashMap<>();

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

        // data ready event listener
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // remove loading screen when data is ready
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

        // display data to the user
        mAdapter = new FirebaseRecyclerAdapter<Toilet, ToiletViewHolder>(Toilet.class, R.layout.toilet_list_item, ToiletViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(ToiletViewHolder toiletViewHolder, Toilet toilet, int position) {
                // create list items
                toiletViewHolder.setName(toilet.getName());
                String liftString = toilet.getLift().toString();
                toiletViewHolder.setToilet(toilet);
                toiletViewHolder.setToiletId(getRef(position).getKey());
                toiletViewHolder.setText(String.format("lift: %s rating: %.1f", liftString.substring(1, liftString.length() - 1), (double) toilet.getTotal_score() / toilet.getCount()));

                // create search string
                String content = getToiletContentString(toilet);
                toiletStrings.put(toiletViewHolder, content);
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        EditText searchBox = (EditText) view.findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if query string is empty, show all items
                if (s.length() == 0) {
                    for (Map.Entry<ToiletViewHolder, String> e : toiletStrings.entrySet()) {
                        ToiletViewHolder view = e.getKey();
                        view.show();
                    }
                    return;
                }

                // normalize query string
                List<String> queryList = getNormalizedQueryStringList(s);

                for (Map.Entry<ToiletViewHolder, String> e : toiletStrings.entrySet()) {
                    ToiletViewHolder view = e.getKey();
                    String content = e.getValue();

                    // show item if relevant
                    boolean show = false;
                    for (String ss : queryList)
                        if (content.contains(ss)) {
                            show = true;
                            break;
                        }
                    if (show)
                        view.show();
                    else
                        view.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @NonNull
    private List<String> getNormalizedQueryStringList(CharSequence s) {
        char[] charArray = s.toString().toLowerCase().toCharArray();
        StringBuilder strBdr = new StringBuilder("");
        for (char aCharArray : charArray)
            if (aCharArray >= '0' && aCharArray <= '9' ||
                    aCharArray >= 'a' && aCharArray <= 'z' ||
                    aCharArray == ' ' || aCharArray == '/')
                strBdr.append(aCharArray);
            else
                strBdr.append(" ");
        String queryString = strBdr.toString();
        while (queryString.contains("  "))
            queryString = queryString.replace("  ", " ");

        return Arrays.asList(queryString.split(" "));
    }

    @NonNull
    private String getToiletContentString(Toilet toilet) {
        StringBuilder strBdr = new StringBuilder("");
        strBdr.append(toilet.getFloor()).append(" ");
        strBdr.append(toilet.getFloor()).append("f ");
        strBdr.append(toilet.getFloor()).append("/f ");
        strBdr.append(toilet.getGender() == Toilet.Gender.M ? getPrefixString("male") :
                toilet.getGender() == Toilet.Gender.F ? getPrefixString("female") :
                        getPrefixString("male") + getPrefixString("female"));
        for (int k : toilet.getLift())
            strBdr.append(k).append(" ");
        strBdr.append(toilet.isHas_changing_room() ? getPrefixString("changing") : "");
        strBdr.append(toilet.isHas_shower() ? getPrefixString("showering") : "");

        return strBdr.toString().trim();
    }

    /**
     * Create concatenated prefix string
     * e.g.
     * input = "apple"
     * output = "a ap app appl apple"
     *
     * @param s input string
     * @return concatenated prefix string
     */
    private static String getPrefixString(String s) {
        StringBuilder stringBuilder = new StringBuilder(" ");
        for (int i = 1; i < s.length() + 1; i++)
            stringBuilder.append(s.substring(0, i)).append(' ');
        return stringBuilder.toString();
    }

}
