package com.littlefox.media.fox.player;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.littlefox.library.system.async.listener.AsyncListener;
import com.littlefox.library.system.common.FileUtils;
import com.littlefox.library.view.controller.FadeAnimationController;
import com.littlefox.library.view.controller.FadeAnimationInformation;
import com.littlefox.library.view.media.ProgressiveMediaListener;
import com.littlefox.library.view.media.ProgressiveMediaPlayer;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.analytics.GoogleAnalyticsHelper;
import com.littlefox.media.fox.player.async.InitVideoInformationAsync;
import com.littlefox.media.fox.player.async.PlayerSaveRecordAsync;
import com.littlefox.media.fox.player.base.BaseActivity;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.Feature;
import com.littlefox.media.fox.player.common.Font;
import com.littlefox.media.fox.player.database.PlayedContentDBHelper;
import com.littlefox.media.fox.player.object.PlayedContentInformation;
import com.littlefox.media.fox.player.object.VideoInformationResult;
import com.littlefox.media.fox.player.object.WebInitInformation;
import com.littlefox.media.fox.player.object.base.BaseResult;
import com.littlefox.media.fox.player.object.result.CaptionInformationResult;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlayerMobileWebActivity extends BaseActivity
{
	@BindView(R.id.progressive_player)
	ProgressiveMediaPlayer _ProgressMediaPlayer;
	
	@BindView(R.id.player_background)
	ImageView _BackgroundDiscoverImage;
	
	@BindView(R.id.progress_wheel_layout)
	ScalableLayout _LoadingLayout;
	
	@BindView(R.id.player_top_base_layout)
	ScalableLayout _TopViewLayout;
	
	@BindView(R.id.player_top_title)
	TextView _TopTitleText;
	
	@BindView(R.id.player_subtitle_button)
	ImageView _TopCaptionSettingButton;
	
	@BindView(R.id.player_close_button)
	ImageView _TopCloseButton;
	
	@BindView(R.id.player_bottom_base_layout)
	ScalableLayout _BottomViewLayout;
	
	@BindView(R.id.player_current_play_time)
	TextView _CurrentPlayTimeText;
	
	@BindView(R.id.seekbar_play)
	SeekBar _ThumbSeekbar;
	
	@BindView(R.id.player_remain_play_time)
	TextView _RemainPlayTimeText;
	
	@BindView(R.id.player_lock_button)
	ImageView _LockButton;
	
	@BindView(R.id.player_play_button_layout)
	ScalableLayout _PlayButtonLayout;
	
	@BindView(R.id.player_prev_button)
	ImageView _PrevButton;
	
	@BindView(R.id.player_play_button)
	ImageView _PlayButton;
	
	@BindView(R.id.player_next_button)
	ImageView _NextButton;

	@BindView(R.id.player_preview_layout)
	ScalableLayout _PreviewLayout;
	
	@BindView(R.id.player_preview_title)
	TextView _PreviewProgressText;
	
	@BindView(R.id.subtitle_setting_layout)
	RelativeLayout _BaseSubTitleLayout;
	
	@BindView(R.id.subtitle_close_button)
	ImageView _SubtitleCloseButton;
	
	@BindView(R.id.subtitle_normal_layout)
	ScalableLayout _SubTitleNormalButton;
	
	@BindView(R.id.subtitle_normal_icon)
	ImageView _SubTitleNormalIcon;
	
	@BindView(R.id.subtitle_normal_text)
	TextView _SubTitleNormalText;
	
	@BindView(R.id.subtitle_use_layout)
	ScalableLayout _SubTitleUseButton;
	
	@BindView(R.id.subtitle_use_icon)
	ImageView _SubTitleUseIcon;
	
	@BindView(R.id.subtitle_use_text)
	TextView _SubTitleUseText;
	
	@BindView(R.id.play_end_layout)
	RelativeLayout _BasePlayEndLayout;
	
	@BindView(R.id.play_end_replay_layout)
	ScalableLayout _PlayEndReplayLayout;
	
	@BindView(R.id.play_end_replay_text)
	TextView _PlayEndReplayText;
	
	@BindView(R.id.play_end_preview_title_text)
	TextView _PlayEndPreviewText;
	
	@BindView(R.id.player_caption_layout)
	ScalableLayout _CaptionLayout;
	
	@BindView(R.id.player_caption_title)
	TextView _CaptionTitleText;
	class StudyTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			mStudyTime++;
		}
		
	}
	
	class UiTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			if(Feature.IS_FREE_USER)
			{
				mMainHandler.sendEmptyMessage(MESSAGE_UPDATE_FREE_UI);
			}
			else
			{
				mMainHandler.sendEmptyMessage(MESSAGE_UPDATE_PAID_UI);
			}
		}
	}
	
	Handler mMainHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MESSAGE_PROGRESS_UI:
				updateUI();
				break;
			case MESSAGE_PLAYER_PAUSE:
				setPlayStatus(PLAYER_PAUSE);
				break;
			case MESSAGE_PLAY_START:
				if(isPlayComplete == false && (_ProgressMediaPlayer.getMediaPlayerStatus() == ProgressiveMediaPlayer.STATUS_STOP || _ProgressMediaPlayer.getMediaPlayerStatus() == ProgressiveMediaPlayer.STATUS_PAUSE))
				{
					_ProgressMediaPlayer.start();
					isPlayComplete = false;
					showLoading(true);
				}
				break;
			case MESSAGE_INIT_PLAY:
				playVideo(VIDEO_INIT_PLAY);
				break;
			case MESSAGE_PREV_PLAY:
				requestPlaySaveRecord();
				playVideo(VIDEO_PREV_PLAY);
				break;
			case MESSAGE_NEXT_PLAY:
				requestPlaySaveRecord();
				playVideo(VIDEO_NEXT_PLAY);
				break;
			case MESSAGE_REPLAY:
				requestPlaySaveRecord();
				playVideo(VIDEO_REPLAY);
				break;
			case MESSAGE_LAYOUT_SETTING:
				settingLayout(msg.arg1);
				break;
			case MESSAGE_VIDEO_VISIBLE:
				_BackgroundDiscoverImage.setVisibility(View.GONE);
				break;
			case MESSAGE_LOCK_MODE_READY:
				mVibrator.vibrate(DURATION_PLAY);
				if(isLockDisplay)
				{
					Log.f("LOCK MODE ON");
					showMenuWithoutPlayButton(false);
				}
				else
				{
					Log.f("LOCK MODE OFF");
					showMenu(false);
				}
				isLockDisplay = (Boolean) msg.obj;
				Message subMessage = Message.obtain();
				subMessage.what = MESSAGE_LOCK_MODE_SET;
				subMessage.obj = msg.obj;
				mMainHandler.sendMessageDelayed(subMessage, DURATION_VIEW_ANIMATION);
				break;
			case MESSAGE_LOCK_MODE_SET:
				setLockModeUI();
				if(isLockDisplay)
				{
					showMenuWithoutPlayButton(true);
				}
				else
				{
					showMenu(true);
				}
				break;
			case MESSAGE_UPDATE_PAID_UI:
				updateUI();
				break;
			case MESSAGE_UPDATE_FREE_UI:
				updatePreviewUI();
				break;
			case MESSAGE_RESUME_SHOW_MENU:
				showMenu(true);
				break;
			case MESSAGE_FINISH:
				finish();
				break;
			case MESSAGE_DELETE_FILES:
				deleteAllContentVideoFile();
				break;
			case MESSAGE_INIT_VIEW_SETTING:
				initCaptionLayout();
				initViewSetting();
				break;
				
			case MESSAGE_CAPTION_LAYOUT_SETTING:
				setCaptionIconStatus();
				setCaptionLayoutVisible(isCaptionPlaying);
				break;
			}
		}
	};
	
	private static final int SECOND 						= 1000;
	private static final int DURATION_LOCK_MODE 			= 2000;
	private static final int DURATION_PLAY 					= 500;
	private static final int DURATION_GONE_BACKGROUND 		= 1000;
	private static final int DUARAION_RESUME_MENU 			= 300;
	
	private static final int VIDEO_INIT_PLAY 	= 0;
	private static final int VIDEO_PREV_PLAY	= 1;
	private static final int VIDEO_NEXT_PLAY 	= 2;
	private static final int VIDEO_REPLAY		= 3;
	
	private static final int MESSAGE_PROGRESS_UI 				= 0;
	private static final int MESSAGE_PLAYER_PAUSE 				= 1;
	private static final int MESSAGE_PLAY_START					= 2;
	private static final int MESSAGE_INIT_PLAY					= 3;
	private static final int MESSAGE_PREV_PLAY					= 4;
	private static final int MESSAGE_NEXT_PLAY					= 5;
	private static final int MESSAGE_REPLAY						= 6;
	private static final int MESSAGE_LAYOUT_SETTING 			= 7;
	private static final int MESSAGE_VIDEO_VISIBLE				= 8;
	private static final int MESSAGE_LOCK_MODE_READY			= 9;
	private static final int MESSAGE_LOCK_MODE_SET				= 10;
	private static final int MESSAGE_UPDATE_PAID_UI 			= 11;
	private static final int MESSAGE_UPDATE_FREE_UI				= 12;
	private static final int MESSAGE_RESUME_SHOW_MENU			= 13;
	private static final int MESSAGE_FINISH						= 14;
	private static final int MESSAGE_DELETE_FILES				= 15;
	private static final int MESSAGE_INIT_VIEW_SETTING			= 16;
	private static final int MESSAGE_CAPTION_LAYOUT_SETTING		= 17;
	
	private static final int LAYOUT_TYPE_DEFAULT 				= 0;
	private static final int LAYOUT_TYPE_SUBTITLE_DIALOG				= 1;
	private static final int LAYOUT_TYPE_PREVIEW_PLAY 			= 2;
	private static final int LAYOUT_TYPE_PREVIEW_END 			= 3;
	private static final int LAYOUT_TYPE_PLAY_END				= 4;

	
	private static final int DURATION_VIEW_ANIMATION 			= 500;
	private static final int DURATION_VIEW_INIT					= 1500;
	
	private static final int PLAYER_RESUME = 0;
	private static final int PLAYER_PAUSE	= 1;
	
	private static final String CAPTION = "caption";

	private FadeAnimationController mFadeAnimationController;
	private boolean isPlayComplete = false;
	private boolean isVideoLoadingComplete = false;
	private Timer mStudyRecordTimer = null;
	private Timer mUiCurrentTimer = null;
	private String mCurrentPlayUrl = "";
	private int mCurrentPlayPosition = -1;
	private int mFreeUserPreviewTime = -1;
	private int mCurrentLayoutType = LAYOUT_TYPE_DEFAULT;
	
	private Vibrator mVibrator;
	private PlayedContentDBHelper mPlayedContentDBHelper;
	private String mCurrentPlayContentId = "";
	private boolean isAuthContentPlayPossible = false;
	private CaptionInformationResult mCaptionInformationResult;
	private int mCurrentCaptionIndex = 0;
	
	/**
	 * 학습시간, 1초마다 갱신되며, 종료되거나 새 영상을 볼때 갱신된다.
	 */
	private int mStudyTime = 0;
	
	/**
	 * 락버튼이 ON 이 되었는 지 , OFF 인지 
	 */
	private boolean isLockDisplay = false;
	
	private WebInitInformation mWebInitInformation;
	private VideoInformationResult mVideoInformationResult;
	private GoogleAnalyticsHelper mGoogleAnalyticsHelper;

	/**
	 * 자막을 보고있는 중인지 아닌지를 체크
	 */
	private boolean isCaptionPlaying = false;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if(Feature.IS_TABLET && Feature.IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY)
		{
			Log.f("NOT SUPPORT DISPLAY");
			setContentView(R.layout.player_mobile_web_main_not_support_display);
		}
		else
		{
			setContentView(R.layout.player_mobile_web_main);
		}
		
		
		ButterKnife.bind(this);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Log.f("");
		mWebInitInformation = (WebInitInformation) CommonUtils.getInstance(this).getPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, WebInitInformation.class);
		initFont();
		initText();
		init();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Log.f("");
		mMainHandler.sendEmptyMessage(MESSAGE_PLAY_START);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		Log.f("");
		isLockDisplay = false;
		setLockModeUI();
		if(isAuthContentPlayPossible)
		{
			_ProgressMediaPlayer.stop();
		}
		
		setPlayIconStatus(PLAYER_PAUSE);
		showMenuWithoutAnimation(false);
		enableTimer(false);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.f("");
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.f("");
		if(isAuthContentPlayPossible)
		{
			requestPlaySaveRecord();
			
			_ProgressMediaPlayer.release();
		}
		mPlayedContentDBHelper.release();

		mMainHandler.sendEmptyMessage(MESSAGE_DELETE_FILES);
		
	}
	
	private void init()
	{	
		mPlayedContentDBHelper = PlayedContentDBHelper.getInstance(this);
		mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		setCaptionLayoutStatus(isCaptionPlaying);
		_LockButton.setOnTouchListener(mLockControlListener);
		
		_ThumbSeekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);

		LayerDrawable layerDrawable = (LayerDrawable)getResources().getDrawable(R.drawable.seekbar_thumb);
		GradientDrawable rectDrawable = (GradientDrawable)layerDrawable.findDrawableByLayerId(R.id._thumbRect);
		GradientDrawable circleDrawable = (GradientDrawable)layerDrawable.findDrawableByLayerId(R.id._thumbCircle);

		rectDrawable.setSize(CommonUtils.getInstance(this).getPixel(45), CommonUtils.getInstance(this).getPixel(45));
		circleDrawable.setSize(CommonUtils.getInstance(this).getPixel(40), CommonUtils.getInstance(this).getPixel(40));
		
		_TopViewLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		
		_BottomViewLayout.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				return true;
			}
		});
		_ProgressMediaPlayer.setOnTouchListener(mMenuVisibleListener);
		registerFadeControllerView();
		
		_ProgressMediaPlayer.register(Common.PATH_MP4_SAVE, mProgressPlayerListener);
		mMainHandler.sendEmptyMessageDelayed(MESSAGE_INIT_PLAY, 500);
	}
	

	
	private void initViewSetting()
	{
		
		if(Feature.IS_FREE_USER)
		{
			settingLayout(LAYOUT_TYPE_PREVIEW_PLAY); 
		}
		else
		{
			showMenuWithoutAnimation(false);
			settingLayout(LAYOUT_TYPE_DEFAULT);
		}
		
	}
	
	private void initFont()
	{
		_CurrentPlayTimeText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_PreviewProgressText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_RemainPlayTimeText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_SubTitleNormalText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_SubTitleUseText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_TopTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_PlayEndReplayText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_PlayEndPreviewText.setTypeface(Font.getInstance(this).getRobotoMedium());
		_CaptionTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
	}
	
	private void initText()
	{
		_PreviewProgressText.setText(getResources().getString(R.string.message_preview));
		_SubTitleNormalText.setText(getResources().getString(R.string.button_subtitle_default));
		_SubTitleUseText.setText(getResources().getString(R.string.button_subtitle_have));
		_PlayEndReplayText.setText(getResources().getString(R.string.button_replay));
	}
	
	private void initCaptionLayout()
	{
		if(mVideoInformationResult.isCaptionDataUse() && mWebInitInformation.getContentType(mCurrentPlayPosition).equals(String.valueOf(Common.REQUEST_CONTENT_TYPE_SONG)) == false)
		{
			setCaptionControlButtonVisible(true);
			setCaptionLayoutStatus(isCaptionPlaying);
			if(mCaptionInformationResult.getCaptionDetailInformationList().size() > 0)
			{
				mMainHandler.sendEmptyMessage(MESSAGE_CAPTION_LAYOUT_SETTING);
			}
		}
		else
		{
			setCaptionControlButtonVisible(false);
		}
		
		
	}
	
	private void setCaptionLayoutVisible(boolean isVisible)
	{
		mFadeAnimationController.promptViewStatus(_CaptionLayout, isVisible);
	}
	

	
	private void setCaptionControlButtonVisible(boolean isCaptionDataHave)
	{
		if(isCaptionDataHave)
		{
			_TopCaptionSettingButton.setVisibility(View.VISIBLE);
		}
		else
		{
			_TopCaptionSettingButton.setVisibility(View.GONE);
		}
	}
	
	private boolean isCaptionLayoutVisible()
	{
		if(_CaptionLayout.getVisibility() == View.VISIBLE)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 자막 등장 사라짐의 애니메이션을 보여주는 메소드
	 * @param isVisible
	 */
	private void startCaptionLayoutAnimation(boolean isVisible)
	{
		if(isVisible)
		{
			if(isCaptionLayoutVisible() == false)
			{
				mFadeAnimationController.startAnimation(_CaptionLayout, FadeAnimationController.TYPE_FADE_IN);
			}
		}
		else
		{
			if(isCaptionLayoutVisible())
			{
				mFadeAnimationController.startAnimation(_CaptionLayout, FadeAnimationController.TYPE_FADE_OUT);
			}
		}
	}
	
	private void setCaptionIconStatus()
	{
		if(isCaptionPlaying)
		{
			_TopCaptionSettingButton.setImageResource(R.drawable.player_icon_subtitle_yellow);
		}
		else
		{
			_TopCaptionSettingButton.setImageResource(R.drawable.icon_subtitle_white);
		}
	}
	
	private void setCaptionLayoutStatus(boolean isVisible)
	{
		
		if(isVisible)
		{
			_TopCaptionSettingButton.setImageResource(R.drawable.player_icon_subtitle_yellow);
			_SubTitleNormalButton.setBackgroundResource(R.drawable.player_btn_default);
			_SubTitleNormalIcon.setImageResource(R.drawable.player_icon_no_subtitle_yellow);
			_SubTitleNormalText.setTextColor(getResources().getColor(R.color.color_ffffff));
			_SubTitleUseButton.setBackgroundResource(R.drawable.player_btn_yellow);
			_SubTitleUseIcon.setImageResource(R.drawable.player_icon_subtitle_gray);
			_SubTitleUseText.setTextColor(getResources().getColor(R.color.color_333333));
		}
		else
		{
			_TopCaptionSettingButton.setImageResource(R.drawable.icon_subtitle_white);
			_SubTitleNormalButton.setBackgroundResource(R.drawable.player_btn_yellow);
			_SubTitleNormalIcon.setImageResource(R.drawable.player_icon_no_subtitle_gray);
			_SubTitleNormalText.setTextColor(getResources().getColor(R.color.color_333333));
			_SubTitleUseButton.setBackgroundResource(R.drawable.player_btn_default);
			_SubTitleUseIcon.setImageResource(R.drawable.player_icon_subtitle_yellow);
			_SubTitleUseText.setTextColor(getResources().getColor(R.color.color_ffffff));
		}
	}
	
	private void setVideoInformation()
	{
		_TopTitleText.setText(mVideoInformationResult.getTitle());
		_CurrentPlayTimeText.setText(CommonUtils.getInstance(this).getMillisecondTime(_ProgressMediaPlayer.getCurrentPlayerDuration()));
		_RemainPlayTimeText.setText(CommonUtils.getInstance(this).getMillisecondTime(_ProgressMediaPlayer.getMaxPlayerDuration()));
	}

	

	
	/**
	 * 저장되어 있는 모든 파일을 삭제한다.
	 */
	private void deleteAllContentVideoFile()
	{
		ArrayList<PlayedContentInformation> playedList = mPlayedContentDBHelper.getPlayedContentList(PlayedContentDBHelper.KEY_RECENT_PLAY_TIME, PlayedContentDBHelper.ORDER_DESC);
		Log.f("Saved File list size : "+ playedList.size());
		for(int i = 0 ; i < playedList.size(); i++)
		{
			Log.f("Deleted File Path  : "+ playedList.get(i).getFilePath()+", Content ID : "+ playedList.get(i).getContentID());
			mPlayedContentDBHelper.deletePlayedContent(playedList.get(i).getContentID());
		}
		
		FileUtils.deleteAllFileInPath(Common.PATH_MP4_SAVE);
	}
	


	/**
	 * 상태에 따라 레이아웃을 보여주는 메소드
	 * @param type
	 */
	private void settingLayout(int type)
	{
		Log.i("type : "+type);
		mCurrentLayoutType = type;
		
		_CurrentPlayTimeText.setText("00:00");
		_RemainPlayTimeText.setText("00:00");
		
		_BaseSubTitleLayout.clearAnimation();
		_BasePlayEndLayout.clearAnimation();
		switch(type)
		{
		case LAYOUT_TYPE_DEFAULT:
			_BaseSubTitleLayout.setVisibility(View.GONE);
			_BasePlayEndLayout.setVisibility(View.GONE);
			break;
		case LAYOUT_TYPE_SUBTITLE_DIALOG:
			_BaseSubTitleLayout.setVisibility(View.VISIBLE);
			_BaseSubTitleLayout.startAnimation(CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, -CommonUtils.getInstance(this).getPixel(1080), 0, new LinearOutSlowInInterpolator()));
			_BaseSubTitleLayout.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					return true;
				}
			});
			break;
		case LAYOUT_TYPE_PREVIEW_PLAY:
			//_TopCaptionSettingButton.setVisibility(View.INVISIBLE);
			//_BaseSubTitleLayout.setVisibility(View.GONE);
			_BottomViewLayout.setVisibility(View.INVISIBLE);
			_PreviewLayout.setVisibility(View.VISIBLE);
			break;
		case LAYOUT_TYPE_PLAY_END:
		case LAYOUT_TYPE_PREVIEW_END:
			_BasePlayEndLayout.setVisibility(View.VISIBLE);
			if(type == LAYOUT_TYPE_PREVIEW_END)
			{
				_PlayEndReplayLayout.setVisibility(View.GONE);
				_PlayEndPreviewText.setVisibility(View.VISIBLE);
			}
			_BasePlayEndLayout.startAnimation(CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, -CommonUtils.getInstance(this).getPixel(1080), 0, new LinearOutSlowInInterpolator()));
			_BasePlayEndLayout.setOnTouchListener(new OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					return true;
				}
			});
			break;

		}
	}
	
	private void setPlayIconStatus(int type)
	{
		if(type == PLAYER_RESUME)
		{
			_PlayButton.setImageResource(R.drawable.selector_player_pause_button);
		}
		else if(type == PLAYER_PAUSE)
		{
			_PlayButton.setImageResource(R.drawable.selector_player_play_button);
		}
	}

	/**
	 * 해당 컨텐츠 정보를 전달하여 비디오 정보를 요청한다.  
	 */
	private void requestCurrentPlayVideoUrlInformation()
	{
		mCurrentPlayContentId = isCaptionPlaying == true ? mWebInitInformation.getContentID(mCurrentPlayPosition) +"_caption" : mWebInitInformation.getContentID(mCurrentPlayPosition);
		
	
		if(isCaptionPlaying == true && mWebInitInformation.getContentType(mCurrentPlayPosition).equals(String.valueOf(Common.REQUEST_CONTENT_TYPE_SONG)) == false)
		{
			Log.i("Change");
			mWebInitInformation.setContentType(mCurrentPlayPosition, String.valueOf(Common.REQUEST_CONTENT_TYPE_MOVIE_WITH_CAPTION));
			CommonUtils.getInstance(this).setPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, mWebInitInformation);
		}
		
		InitVideoInformationAsync initVideoInformationAsync = new InitVideoInformationAsync(this);
		initVideoInformationAsync.setData(mCurrentPlayPosition);
		initVideoInformationAsync.setAsyncListener(mAsyncListener);
		initVideoInformationAsync.execute();
	}
	
	private boolean isLoadingMedia()
	{
		return _LoadingLayout.getVisibility() == View.VISIBLE ? true : false;
	}
	
	private void showLoading(boolean isLoading)
	{
		if(isLoading)
		{
			_LoadingLayout.setVisibility(View.VISIBLE);
		}
		else
		{
			_LoadingLayout.setVisibility(View.GONE);
		}
	}
	
	private void setLockModeUI()
	{
		
		if(isLockDisplay)
		{
			_LockButton.setImageResource(R.drawable.player_icon_lock);
			_TopCaptionSettingButton.setVisibility(View.INVISIBLE);
			_TopCloseButton.setVisibility(View.INVISIBLE);
			_CurrentPlayTimeText.setVisibility(View.INVISIBLE);
			_ThumbSeekbar.setVisibility(View.INVISIBLE);
			_RemainPlayTimeText.setVisibility(View.INVISIBLE);
		}
		else
		{
			_LockButton.setImageResource(R.drawable.player_icon_unlock);
			_TopCaptionSettingButton.setVisibility(View.VISIBLE);
			_TopCloseButton.setVisibility(View.VISIBLE);
			_CurrentPlayTimeText.setVisibility(View.VISIBLE);
			_ThumbSeekbar.setVisibility(View.VISIBLE);
			_RemainPlayTimeText.setVisibility(View.VISIBLE);
			
		}
	}
	
	/**
	 * 메뉴가 현재 보이는 상태인지 체크
	 * @return
	 */
	private boolean isMenuVisible()
	{
		Log.i("mCurrentLayoutType : " + mCurrentLayoutType+ " , _TopViewLayout  : "+ _TopViewLayout.getVisibility() + " , _BottomViewLayout.getVisibility() : "+_BottomViewLayout.getVisibility());
		Log.i("_PlayButtonLayout getVisibility: " + _PlayButtonLayout.getVisibility());
		if(mCurrentLayoutType == LAYOUT_TYPE_PREVIEW_PLAY)
		{
			if((_TopViewLayout.getVisibility()) == View.VISIBLE)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if((_TopViewLayout.getVisibility()  == View.VISIBLE || _BottomViewLayout.getVisibility() == View.VISIBLE || _PlayButtonLayout.getVisibility()== View.VISIBLE ))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
	}
	
	/**
	 * Top 과 Bottom 메뉴바를 내리거나 올린다.  프리뷰일때는 하단 과 플레이 중지 버튼의 애니메이션을 실행하지않는다.
	 * @param isVisible TRUE : 메뉴를 보이게한다. FALSE : 메뉴를 가린다.
	 */
	private void showMenu(boolean isVisible)
	{
		Log.i("isVisible : "+isVisible+", isMenuVisible : "+isMenuVisible());
		if(isVisible)
		{
			if(isMenuVisible() == false)
			{
				mFadeAnimationController.startAnimation(_TopViewLayout, FadeAnimationController.TYPE_FADE_IN);
				if(mCurrentLayoutType == LAYOUT_TYPE_DEFAULT)
				{
					mFadeAnimationController.startAnimation(_PlayButtonLayout, FadeAnimationController.TYPE_FADE_IN);
					mFadeAnimationController.startAnimation(_BottomViewLayout, FadeAnimationController.TYPE_FADE_IN);
				}

			}
		}
		else
		{
			if(isMenuVisible() == true)
			{
				mFadeAnimationController.startAnimation(_TopViewLayout, FadeAnimationController.TYPE_FADE_OUT);
				if(mCurrentLayoutType == LAYOUT_TYPE_DEFAULT)
				{
					mFadeAnimationController.startAnimation(_PlayButtonLayout, FadeAnimationController.TYPE_FADE_OUT);
					mFadeAnimationController.startAnimation(_BottomViewLayout, FadeAnimationController.TYPE_FADE_OUT);
					
				}
			}
		}
	}
	
	private void showMenuWithoutAnimation(boolean isVisible)
	{
		if (isVisible)
		{
			_TopViewLayout.setVisibility(View.VISIBLE);
			if (mCurrentLayoutType == LAYOUT_TYPE_DEFAULT)
			{
				_PlayButtonLayout.setVisibility(View.VISIBLE);
				_BottomViewLayout.setVisibility(View.VISIBLE);
			}

		}
		else
		{
			_TopViewLayout.setVisibility(View.GONE);
			if (mCurrentLayoutType == LAYOUT_TYPE_DEFAULT)
			{
				_PlayButtonLayout.setVisibility(View.GONE);
				_BottomViewLayout.setVisibility(View.GONE);
			}

		}
	}

	private void showMenuWithoutPlayButton(boolean isVisible)
	{
		if(isVisible)
		{
			mFadeAnimationController.startAnimation(_TopViewLayout, FadeAnimationController.TYPE_FADE_IN);
			mFadeAnimationController.promptViewStatus(_PlayButtonLayout, false);
			mFadeAnimationController.startAnimation(_BottomViewLayout, FadeAnimationController.TYPE_FADE_IN);
		}
		else
		{
			mFadeAnimationController.startAnimation(_TopViewLayout, FadeAnimationController.TYPE_FADE_OUT);
			mFadeAnimationController.promptViewStatus(_PlayButtonLayout, false);
			mFadeAnimationController.startAnimation(_BottomViewLayout, FadeAnimationController.TYPE_FADE_OUT);
		}

	}
	
	/**
	 * 현재 상태에 따라 PREV 버튼 , NEXT 버튼을 보이고 안보이는 처리를 한다.
	 */
	private void settingPlayMenu()
	{
		if(Feature.IS_FREE_USER)
		{
			initPreviewText();
		}
		
		//전체 재생 , 선택한 리스트 재생 일때
		if(mWebInitInformation.isMultiVideoPlay())
		{
			_PrevButton.setVisibility(View.VISIBLE);
			_NextButton.setVisibility(View.VISIBLE);
			if(mCurrentPlayPosition == 0)
			{
				_PrevButton.setVisibility(View.GONE);
			}
			else if(mCurrentPlayPosition >= (mWebInitInformation.getPlayItemCount() - 1))
			{
				_NextButton.setVisibility(View.GONE);
			}

		}
		// 한편만 재생 할 때
		else
		{
			_PrevButton.setVisibility(View.GONE);
			_NextButton.setVisibility(View.GONE);
		}
		
		//송은 자막이없다.
		if(mWebInitInformation.getContentType(mCurrentPlayPosition).equals(String.valueOf(Common.REQUEST_CONTENT_TYPE_SONG)))
		{
			_TopCaptionSettingButton.setVisibility(View.GONE);
		}
	}
	
	private void registerFadeControllerView()
	{
		final int CONTROLLER_VIEW_HEIGHT = CommonUtils.getInstance(this).getPixel(150);
		mFadeAnimationController = new FadeAnimationController(this);
		mFadeAnimationController.addControlView(new FadeAnimationInformation(_TopViewLayout, 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, -CONTROLLER_VIEW_HEIGHT, 0), 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, 0, -CONTROLLER_VIEW_HEIGHT)));
		mFadeAnimationController.addControlView(new FadeAnimationInformation(_BottomViewLayout, 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, CONTROLLER_VIEW_HEIGHT, 0), 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, 0, CONTROLLER_VIEW_HEIGHT)));
		mFadeAnimationController.addControlView(new FadeAnimationInformation(_PlayButtonLayout, 
				CommonUtils.getInstance(this).getAlphaAnimation(DURATION_VIEW_ANIMATION, 0.2f, 1.0f), 
				CommonUtils.getInstance(this).getAlphaAnimation(DURATION_VIEW_ANIMATION, 1.0f, 0.2f)));
		mFadeAnimationController.addControlView(new FadeAnimationInformation(_CaptionLayout, 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, CONTROLLER_VIEW_HEIGHT, 0), 
				CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, 0, CONTROLLER_VIEW_HEIGHT)));
	}
	
	private void requestPlaySaveRecord()
	{
		PlayerSaveRecordAsync async = new PlayerSaveRecordAsync(this);
		async.setData(mCurrentPlayPosition, mStudyTime);
		async.setAsyncListener(mAsyncListener);
		async.execute();
		mStudyTime = 0;
	}
	
	private void playVideo(int playType)
	{
		_ProgressMediaPlayer.reset();
		enableTimer(false);
		mStudyTime = 0;
		showMenu(false);
		showLoading(true);
		isVideoLoadingComplete = false;
		_BackgroundDiscoverImage.setVisibility(View.VISIBLE);
		

		
		switch(playType)
		{
		case VIDEO_INIT_PLAY:
			mCurrentPlayPosition = 0;
			break;
		case VIDEO_PREV_PLAY:
			mCurrentPlayPosition = getPrevPosition(mCurrentPlayPosition);
			showMenu(false);
			break;		
		case VIDEO_NEXT_PLAY:
			mCurrentPlayPosition = getNextPositionWhenSingle(mCurrentPlayPosition);
			showMenu(false);
			break;
		case VIDEO_REPLAY:
			break;
		}
		Log.i("mCurrentPlayPosition : "+mCurrentPlayPosition);
		settingPlayMenu();
		
		_ThumbSeekbar.setThumbOffset(CommonUtils.getInstance(this).getPixel(0));
		_ThumbSeekbar.setProgress(0);
		_ThumbSeekbar.setSecondaryProgress(0);
		
		
		makeAvailableStorageSizeToPlay();
		
		//Log.f("Current Play Title : "+ mContentPlayObject.getPlayObjectList().get(mCurrentPlayPosition).getTitle());
		//Log.f("Current Play Url : "+ mContentPlayObject.getPlayObjectList().get(mCurrentPlayPosition).getPlayUrl());
		//Log.f("Current Play Url ~~ : "+ mCurrentPlayUrl);
	//	
	}
	
	private void playProgressiveDownloadContent()
	{
		sendAnalytics();
		_ProgressMediaPlayer.startPlay(mCurrentPlayContentId, mCurrentPlayUrl, isCurrentVideoDownloadComplete());
	}
	

	private void makeAvailableStorageSizeToPlay()
	{
		long availableStorageSize = 0L;
		ArrayList<PlayedContentInformation> playedList = mPlayedContentDBHelper.getPlayedContentList(PlayedContentDBHelper.KEY_RECENT_PLAY_TIME, PlayedContentDBHelper.ORDER_DESC);
		
		availableStorageSize = CommonUtils.getInstance(this).getAvailableStorageSize(); 
		if(availableStorageSize <= Common.MIN_PLAYED_STORAGE_SIZE)
		{
			if(playedList != null)
			{
				for(int i = 0 ; i < playedList.size(); i++)
				{
					mPlayedContentDBHelper.deletePlayedContent(playedList.get(i).getContentID());
				}
			}
			FileUtils.deleteAllFileInPath(Common.PATH_MP4_SAVE);
			
			availableStorageSize = CommonUtils.getInstance(this).getAvailableStorageSize();
			if(availableStorageSize <= Common.MIN_PLAYED_STORAGE_SIZE)
			{
				Toast.makeText(this, getResources().getString(R.string.message_storage_size_warning), Toast.LENGTH_LONG).show();
				mMainHandler.sendEmptyMessageDelayed(MESSAGE_FINISH, DURATION_VIEW_INIT);
			}
			else
			{
				requestCurrentPlayVideoUrlInformation();
			}
		}
		else
		{
			requestCurrentPlayVideoUrlInformation();
		}
		
		
		
	}
	
	/**
	 * 현재 파일이 다운로드 되었는지 확인 하는 메소드
	 * @return
	 */
	private boolean isCurrentVideoDownloadComplete()
	{
		PlayedContentInformation playedContentInformation = mPlayedContentDBHelper.getContentInformation(mCurrentPlayContentId);
		Log.f("ID : " + mCurrentPlayContentId +", isDownloadComplete : " +playedContentInformation.isDownloadComplete());
		return playedContentInformation.isDownloadComplete();
	}
	
	private void savePlayFileDataBase()
	{
		PlayedContentInformation playedContentInformation = null;
		String FileSavePath = Common.PATH_MP4_SAVE + mCurrentPlayContentId;
		Log.i("FileSavePath : "+FileSavePath);
		if(mPlayedContentDBHelper.getContentInformation(mCurrentPlayContentId) == null)
		{
			
			playedContentInformation = new PlayedContentInformation(mCurrentPlayContentId, String.valueOf(System.currentTimeMillis()), FileSavePath, "0", false);
			mPlayedContentDBHelper.addPlayedContent(playedContentInformation);
		}
		else
		{
			mPlayedContentDBHelper.updatePlayedContent(mCurrentPlayContentId, PlayedContentDBHelper.KEY_RECENT_PLAY_TIME, String.valueOf(System.currentTimeMillis()));
		}
	}
	
	/**
	 * 총 플레이 시간을 저장
	 */
	private void saveTotalPlaytimeDataBase()
	{
		if(mPlayedContentDBHelper.getContentInformation(mCurrentPlayContentId) != null)
		{
			mPlayedContentDBHelper.updatePlayedContent(mCurrentPlayContentId, PlayedContentDBHelper.KEY_TOTAL_PLAY_TIME, String.valueOf(_ProgressMediaPlayer.getMaxPlayerDuration()/SECOND));
		}
	}
	
	/**
	 * UI 화면을 갱신하는 타이머를 동작시키거나 중지시킨다.
	 * @param isStart
	 */
	private void enableTimer(boolean isStart)
	{
		if(isStart)
		{
			if(mUiCurrentTimer == null)
			{
				mUiCurrentTimer = new Timer();
				mUiCurrentTimer.schedule(new UiTimerTask(), 0, 100);
			}
			
			if(mStudyRecordTimer == null)
			{
				mStudyRecordTimer = new Timer();
				mStudyRecordTimer.schedule(new StudyTimerTask(), 0, 1000);
			}
		}
		else
		{
			if(mUiCurrentTimer != null)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
			{
				mUiCurrentTimer.cancel();
				mUiCurrentTimer = null;
			}
			
			if(mStudyRecordTimer != null)
			{
				mStudyRecordTimer.cancel();
				mStudyRecordTimer = null;
			}
		}
	}
	
	/**
	 *  다운로드 도중 플레이가 될 수 있는 상황 인지 확인
	 */
	private void checkDownloadVideoStatus()
	{
		if(_ProgressMediaPlayer.isPlayAvailable())
		{
			if(_ProgressMediaPlayer.isVideoPlaying() == false)
			{
				Log.f("Wifi Status Bad : Play");
				showLoading(false);
				setPlayStatus(PLAYER_RESUME);
			}
		}
		else
		{
			if(isLoadingMedia() == false)
			{
				Log.f("Wifi Status Bad : Loading");
				showLoading(true);
				showMenu(false);
				setPlayStatus(PLAYER_PAUSE);
			}

		}
	}
	
	private long getMillisecond(int second)
	{
		return second * SECOND;
	}
	/**
	 * 프리로 보는 시간이 끝났다면 TRUE 아니면 FALSE 를 리턴
	 * @return
	 */
	private boolean isFreeUserLimitedTimeEnd()
	{
		if(_ProgressMediaPlayer.getCurrentPlayerDuration() >= getMillisecond(mFreeUserPreviewTime))
		{
			return true;
		}
		
		return false;
	}
	
	private void setPlayStatus(int type)
	{
		try
		{
			switch(type)
			{
			case PLAYER_PAUSE:
				Log.i("");
				_ProgressMediaPlayer.pause();
				break;
			case PLAYER_RESUME:
				Log.i("mProgressPlayer.getMediaPlayerStatus() : " + _ProgressMediaPlayer.getMediaPlayerStatus());
				if (_ProgressMediaPlayer.getMediaPlayerStatus() == ProgressiveMediaPlayer.STATUS_STOP)
				{
					_ProgressMediaPlayer.start();
				}
				else
				{
					_ProgressMediaPlayer.resume();
				}
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 단편 한편을 플레이 or 송 한편을 플레이 or 학습 자료 한편을 플레이시에 해당 포지션을 가져온다. 마지막편이 보여지면 1편을 다음으로 보여줘야함.
	 * @return
	 */
	private int getNextPositionWhenSingle(int position)
	{
		position++;
		
		if(position >= mWebInitInformation.getPlayItemCount())
		{
			position = 0;
		}
		return position;
	}
	
	private int getPrevPosition(int position)
	{
		position--;
		
		if(position < 0 )
		{
			position = 0;
		}
		
		return position;
	}
	
	
	private void initPreviewText()
	{
		_PreviewProgressText.setText(getResources().getString(R.string.message_preview));
	}
	
	private String getPreviewTextTime(int milisecondTime)
	{
		final int FREE_MAX_TIME = 60;
		int seconds  = 0;
        int totalSeconds = milisecondTime / 1000;
        
        if(totalSeconds == FREE_MAX_TIME)
        {
        	seconds = 60;
        }
        else
        {
        	seconds = totalSeconds % 60;
        }

        return String.valueOf(seconds);
	}
	
	private void updatePreviewUI()
	{
		if(_ProgressMediaPlayer != null && isPlayComplete == false && mUiCurrentTimer != null)
		{
			if (isFreeUserLimitedTimeEnd() && _ProgressMediaPlayer.isVideoPlaying())
			{
				Log.f("Preview Play Time End.");
				setPlayStatus(PLAYER_PAUSE);
				enableTimer(false);
				showMenu(false);
				if(isMenuVisible())
				{
					Message msg = Message.obtain();
					msg.what = MESSAGE_LAYOUT_SETTING;
					msg.arg1 = LAYOUT_TYPE_PREVIEW_END;
					mMainHandler.sendMessageDelayed(msg, DURATION_VIEW_ANIMATION);
				}
				else
				{
					settingLayout(LAYOUT_TYPE_PREVIEW_END);
				}
				
				return;
			}
			
			if(_ProgressMediaPlayer.isPreviewDownloadComplete() == false)
			{
				checkDownloadVideoStatus();
			}
			//미리보기 타이틀 갱신
			int remainPreviewTime = (int) (getMillisecond(mFreeUserPreviewTime) - _ProgressMediaPlayer.getCurrentPlayerDuration());

			_PreviewProgressText.setText(getResources().getString(R.string.message_preview) +getPreviewTextTime(remainPreviewTime)+ getResources().getString(R.string.second));
			
			if(Feature.IS_CAPTION_USED)
			{
				if(isTimeForCaption() == true)
				{
					_CaptionTitleText.setText(Html.fromHtml(mCaptionInformationResult.getCaptionDetailInformationList().get(mCurrentCaptionIndex).getText()));
					mCurrentCaptionIndex++;
				}
			}
		}
	}
	
	
	
	private void updateUI()
	{
		
		_ThumbSeekbar.post(new Runnable(){

			@Override
			public void run()
			{
				if(_ProgressMediaPlayer != null && isPlayComplete == false && mUiCurrentTimer != null)
				{
					if(_ProgressMediaPlayer.isDownloadComplete() == false)
					{
						checkDownloadVideoStatus();
					}
					
					try
					{
						_ThumbSeekbar.setProgress(_ProgressMediaPlayer.getMediaPlayProgress());
					}catch(Exception e)
					{
						e.printStackTrace();
					}
					
					_CurrentPlayTimeText.setText(CommonUtils.getInstance(PlayerMobileWebActivity.this).getMillisecondTime(_ProgressMediaPlayer.getCurrentPlayerDuration()));
					
					if(Feature.IS_CAPTION_USED)
					{
						if(isTimeForCaption() == true)
						{
							_CaptionTitleText.setText(Html.fromHtml(mCaptionInformationResult.getCaptionDetailInformationList().get(mCurrentCaptionIndex).getText()));
							mCurrentCaptionIndex++;
						}
					}
				}
			}
		});
	}
	
	/**
	 * 캡션에 대한 정보 처리 타이밍인지 확인 하는 메소드
	 * @return
	 */
	private boolean isTimeForCaption()
	{
		try
		{
			if(mCurrentCaptionIndex >= mCaptionInformationResult.getCaptionDetailInformationList().size())
			{
				return false;
			}

			float visibleTime = mCaptionInformationResult.getCaptionDetailInformationList().get(mCurrentCaptionIndex).getStartTime();
			if( visibleTime <= (float)_ProgressMediaPlayer.getCurrentPlayerDuration())
			{
				return true;
			}
			
		}catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}

		
		return false;
	}
	
	private int getCaptionCurrentIndex()
	{
		float visibleTime = 0L;
		visibleTime = mCaptionInformationResult.getCaptionDetailInformationList().get(0).getStartTime();
		if(visibleTime > (float)_ProgressMediaPlayer.getCurrentPlayerDuration())
		{
			return 0;
		}
		if(mCurrentCaptionIndex < mCaptionInformationResult.getCaptionDetailInformationList().size())
		{
			for(int i = 0; i < mCaptionInformationResult.getCaptionDetailInformationList().size(); i++)
			{
				visibleTime = mCaptionInformationResult.getCaptionDetailInformationList().get(i).getStartTime();;
				if( visibleTime >= (float)_ProgressMediaPlayer.getCurrentPlayerDuration())
				{
					return i;
				}

			}
		}
		
		
		return -1;
	}
	
	/**
	 * 플레이를 할때 해당 구글 애널리틱스 정보를 보냄
	 */
	private void sendAnalytics()
	{
		String action = "";
		if(Feature.IS_FREE_USER)
		{
			if(mWebInitInformation.getContentType(mCurrentPlayPosition).equals(String.valueOf(Common.REQUEST_CONTENT_TYPE_SONG)))
			{
				action = Common.ANALYTICS_ACTION_SONG_PREVIEW;
			}
			else
			{
				action = Common.ANALYTICS_ACTION_STORY_PREVIEW;
			}
			
		}
		else
		{
			if(mWebInitInformation.getContentType(mCurrentPlayPosition).equals(String.valueOf(Common.REQUEST_CONTENT_TYPE_SONG)))
			{
				action = Common.ANALYTICS_ACTION_SONG;
			}
			else
			{
				action = Common.ANALYTICS_ACTION_STORY;
			}
		}
		
		mGoogleAnalyticsHelper.getInstance(this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_PLAYER, action, 
				mWebInitInformation.getServiceCode()+"_"+mWebInitInformation.getContentID(mCurrentPlayPosition)+"_"+Common.ANALYTICS_LABEL_PLAY);
	}
	
	@OnClick({R.id.play_end_replay_layout, R.id.subtitle_normal_layout, R.id.subtitle_use_layout,R.id.subtitle_close_button})
	public void onDisplayButtonClick(View view)
	{
		if(mMainHandler.hasMessages(MESSAGE_REPLAY) || mMainHandler.hasMessages(MESSAGE_NEXT_PLAY))
		{
			return;
		}
		
		initViewSetting();
		
		switch(view.getId())
		{
		case R.id.subtitle_close_button:
			Log.f("Player Subtitle Page Close Button Click");
			break;
		case R.id.subtitle_normal_layout:
			Log.f("Player Subtitle Page Not Use Button Click");
			isCaptionPlaying = false;
			setCaptionIconStatus();
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_REPLAY, DURATION_PLAY);
			break;
		case R.id.subtitle_use_layout:
			Log.f("Player Subtitle Page Use Button Click");
			isCaptionPlaying = true;
			setCaptionIconStatus();
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_REPLAY, DURATION_PLAY);
			break;
		case R.id.play_end_replay_layout:
			Log.f("Player End Page Replay Button Click");
			settingLayout(LAYOUT_TYPE_DEFAULT);
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_REPLAY, DURATION_PLAY);
			break;
		}
	}
	
	@OnClick({R.id.play_end_close_button,R.id.player_close_button, R.id.player_next_button, R.id.player_play_button, R.id.player_prev_button, R.id.player_subtitle_button})
	public void onPlayerButtonClick(View view)
	{
		if(mMainHandler.hasMessages(MESSAGE_PREV_PLAY) || mMainHandler.hasMessages(MESSAGE_NEXT_PLAY))
		{
			return;
		}
		
		switch(view.getId())
		{
		case R.id.player_close_button:
		case R.id.play_end_close_button:
			Log.f("Player Main Close Button Click");
			finish();
			break;
		case R.id.player_next_button:
			Log.f("Player Next Button Click");
			showMenu(false);
			settingLayout(LAYOUT_TYPE_DEFAULT);
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_NEXT_PLAY, DURATION_PLAY);
			break;
		case R.id.player_prev_button:
			Log.f("Player Prev Button Click");
			showMenu(false);
			settingLayout(LAYOUT_TYPE_DEFAULT);
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_PREV_PLAY, DURATION_PLAY);
			break;
		case R.id.player_play_button:
			if(_ProgressMediaPlayer.isVideoPlaying())
			{
				Log.f("Player Pause Button Click");
				setPlayIconStatus(PLAYER_PAUSE);
				setPlayStatus(PLAYER_PAUSE);
				enableTimer(false);
			}
			else
			{
				Log.f("Player Play Button Click");
				_PlayButton.setImageResource(R.drawable.selector_player_pause_button);
				setPlayIconStatus(PLAYER_RESUME);
				setPlayStatus(PLAYER_RESUME);
				enableTimer(true);
			}
			break;
		case R.id.player_subtitle_button:
			if(mCaptionInformationResult.getCaptionDetailInformationList().size() > 0 )
			{
				isCaptionPlaying = !isCaptionPlaying;
				setCaptionIconStatus();
				if(isCaptionPlaying)
				{
					Log.f("Player Caption Layout Gone Click");
					startCaptionLayoutAnimation(true);
				}
				else
				{
					Log.f("Player Caption Layout Visible Click");
					startCaptionLayoutAnimation(false);
				}
			}
			else
			{
				showMenu(false);
				if(isMenuVisible())
				{
					Log.f("Player SubTitle Layout Gone Click");
					Message msg = Message.obtain();
					msg.what = MESSAGE_LAYOUT_SETTING;
					msg.arg1 = LAYOUT_TYPE_SUBTITLE_DIALOG;
					mMainHandler.sendMessageDelayed(msg, DURATION_VIEW_ANIMATION);
				}
				else
				{
					Log.f("Player SubTitle Layout Visible Click");
					settingLayout(LAYOUT_TYPE_SUBTITLE_DIALOG);
				}
			}
			break;	
		}
	}
	
	private OnTouchListener mLockControlListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				Message msg = Message.obtain();
				msg.what = MESSAGE_LOCK_MODE_READY;
				msg.obj = !isLockDisplay;
				mMainHandler.sendMessageDelayed(msg, DURATION_LOCK_MODE);
				
			}else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_OUTSIDE)
			{
				if(mMainHandler.hasMessages(MESSAGE_LOCK_MODE_READY))
				{
					mMainHandler.removeMessages(MESSAGE_LOCK_MODE_READY);
				}
				
			}
			return true;
		}
		
	};
	
	private OnTouchListener mMenuVisibleListener = new OnTouchListener()
	{

		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				if(isVideoLoadingComplete == false)
				{
					return false;
				}
				
				if(mCurrentLayoutType != LAYOUT_TYPE_DEFAULT && mCurrentLayoutType != LAYOUT_TYPE_PREVIEW_PLAY)
				{
					return false;
				}
				Log.i("mCurrentLayoutType : "+mCurrentLayoutType + ", menu Visible : "+isMenuVisible());
				
				if(mFadeAnimationController.isAnimationing(_TopViewLayout) || mFadeAnimationController.isAnimationing(_BottomViewLayout) || mFadeAnimationController.isAnimationing(_PlayButtonLayout))
				{
					return false;
				}
				
				if(isMenuVisible() == false)
				{
					showMenu(true);
				}
				else
				{
					showMenu(false);
				}
			}
			return true;

		}
		
	};
	
	private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener()
	{
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			_ProgressMediaPlayer.seekToMediaPlay(seekBar.getProgress());
			if(Feature.IS_CAPTION_USED)
			{
				try
				{
					int checkCaptionIndex = getCaptionCurrentIndex();
					if(checkCaptionIndex != -1)
					{
						mCurrentCaptionIndex = checkCaptionIndex;
					}
				}catch(IndexOutOfBoundsException e)
				{
					e.printStackTrace();
				}
			}
			enableTimer(true);
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			enableTimer(false);
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){}
	};
	
	private ProgressiveMediaListener mProgressPlayerListener = new ProgressiveMediaListener()
	{
		@Override
		public void onError(int messageType)
		{
			Log.f("messageType : "+messageType);
		}

		@Override
		public void onSetSeekBarMaxProgress(int progress)
		{
			Log.f("progress : "+progress);
			_ThumbSeekbar.setMax(progress);
		}

		@Override
		public void onPlayStart()
		{
			Log.f("");
			
			saveTotalPlaytimeDataBase();
			isVideoLoadingComplete = true;
			isPlayComplete = false;
			setVideoInformation();
			setPlayIconStatus(PLAYER_RESUME);
			showLoading(false);
			//플레이 타이머 보여줌
			enableTimer(true);
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_VIDEO_VISIBLE, DURATION_GONE_BACKGROUND);
		}

		@Override
		public void onPlayResume()
		{
			Log.f("");
			
			showLoading(false);
			if(Feature.IS_FREE_USER)
			{
				enableTimer(true);
			}
			else
			{
				mMainHandler.sendEmptyMessage(PLAYER_PAUSE);
				mMainHandler.sendEmptyMessageDelayed(MESSAGE_RESUME_SHOW_MENU, DUARAION_RESUME_MENU);
			}
		}

		@Override
		public void onPlayComplete()
		{
			Log.f("");
			isPlayComplete = true;
			showMenu(false);
			enableTimer(false);
			requestPlaySaveRecord();
			
			//선택한리스트를 전부 보여주거나 한편을 보여줬을때만 띠운다. 그렇지 않고는 다음편을 플레이한다.
			//화면끝낫을때 레이아웃 보여줌
			//동화한편, 동요한편, 시리즈일때 
			if(mWebInitInformation.isMultiVideoPlay() && mCurrentPlayPosition < mWebInitInformation.getPlayItemCount() -1)
			{
				mMainHandler.sendEmptyMessageDelayed(MESSAGE_NEXT_PLAY, DURATION_PLAY);
			}
			else
			{	
				if(isMenuVisible())
				{
					Message msg = Message.obtain();
					msg.what = MESSAGE_LAYOUT_SETTING;
					msg.arg1 = LAYOUT_TYPE_PLAY_END;
					mMainHandler.sendMessageDelayed(msg, DURATION_VIEW_ANIMATION);
				}
				else
				{
					settingLayout(LAYOUT_TYPE_PLAY_END);
				}
			}
			
			
		}

		@Override
		public void onSeekComplete(int progress)
		{
			Log.f("progress : "+progress);
			_ThumbSeekbar.setProgress(progress);
			_CurrentPlayTimeText.setText(CommonUtils.getInstance(PlayerMobileWebActivity.this).getMillisecondTime(_ProgressMediaPlayer.getCurrentPlayerDuration()));
		}

		@Override
		public void onDownloadStart()
		{
			Log.f("");
			_ThumbSeekbar.setSecondaryProgress(0);
		}

		@Override
		public void onDownloadProgress(int progress)
		{
			_ThumbSeekbar.setSecondaryProgress(progress);
		}

		@Override
		public void onDownloadComplete(int progress)
		{
			_ThumbSeekbar.setSecondaryProgress(progress);
		}

		@Override
		public void onFullDownloadComplete()
		{
			Log.f("Content ID : "+mCurrentPlayContentId);
			mPlayedContentDBHelper.updatePlayedContent(
					mCurrentPlayContentId, 
					PlayedContentDBHelper.KEY_DOWNLOAD_COMPLETE, 
					PlayedContentDBHelper.DOWNLOAD_COMPLETE);
		}

	};
	
	private AsyncListener mAsyncListener = new AsyncListener()
	{

		@Override
		public void onRunningStart(String code) {

		}

		@Override
		public void onRunningEnd(String code, Object mObject)
		{
			switch(code)
			{
				case Common.ASYNC_CODE_PLAY_SAVE_RECORD:
					if(((BaseResult)mObject).getResult().equals(BaseResult.RESULT_OK))
					{
						Log.f("send Play Study Record success");
					}
					else
					{
						Log.f("send Play Study Record fail");
					}
					mStudyTime = 0;
					break;
				case Common.ASYNC_CODE_INIT_VIDEO_INFORMATION:
					if(((BaseResult)mObject).getResult().equals(BaseResult.RESULT_OK) && ((BaseResult)mObject).getCode() == BaseResult.SUCCESS_CODE_OK)
					{
						isAuthContentPlayPossible = true;
						savePlayFileDataBase();
						mVideoInformationResult = ((VideoInformationResult)mObject);
						mCaptionInformationResult = mVideoInformationResult.getCaptionInformationResult();

						Log.i("Caption Use  : " + mVideoInformationResult.isCaptionDataUse());
						Log.i("Caption Size : " +mCaptionInformationResult.getCaptionDetailInformationList().size());

						mCurrentPlayUrl = mVideoInformationResult.getVideoUrl();

						if(Feature.IS_FREE_USER && mVideoInformationResult.isFreeViewAvailable() == false)
						{
							Feature.IS_FREE_USER = false;
						}


						if(Feature.IS_FREE_USER)
						{
							mFreeUserPreviewTime = Integer.valueOf(mVideoInformationResult.getFreeViewTime());
						}
						mMainHandler.sendEmptyMessage(MESSAGE_INIT_VIEW_SETTING);
						playProgressiveDownloadContent();
					}
					else
					{
						Toast.makeText(PlayerMobileWebActivity.this, ((BaseResult)mObject).getMessage(), Toast.LENGTH_LONG).show();
						mMainHandler.sendEmptyMessageDelayed(MESSAGE_FINISH, DURATION_VIEW_ANIMATION);
					}
					break;
			}
		}

		@Override
		public void onRunningCanceled(String code) {

		}

		@Override
		public void onRunningProgress(String code, Integer progress) {

		}

		@Override
		public void onRunningAdvanceInformation(String code, Object object) {

		}

		@Override
		public void onErrorListener(String code, String message) {

		}
	};
	


}
