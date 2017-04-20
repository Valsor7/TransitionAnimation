package com.boost.transitionanimation;

import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        Profile profile = getIntent().getParcelableExtra(ProfileActivity.class.getSimpleName());
        if (profile != null){
            postponeEnterTransition();
            Log.d(TAG, "onCreate: " + profile.getAvatarUrl());
            setupIcon(profile);
        }
    }

    private void setupIcon(Profile profile) {
        Glide.with(this).load(profile.getAvatarUrl()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Log.d(TAG, "onResourceReady: ");
                startPostponedEnterTransition();
                return false;
            }
        }).into(mIvAvatar);
    }
}
