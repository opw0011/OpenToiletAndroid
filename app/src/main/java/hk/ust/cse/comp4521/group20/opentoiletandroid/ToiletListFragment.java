package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;
import xyz.sahildave.widget.SearchViewLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletListFragment extends Fragment {

    public static final String TAG = "ToiletListFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Map<ToiletViewHolder, String> toiletStrings = new HashMap<>();
    private Query query;
    private int queryDoneOnFB = -1;
    private String searchString = "";

    private SearchStaticFragment searchStaticFragment;
    private SearchViewLayout searchViewLayout;

    public ToiletListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ToiletListFragment constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toilet_list, container, false);
        // get data from Firebase
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("toilet_items");
        query = mRef;
        mRecyclerView = (RecyclerView) view.findViewById(R.id.lvToilet);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // display data to the user
        setAdapter(true);
        searchViewLayout = (SearchViewLayout) getActivity().findViewById(R.id.search_view_container);
        if (searchViewLayout.getVisibility() == View.GONE) searchViewLayout.setVisibility(View.VISIBLE);
        searchViewLayout.handleToolbarAnimation(((MainActivity)getActivity()).getToolbar());
        searchStaticFragment = new SearchStaticFragment();
        searchViewLayout.setExpandedContentFragment(getActivity(), searchStaticFragment);
        searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                searchViewLayout.collapse();
                searchString = searchKeyword;
                if (searchStaticFragment.getFloor().length() != 0) {
                    query = mRef.orderByChild("floor").equalTo(searchStaticFragment.getFloor().toUpperCase());
                    queryDoneOnFB = 0;
                } else if (searchStaticFragment.getLiftNumber() != -1) {
                    query = mRef.orderByChild("lift/" + searchStaticFragment.getLiftNumber()).equalTo(true);
                    queryDoneOnFB = 1;
                } else if (searchStaticFragment.needAccessible()) {
                    query = mRef.orderByChild("has_accessible_toilet").equalTo(true);
                    queryDoneOnFB = 2;
                } else if (searchStaticFragment.needChanging()) {
                    query = mRef.orderByChild("has_changing_room").equalTo(true);
                    queryDoneOnFB = 3;
                } else if (searchStaticFragment.needShower()) {
                    query = mRef.orderByChild("has_shower").equalTo(true);
                    queryDoneOnFB = 4;
                } else {
                    query = mRef;
                    queryDoneOnFB = 5;
                }

                setAdapter(false);
            }
        });
        // show loading screen
        LoadingScreen loadingScreen = new LoadingScreen();

        getActivity().getFragmentManager().beginTransaction()
                .add(R.id.drawer_layout, loadingScreen).commit();
        // data ready event listener
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && getActivity() != null) {
                    // remove loading screen when data is ready
                    getActivity().getFragmentManager().beginTransaction()
                            .remove(loadingScreen).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // show error message
//                Snackbar.make(container, "Database error", 3000)
//                        .setAction("OK", v -> getActivity().getFragmentManager().beginTransaction()
//                                .remove(loadingScreen).commit())
//                        .show();
            }
        });
        return view;
    }

    private void setAdapter(boolean isCreateView) {
        toiletStrings.clear();

        mAdapter = new FirebaseRecyclerAdapter<Toilet, ToiletViewHolder>(Toilet.class, R.layout.toilet_list_item, ToiletViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ToiletViewHolder toiletViewHolder, Toilet toilet, int position) {
                // create list items
                toiletViewHolder.setName(toilet.getName());
                String liftString = toilet.getLiftList().toString();
                toiletViewHolder.setToilet(toilet);
                toiletViewHolder.setToiletId(getRef(position).getKey());
                toiletViewHolder.setText(String.format("lift: %s rating: %.1f", liftString.substring(1, liftString.length() - 1), (toilet.getCount() != 0)?(double) toilet.getTotal_score() / toilet.getCount():0));
                if (isCreateView) {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    Boolean accessiblePref = sharedPref.getBoolean("accessible_switch", false);
                    int genderPref = Integer.parseInt(sharedPref.getString("gender_list", "0"));
                    boolean check;
                    if (accessiblePref && genderPref != 0) check = toilet.isHas_accessible_toilet() && ((genderPref == 1) ? toilet.getGender().equals(Toilet.Gender.M) : toilet.getGender().equals(Toilet.Gender.F));
                    else if (accessiblePref) check = toilet.isHas_accessible_toilet();
                    else check = genderPref == 0 || ((genderPref == 1) ? toilet.getGender().equals(Toilet.Gender.M) : toilet.getGender().equals(Toilet.Gender.F));
                    if (check) toiletViewHolder.show();
                    else toiletViewHolder.hide();
                    return;
                }

                if (searchStaticFragment.getGender() != Toilet.Gender.Both && toilet.getGender() != searchStaticFragment.getGender()) {
                    toiletViewHolder.hide();
                    return;
                }

                boolean show = false;
                if (searchString.equals(" ") || searchString.length() == 0) {
                    show = true;
                } else {
                    // normalize query string
                    List<String> queryList = getNormalizedQueryStringList(searchString);
                    for (String ss : queryList) {
                        if (getToiletContentString(toilet).contains(ss)) {
                            show = true;
                            break;
                        }
                    }
                }
                if (show) {
                    switch (queryDoneOnFB) {
                        case 0:
                            if (searchStaticFragment.getLiftNumber() != -1) {
                                if (!toilet.getLiftList().contains(searchStaticFragment.getLiftNumber() + "")) {
                                    toiletViewHolder.hide();
                                    return;
                                }
                            }
                        case 1:
                            if (searchStaticFragment.needAccessible()) {
                                if (!toilet.isHas_accessible_toilet()) {
                                    toiletViewHolder.hide();
                                    return;
                                }
                            }
                        case 2:
                            if (searchStaticFragment.needChanging()) {
                                if (!toilet.isHas_changing_room()) {
                                    toiletViewHolder.hide();
                                    return;
                                }
                            }
                        case 3:
                            if (searchStaticFragment.needShower()) {
                                if (!toilet.isHas_shower()) {
                                    toiletViewHolder.hide();
                                    return;
                                }
                            }
                    }
                    toiletViewHolder.show();
                } else {
                    toiletViewHolder.hide();
                }
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        Log.d("RecyclerView Height", mRecyclerView.getMeasuredHeight()+"");
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
        for (String k : toilet.getLiftList())
            strBdr.append(k).append(" ");
        strBdr.append(toilet.isHas_changing_room() ? getPrefixString("changing") : "");
        strBdr.append(toilet.isHas_shower() ? getPrefixString("showering") : "");

        return strBdr.toString().trim();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchViewLayout.getVisibility() == View.GONE) searchViewLayout.setVisibility(View.VISIBLE);
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
