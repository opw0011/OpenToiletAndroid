package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
;


/**
 * Created by samch on 2017/5/23.
 */
public class SearchStaticFragment extends Fragment {

    private EditText floorText;
    private EditText liftText;
    private Spinner genderSpinner;

    public enum Gender {
        M, F, Both
    }

    private Gender gender;

    public SearchStaticFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_static, container, false);
        floorText = (EditText) view.findViewById(R.id.tv_floor);
        liftText = (EditText) view.findViewById(R.id.tv_lift);
        genderSpinner = (Spinner) view.findViewById(R.id.genderSpinner);
        genderSpinner.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.pref_gender_list_titles, android.R.layout.simple_spinner_dropdown_item));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gender = Gender.Both;
                        break;
                    case 1:
                        gender = Gender.M;
                        break;
                    case 2:
                        gender = Gender.F;
                        break;
                }
                //TODO: find a better way to show the search button
                EditText mSearchEditText = (EditText) getActivity().findViewById(xyz.sahildave.widget.R.id.search_expanded_edit_text);
                mSearchEditText.setText(" ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public String getFloor() {
        return floorText.getText().toString();
    }

    public int getLiftNumber() {
        String string = liftText.getText().toString();
        if (string.length() == 0) return -1;
        return Integer.parseInt(string);
    }

    public Gender getGender() {
        return gender;
    }

}
