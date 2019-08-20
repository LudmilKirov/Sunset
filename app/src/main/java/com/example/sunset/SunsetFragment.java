package com.example.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resource = getResources();
        mBlueSkyColor = resource.getColor(R.color.blue_sky);
        mSunsetSkyColor = resource.getColor(R.color.sunset_sky);
        mNightSkyColor = resource.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });

        return view;
    }

    private void startAnimation() {
        //Return the local layout rect for the view
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        //Where the animation should start and end
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView,"y",sunYStart,sunYEnd)
                .setDuration(3000);
        //make the sun speed up a bit at the beginning
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,
                        "backgroundColor",
                        mBlueSkyColor,mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        //AnimatorListener tells you when an animation completes.
        // So you could write a listener that waits until the end
        // of the first animation at which time you can start the
        // second night sky animation.This is a huge hassle,though
        // and requires  a lot of listeners.
        // It is much easier to use an AnimatorSet.

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        //Run an Animator set
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();
    }
}
