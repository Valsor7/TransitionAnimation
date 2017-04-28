package com.boost.transitionanimation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;

/**
 * Created by root on 28.04.17.
 */

public class ItemEnterAnimator extends DefaultItemAnimator {
    private static final String TAG = "ItemEnterAnimator";

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "animateAdd: " + viewHolder.getLayoutPosition());
        if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
            lastAddAnimatedItem++;
            runEnterAnimation((ProfilesAdapter.ProfileViewHolder) viewHolder);
            return false;
        }

        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final ProfilesAdapter.ProfileViewHolder holder) {
        final int screenHeight = getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchAddFinished(holder);
            }
        })
                .start();
    }

    private int getScreenHeight(Context c) {
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;

    }
}
