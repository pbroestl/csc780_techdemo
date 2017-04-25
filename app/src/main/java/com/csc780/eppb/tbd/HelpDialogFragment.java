package com.csc780.eppb.tbd;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        String[] spells = this.getResources().getStringArray(R.array.list_spells);
        SpellItem spellItems[] = new SpellItem[spells.length];

        for(int i = 0; i < spellItems.length; i++) {
            spellItems[i] = new SpellItem(spells[i], this.getResources().getDrawable(R.drawable.gesture_placeholder));
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.spellList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        SpellListAdapter spellListAdapter = new SpellListAdapter(spellItems);
        recyclerView.setAdapter(spellListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }


}
