package com.boost.transitionanimation;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ProfileViewHolder> {

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
        return mProfilesList.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        View mView;

        @BindView(R.id.iv_avatar)
        ImageView mAvatarImageView;

        @BindView(R.id.tv_name)
        TextView mName;

        ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }

        void onBind(Profile profile) {
            mName.setText(profile.getFullName());

            Glide.with(mView.getContext())
                    .load(profile.getAvatarUrl())
                    .dontAnimate()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(mAvatarImageView);
        }

        @OnClick(R.id.layout_container)
        public void clickProfile(){
            mClickListener.onClickItem(mView);
        }
    }
}
