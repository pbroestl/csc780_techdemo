package com.csc780.eppb.tbd;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Elaine on 4/23/2017.
 */

public class SpellListAdapter extends RecyclerView.Adapter<SpellListAdapter.ViewHolder> {

    private SpellItem[] spellItems;

    public SpellListAdapter(SpellItem[] spellItems) {
        this.spellItems = spellItems;
    }

    @Override
    public SpellListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View spellItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spell_item, null);

        ViewHolder viewHolder = new ViewHolder(spellItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(spellItems[position].getSpellName());
        viewHolder.mImageView.setImageDrawable(spellItems[position].getGesturePic());
    }

    public int getItemCount() {
        return spellItems.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View spellItemView) {
            super(spellItemView);
            mTextView = (TextView) spellItemView.findViewById(R.id.spellName);
            mImageView = (ImageView) spellItemView.findViewById(R.id.gesturePic);
        }
    }


}
