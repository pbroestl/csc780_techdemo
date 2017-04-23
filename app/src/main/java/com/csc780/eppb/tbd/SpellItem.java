package com.csc780.eppb.tbd;

import android.graphics.drawable.Drawable;

/**
 * Created by Elaine on 4/23/2017.
 */

public class SpellItem {
    private String mSpellName;
    private Drawable mGesturePic;

    public SpellItem(String spellName, Drawable gesturePic) {
        mSpellName = spellName;
        mGesturePic = gesturePic;
    }

    public String getSpellName() { return mSpellName; }

    public Drawable getGesturePic() { return mGesturePic; }
}
