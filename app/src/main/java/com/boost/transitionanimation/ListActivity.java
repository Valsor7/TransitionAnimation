package com.boost.transitionanimation;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boost.transitionanimation.model.ApiModule;
import com.boost.transitionanimation.model.CatModel;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private static final int CATS_AMOUNT = 10;
    @BindView(R.id.rl_profiles)
    RecyclerView mProfilesRecyclerView;

    private List<Profile> mProfiles = new ArrayList<>();
    private ProfilesAdapter mProfilesAdapter;

    private OnClickProfileListener mViewHolderListener = new OnClickProfileListener() {
        @Override
        public void onClickItem(View view) {
            Profile profile = mProfiles.get(mProfilesRecyclerView.getChildAdapterPosition(view));
            ImageView ivAvatar = ButterKnife.findById(view, R.id.iv_avatar);
            openProfileActivity(ivAvatar, profile);
        }
    };

    private void openProfileActivity(ImageView ivAvatar, Profile profile) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.class.getSimpleName(), profile);
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(ListActivity.this, ivAvatar, getString(R.string.shared_image_tag));
        ActivityCompat.startActivity(ListActivity.this, intent, optionsCompat.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mProfilesAdapter = new ProfilesAdapter(mProfiles, mViewHolderListener);

        mProfilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProfilesRecyclerView.setAdapter(mProfilesAdapter);

        setupDummyData();
    }

    private void setupDummyData() {
        int cat = CATS_AMOUNT;

        while (cat != 0) {
            Profile profile = new Profile();
            profile.setFullName("My cat " + cat);
            mProfiles.add(profile);
            initCall(cat - 1);
            cat--;
        }
        mProfilesAdapter.notifyDataSetChanged();
    }

    private void initCall(final int cat) {
        Call<CatModel> meawCall = ApiModule.getApiInterface().getRandomUrl();
        meawCall.enqueue(new Callback<CatModel>() {
            @Override
            public void onResponse(Call<CatModel> call, Response<CatModel> response) {
                Log.d(TAG, "onResponse: ");
                if (response.body() != null)
                    Log.d(TAG, "onResponse: " + response.body());
                    mProfiles.get(cat).setAvatarUrl(response.body().getFile());
            }

            @Override
            public void onFailure(Call<CatModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
