package com.jahepi.maze;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jahepi.maze.ads.AdListener;
import com.jahepi.maze.utils.Constant;

public class AndroidLauncher extends AndroidApplication implements AdListener {

	private static final String TAG = "AndroidLauncher";
	protected AdView adView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MobileAds.initialize(this, Constant.ADMOB_KEY);

		RelativeLayout relativeLayout = new RelativeLayout(this);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		View gameView = initializeForView(new Maze(this), config);

		relativeLayout.addView(gameView);

		adView = new AdView(this);
		adView.setAdListener(new com.google.android.gms.ads.AdListener() {
			@Override
			public void onAdLoaded() {
				int visibility = adView.getVisibility();
				adView.setVisibility(View.GONE);
				adView.setVisibility(visibility);
				Gdx.app.log(TAG, "Ad loaded");
			}
		});

		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(Constant.ADMOB_KEY);

		AdRequest.Builder builder = new AdRequest.Builder();
		if (Constant.ENABLE_TEST_ADDS) {
			builder.addTestDevice(Constant.ADMOB_TEST_ADS_KEY);
		}

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);

		if (Constant.ENABLE_ADDS) {
			relativeLayout.addView(adView, params);
			adView.loadAd(builder.build());
		}

		setContentView(relativeLayout);
	}

	@Override
	public void show(final boolean active) {
		if (!Constant.ENABLE_ADDS) {
			return;
		}
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (active) {
					adView.setVisibility(View.VISIBLE);
				} else {
					adView.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void showInterstitial() {

	}
}
