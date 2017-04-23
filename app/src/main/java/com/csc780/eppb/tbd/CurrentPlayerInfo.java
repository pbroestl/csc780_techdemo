package com.csc780.eppb.tbd;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Elaine on 4/17/2017.
 */

public class CurrentPlayerInfo extends RelativeLayout {

    View mRootView;
    TextView mNameTextView;
    TextView mHpTextView;
    ImageView mPictureImageView;

    public CurrentPlayerInfo(Context context) {
        super(context);
        init(context);
    }

    public CurrentPlayerInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mRootView = inflate(context, R.layout.player_info, this);
        mNameTextView = (TextView) mRootView.findViewById(R.id.playerName);
        mHpTextView = (TextView) mRootView.findViewById(R.id.playerHP);
        mPictureImageView = (ImageView) mRootView.findViewById(R.id.playerImg);

        Player player = new Player(context);
        mNameTextView.setText(player.getName());
        mHpTextView.setText(player.getHealth() + "/" + Player.MAX_HEALTH);
        mPictureImageView.setImageDrawable(player.getPicture());
        mNameTextView.setTextColor(Color.RED);
        mHpTextView.setTextColor(Color.RED);
    }

    private void updateHealth() {

    }
}
