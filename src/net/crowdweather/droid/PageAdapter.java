package net.crowdweather.droid;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {

	private Context mContext;
	private final int MAP_TAG = 1;
	private final int HISTORY_TAG = 2;
	private final int CURRENT_WEATHER_TAG = 0 ;

	public PageAdapter(FragmentManager fm, Context context) {
		super(fm);
		mContext = context;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub

		switch (position) {
		case MAP_TAG:
			return "Map";
		case HISTORY_TAG:
			return "History";
		case CURRENT_WEATHER_TAG:
			return "Home";
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Fragment getItem(int position) {

		Fragment pageFragment = null;

		switch (position) {
		case MAP_TAG:
			pageFragment = new MapViewFragment();
			break;
		case HISTORY_TAG:
			pageFragment = new HistoryFragment();
			break;
		case CURRENT_WEATHER_TAG:
			pageFragment = new CurrentWeatherFragment();
		}

		return pageFragment;
	}
}