package hk.ust.cse.comp4521.group20.opentoiletandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToiletListFragment extends Fragment {

    public static final String TAG = "ToiletListFragment";

    private static final Toilet[] toiletsDummyData = {
            new Toilet("4/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 4.9, 9, 2, true, false, true),
            new Toilet("4/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 4.2, 4, 2, true, false, true),
            new Toilet("3/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 4.2, 9, 2, false, false, false),
            new Toilet("3/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 4.0, 0, 0, false, false, false),
            new Toilet("2/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 2.2, 9, 2, false, false, false),
            new Toilet("2/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 1.0, 4, 2, false, false, false),
            new Toilet("4/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 4.9, 9, 2, true, false, true),
            new Toilet("4/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 4.2, 4, 2, true, false, true),
            new Toilet("3/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 4.2, 9, 2, false, false, false),
            new Toilet("3/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 4.0, 0, 0, false, false, false),
            new Toilet("2/F Male Toilet Near Lift 19", 19, Toilet.Gender.Male, 2.2, 9, 2, false, false, false),
            new Toilet("2/F Female Toilet Near Lift 19", 19, Toilet.Gender.Female, 1.0, 4, 2, false, false, false),
    };

    public ToiletListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ToiletListFragment constructor");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_toilet_list, container, false);

        ListView listView = (ListView) view.findViewById(R.id.lvToilet);

        // List view adapter
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        return view;


    }

    // Adapter to connect data to the list view
    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return toiletsDummyData.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getActivity().getLayoutInflater().inflate(R.layout.toilet_list_item, null);
            TextView textViewName = (TextView) convertView.findViewById(R.id.tvName);
            TextView textViewDescription = (TextView) convertView.findViewById(R.id.tvDescription);
            Toilet toilet = toiletsDummyData[position];
            textViewName.setText(toilet.getName());
            textViewDescription.setText(String.format("lift:%s, like:%d, dislike:%d", toilet.getLift(), toilet.getLike(), toilet.getDislike()));
            return convertView;
        }
    }

}
