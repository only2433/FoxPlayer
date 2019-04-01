package com.littlefox.media.fox.player.analytics;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.littlefox.media.fox.player.R;

import android.app.Activity;
import android.content.Context;




/**
 * 구글 애널리틱스를 사용하게하는  Helper 클래스
 * @author 정재현
 *
 */
public class GoogleAnalyticsHelper
{
	
	public enum TrackerName {
	    APP_TRACKER, // Tracker used only in this app.
	    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	  }

	private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	//TODO : 입력해야함.
	public static final String PROPERTY_ID = "UA-37277849-5";
	
	public static GoogleAnalyticsHelper sGoogleAnalyticsHelper = null;
	private GoogleAnalytics mGoogleAnalytics;
	private Tracker mTracker;
	
	public static GoogleAnalyticsHelper getInstance(Context context)
	{
		if(sGoogleAnalyticsHelper == null)
		{
			sGoogleAnalyticsHelper = new GoogleAnalyticsHelper();
			sGoogleAnalyticsHelper.init(context);
		}
		return sGoogleAnalyticsHelper;
	}
	
	/**
	 * 구글 애널리틱스를 초기화 시킨다.
	 * @param context
	 */
	private void init(Context context)
	{
		mGoogleAnalytics = GoogleAnalytics.getInstance(context);
		mGoogleAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
		mTracker = getTracker(TrackerName.APP_TRACKER);
		mTracker.enableAdvertisingIdCollection(true);
		mTracker.enableAutoActivityTracking(true);
		mTracker.enableAutoActivityTracking(true);
		mTracker.setSessionTimeout(300);
	}
	
	
	synchronized Tracker getTracker(TrackerName trackerId) {
	    if (!mTrackers.containsKey(trackerId)) {
	      Tracker t = (trackerId == TrackerName.APP_TRACKER) ? mGoogleAnalytics.newTracker(PROPERTY_ID)
	          : (trackerId == TrackerName.GLOBAL_TRACKER) ? mGoogleAnalytics.newTracker(R.xml.global_tracker)
	              : mGoogleAnalytics.newTracker(R.xml.ecommerce_tracker);
	      mTrackers.put(trackerId, t);
	  
	    }
	    return mTrackers.get(trackerId);
	  }

	
	/**
	 * 현재 보이는 화면을 전달한다.
	 * @param viewName
	 */
	public void sendCurrentAppView(String viewName)
	{
		mTracker.setScreenName(viewName);
		mTracker.send(new HitBuilders.AppViewBuilder().build());
	}
	
	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 */
	public void sendCurrentEvent(String category, String action)
	{
		mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).build());
	}
	
	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 * @param label 행동에 관한 세부적 내용
	 */
	public void sendCurrentEvent(String category, String action, String label)
	{
		mTracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
	}
	
	/**
	 * 현재 액티비티의 행동을 추적한다.
	 * @param activity
	 */
	public void startAnalytics(Activity activity)
	{
		mGoogleAnalytics.reportActivityStart(activity);
	}
	
	/**
	 * 현재 액티비티의 행동을 추적하는것을 그만둔다.
	 * @param activity
	 */
	public void stopAnalytics(Activity activity)
	{
		mGoogleAnalytics.reportActivityStop(activity);
	}
	
	
}
