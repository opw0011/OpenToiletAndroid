package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
;import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * Created by samch on 2017/5/23.
 */
public class SearchStaticFragment extends Fragment {

    private EditText floorText;
    private EditText liftText;
    private Spinner genderSpinner;
    private CheckBox accessibleCheckBox;
    private CheckBox changingCheckBox;
    private CheckBox showerCheckBox;

    private Toilet.Gender gender;

    public SearchStaticFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_static, container, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean accessiblePref = sharedPref.getBoolean("accessible_switch", false);
        int genderPref = Integer.parseInt(sharedPref.getString("gender_list", "0"));
        floorText = (EditText) view.findViewById(R.id.tv_floor);
        liftText = (EditText) view.findViewById(R.id.tv_lift);
        genderSpinner = (Spinner) view.findViewById(R.id.genderSpinner);
        genderSpinner.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.pref_gender_list_titles, android.R.layout.simple_spinner_dropdown_item));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        gender = Toilet.Gender.Both;
                        break;
                    case 1:
                        gender = Toilet.Gender.M;
                        break;
                    case 2:
                        gender = Toilet.Gender.F;
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
        genderSpinner.setSelection(genderPref);
        accessibleCheckBox = (CheckBox) view.findViewById(R.id.checkbox_accessible_toilet);
        accessibleCheckBox.setChecked(accessiblePref);
        changingCheckBox = (CheckBox) view.findViewById(R.id.checkbox_changing_room);
        showerCheckBox = (CheckBox) view.findViewById(R.id.checkbox_shower);
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

    public Toilet.Gender getGender() {
        return gender;
    }

    public boolean needAccessible() {
        return accessibleCheckBox.isChecked();
    }

    public boolean needChanging() {
        return changingCheckBox.isChecked();
    }

    public boolean needShower() {
        return showerCheckBox.isChecked();
    }

}
