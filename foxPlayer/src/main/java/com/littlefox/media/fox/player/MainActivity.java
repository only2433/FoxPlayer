package com.littlefox.media.fox.player;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.littlefox.library.view.extra.SwipeDisableViewPager;
import com.littlefox.library.view.scroller.FixedSpeedScroller;
import com.littlefox.library.view.text.SeparateTextView;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.analytics.GoogleAnalyticsHelper;
import com.littlefox.media.fox.player.base.BaseActivity;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.Font;
import com.littlefox.media.fox.player.fragment.SelectThumbnailFragment;
import com.littlefox.media.fox.player.object.result.InitInformationResult;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity
{
	@BindView(R.id.include_other_app_layout)
	RelativeLayout _OtherViewLayout;
	
	@BindView(R.id.main_title_text)
	SeparateTextView _TitleText;
	
	@BindView(R.id.player_viewpager)
	SwipeDisableViewPager _ViewPager;
	
	@BindView(R.id.other_app_title_text)
	TextView _OtherAppTitleText;
	
	@BindView(R.id.other_app_littlefox_eng_text)
	TextView _OtherAppLittlefoxEngText;

	@BindView(R.id.other_app_littlefox_ch_text)
	TextView _OtherAppLittlefoxChText;
	
	@BindView(R.id.other_app_littlefox_song_text)
	TextView _OtherAppLittlefoxSongText;
	
	@BindView(R.id.other_app_littlefox_storybook_text)
	TextView _OtherAppLittlefoxStoryBookText;
	
	public interface OnMainCommnuicateListener
	{
		public void movePlayTypeLayout();
		public void playVideo(int type);
	}
	private static final int DURATION_ANIMATION 			= 300;
	
	private ArrayList<Fragment> mSelectDisplayList;
	private int mCurrentChoiceType = Common.TYPE_ENGLISH;
	private FixedSpeedScroller mFixedSpeedScroller;
	private MainSelectionPagerAdapter mMainSelectionPagerAdapter;
	private Animation mOtherAppAnimation = null;
	private InitInformationResult mInitInformationResult;
	private GoogleAnalyticsHelper mGoogleAnalyticsHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		init();
		initFont();
	}
	
	private void init()
	{
		mCurrentChoiceType = getIntent().getIntExtra(Common.INTENT_SELECT_TYPE, Common.TYPE_ENGLISH);
		
		mInitInformationResult = (InitInformationResult) CommonUtils.getInstance(this).getPreferenceObject(Common.PARAMS_APP_INFO, InitInformationResult.class);
		
		mMainSelectionPagerAdapter = new MainSelectionPagerAdapter(getSupportFragmentManager());
		mMainSelectionPagerAdapter.addFragment(Common.TYPE_ENGLISH);
		mMainSelectionPagerAdapter.addFragment(Common.TYPE_CHINESE);
		_ViewPager.setAdapter(mMainSelectionPagerAdapter);
		
		initAdapterScroller();
		_ViewPager.setCurrentItem(mCurrentChoiceType);	
		setupOtherAppView();
	}
	
    private void initFont()
    {
    	_TitleText.setPaintFlags(_TitleText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
    	_TitleText.setTypeface(Font.getInstance(this).getRobotoBold());
    	_TitleText.setSeparateText(getResources().getString(R.string.title_intro_littlefox), " " +getResources().getString(R.string.title_intro_player))
		.setSeparateColor(getResources().getColor(R.color.color_ffffff), getResources().getColor(R.color.color_fff305))
		.setSeparateTextSize(CommonUtils.getInstance(this).getPixel(52), CommonUtils.getInstance(this).getPixel(52))
		.showView();
    	
    	_OtherAppTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_OtherAppLittlefoxEngText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_OtherAppLittlefoxChText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_OtherAppLittlefoxSongText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_OtherAppLittlefoxStoryBookText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	
    }
    
    private void changeStatusColor(boolean isPopupMode)
    {
    	if(Build.VERSION.SDK_INT >= Common.LOLLIPOP)
		{
    		if(isPopupMode)
    		{
    			CommonUtils.getInstance(this).setStatusBar(getResources().getColor(R.color.color_000000));
    		}
    		else
    		{
    			CommonUtils.getInstance(this).setStatusBar(getResources().getColor(R.color.color_3e95c1));
    		}
			
		}
    }
    
    private void setupOtherAppView()
    {
    	_OtherViewLayout.setVisibility(View.GONE);
    	mOtherAppAnimation = CommonUtils.getInstance(this).getScaleAnimation(DURATION_ANIMATION, 0.4f, 1.0f);
    	mOtherAppAnimation.setAnimationListener(new AnimationListener()
		{
			
			@Override
			public void onAnimationStart(Animation animation)
			{
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation){}
			
			@Override
			public void onAnimationEnd(Animation animation){
				changeStatusColor(true);
			}
		});
    }
    
    /**
     * 다른앱 보기 관련된 팝업을 보여주거나 사라지게 하는 메소드
     * @param isStart TRUE : 다른 앱보기 팝업이 보여진다.</p>FALSE : 다른 앱 보기 팝업이 사라진다.
     */
    private void settingOtherAppAnimation(boolean isStart)
    {
    	if(isStart)
    	{
    		Log.i("TRUE");
    		_OtherViewLayout.setVisibility(View.VISIBLE);
    		_OtherViewLayout.startAnimation(mOtherAppAnimation);
    	}
    	else
    	{
    		_OtherViewLayout.clearAnimation();
    		_OtherViewLayout.setVisibility(View.GONE);
    		changeStatusColor(false);
    	}
    	
    }
	
	private boolean isOtherAppPopupVisible()
	{
		return _OtherViewLayout.getVisibility() == View.VISIBLE ? true : false;
	}
	
	private void initAdapterScroller()
	{
		mFixedSpeedScroller = new FixedSpeedScroller(this, new LinearOutSlowInInterpolator());
		mFixedSpeedScroller.setDuration(DURATION_ANIMATION);
		
		try
		{
			Field scroller = ViewPager.class.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			scroller.set(_ViewPager, mFixedSpeedScroller);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void startVideoActivity(int type)
	{
		Intent intent;
		switch(type)
		{
		case Common.TYPE_CHINESE:
			mGoogleAnalyticsHelper.getInstance(this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_CHINESE_CONTENTS,
					mInitInformationResult.getChineseInformation().getContentId()+"_"+Common.ANALYTICS_LABEL_PLAY);
			intent = new Intent(this, PlayerActivity.class);
			intent.putExtra(Common.INTENT_PLAYER_PARAMS, (Parcelable)mInitInformationResult.getChineseInformation());
	    	startActivity(intent);
	    	overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		case Common.TYPE_ENGLISH:
			mGoogleAnalyticsHelper.getInstance(this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_CHINESE_CONTENTS,
					mInitInformationResult.getEnglishInformation().getContentId()+"_"+Common.ANALYTICS_LABEL_PLAY);
			intent = new Intent(this, PlayerActivity.class);
			intent.putExtra(Common.INTENT_PLAYER_PARAMS, (Parcelable)mInitInformationResult.getEnglishInformation());
	    	startActivity(intent);
	    	overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		}
	}
	
	@OnClick({R.id.button_setting, R.id.other_app_close_button, R.id.button_other_app_littlefox_eng, R.id.button_other_app_littlefox_ch, R.id.button_other_app_littlefox_song,
		R.id.button_other_app_rocket_girl, R.id.other_app_littlefox_eng_text, R.id.other_app_littlefox_ch_text, R.id.other_app_littlefox_song_text, R.id.other_app_littlefox_storybook_text})
	public void onSelectClick(View view)
	{
		switch(view.getId())
		{
		case R.id.button_setting:
			settingOtherAppAnimation(true);
			break;
		case R.id.other_app_close_button:
			settingOtherAppAnimation(false);
			break;
		case R.id.button_other_app_littlefox_eng:
		case R.id.other_app_littlefox_eng_text:
			mGoogleAnalyticsHelper.getInstance(MainActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_ENGLISH);
			CommonUtils.getInstance(MainActivity.this).startLinkMove(Common.URL_LITTLEFOX_ENGLISH);
			break;
		case R.id.button_other_app_littlefox_song:
		case R.id.other_app_littlefox_song_text:
			mGoogleAnalyticsHelper.getInstance(MainActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_SONG);
			CommonUtils.getInstance(MainActivity.this).startLinkMove(Common.URL_LITTLEFOX_SONG);
			break;
		case R.id.button_other_app_rocket_girl:
		case R.id.other_app_littlefox_storybook_text:
			mGoogleAnalyticsHelper.getInstance(MainActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_STORYBOOK_ROCKET_GIRL);
			CommonUtils.getInstance(MainActivity.this).startLinkMove(Common.URL_LITTLEFOX_ROCKET_GIRL);
			break;
		case R.id.button_other_app_littlefox_ch:
		case R.id.other_app_littlefox_ch_text:
			mGoogleAnalyticsHelper.getInstance(MainActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_CHINESE);
			CommonUtils.getInstance(MainActivity.this).startLinkMove(Common.URL_LITTLEFOX_CHINESE);
			break;
		}
	}
	
	@Override
	public void onBackPressed()
	{
		if(isOtherAppPopupVisible())
		{
			_OtherViewLayout.clearAnimation();
			_OtherViewLayout.setVisibility(View.GONE);
		}
		else
		{
			finish();
		}
	}

	private class MainSelectionPagerAdapter extends FragmentPagerAdapter
	{
		public MainSelectionPagerAdapter(FragmentManager fragmentManager)
		{
			super(fragmentManager);
			mSelectDisplayList = new ArrayList<Fragment>();
		}
		
		public void addFragment(int type)
		{
			Fragment fragment = null;
			switch(type)
			{
			case Common.TYPE_ENGLISH:
				fragment = SelectThumbnailFragment.getInstance();
				((SelectThumbnailFragment)fragment).settingType(Common.TYPE_ENGLISH, mInitInformationResult.getEnglishInformation().getTitle(), mInitInformationResult.getEnglishInformation().getThumbnalUrl());
				((SelectThumbnailFragment)fragment).setMainCommunicateListener(mOnMainCommnuicateListener);
				break;
			case Common.TYPE_CHINESE:
				fragment = SelectThumbnailFragment.getInstance();
				((SelectThumbnailFragment)fragment).settingType(Common.TYPE_CHINESE,mInitInformationResult.getChineseInformation().getTitle(), mInitInformationResult.getChineseInformation().getThumbnalUrl());
				((SelectThumbnailFragment)fragment).setMainCommunicateListener(mOnMainCommnuicateListener);
				break;
			}
			mSelectDisplayList.add(fragment);
		}
		@Override
		public Fragment getItem(int position)
		{
			return mSelectDisplayList.get(position);
		}

		@Override
		public int getCount()
		{
			return mSelectDisplayList.size();
		}
		
		
	};
	
	private OnMainCommnuicateListener mOnMainCommnuicateListener = new OnMainCommnuicateListener()
	{
		
		@Override
		public void playVideo(int type)
		{
			Log.i("type : "+type);
			startVideoActivity(type);
		}
		
		@Override
		public void movePlayTypeLayout()
		{
			// TODO 화면 변경
			if(_ViewPager.getCurrentItem() == Common.TYPE_ENGLISH)
			{
				_ViewPager.setCurrentItem(Common.TYPE_CHINESE, true);
			}
			else
			{
				_ViewPager.setCurrentItem(Common.TYPE_ENGLISH, true);
			}


		}
	};
}
