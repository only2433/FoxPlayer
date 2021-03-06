package com.littlefox.media.fox.player.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.littlefox.logmonitor.Log;




/**
 * 구글 애널리틱스를 사용하게하는  Helper 클래스
 * @author 정재현
 *
 */
public class GoogleAnalyticsHelper
{

	
	//TODO : 입력해야함.
	public static final String PROPERTY_ID = "UA-37277849-5";
	
	public static GoogleAnalyticsHelper sGoogleAnalyticsHelper = null;
	private FirebaseAnalytics mFirebaseAnalytics;
	
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
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
	}
	

	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 */
	public void sendCurrentEvent(String category, String action)
	{
		Log.i("Category : "+ category + ", Action : "+action);
		Bundle bundle = new Bundle();
		bundle.putString("action", action);
		mFirebaseAnalytics.logEvent(category, bundle);
	}
	
	/**
	 * 현재 사용자의 이벤트를 전달한다.
	 * @param category 액티비티
	 * @param action 특정 행동
	 * @param label 행동에 관한 세부적 내용
	 */
	public void sendCurrentEvent(String category, String action, String label)
	{
		Log.i("Category : "+ category + ", Action : "+action+", Label : "+label);
		Bundle bundle = new Bundle();
		bundle.putString("action", action);
		bundle.putString("label", label);
		mFirebaseAnalytics.logEvent(category, bundle);
	}
	

}
