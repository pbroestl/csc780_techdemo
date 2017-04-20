package com.csc780.eppb.tbd;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Elaine on 4/19/2017.
 */

public class HelpDialogFragment extends DialogFragment {

    public static HelpDialogFragment newInstance() {
        return new HelpDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_help, container, false);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.list_spells, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);

        return v;
    }


}
