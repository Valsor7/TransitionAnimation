package com.boost.transitionanimation;


import android.content.Context;
import android.graphics.Point;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileViewHolder> {
    private static final String TAG = "ProfilesAdapter";
    private List<Profile> mProfilesList = new ArrayList<>();
    private OnClickProfileListener mClickListener;

    ProfilesAdapter(List<Profile> mProfilesList, OnClickProfileListener listener) {
        this.mProfilesList = mProfilesList;
        mClickListener = listener;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);

        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        holder.onBind(mProfilesList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + mProfilesList.size());
        return mProfilesList.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView mAvatarImageView;

        @BindView(R.id.tv_name)
        TextView mName;

        ProfileViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void onBind(Profile profile) {
//

            mName.setText(profile.getFullName());
            mAvatarImageView.setTransitionName(itemView.getContext().getString(R.string.shared_image_tag) + getAdapterPosition());

            Glide.with(itemView.getContext())
                    .load(profile.getAvatarUrl())
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mAvatarImageView);
        }

        @OnClick(R.id.layout_container)
        public void clickProfile() {
            mClickListener.onClickItem(itemView);
        }


    }
}
