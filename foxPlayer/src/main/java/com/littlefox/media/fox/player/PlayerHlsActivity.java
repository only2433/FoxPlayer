package com.littlefox.media.fox.player;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.littlefox.library.view.controller.FadeAnimationController;
import com.littlefox.library.view.controller.FadeAnimationInformation;
import com.littlefox.library.view.layoutmanager.LinearLayoutScrollerManager;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.adapter.PlayerListAdapter;
import com.littlefox.media.fox.player.adapter.PlayerSpeedListAdapter;
import com.littlefox.media.fox.player.adapter.listener.PlayerEventListener;
import com.littlefox.media.fox.player.analytics.GoogleAnalyticsHelper;
import com.littlefox.media.fox.player.base.BaseActivity;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.Feature;
import com.littlefox.media.fox.player.common.FileUtils;
import com.littlefox.media.fox.player.common.Font;
import com.littlefox.media.fox.player.enumItem.PlayerStatus;
import com.littlefox.media.fox.player.object.InitInformationObject;
import com.littlefox.media.fox.player.object.result.CaptionInformationResult;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlayerHlsActivity extends BaseActivity
{
    @BindView(R.id._playerView)
    PlayerView _PlayerView;

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

    //@BindView(R.id.player_lock_button)
   // ImageView _LockButton;

    @BindView(R.id.player_play_button_layout)
    ScalableLayout _PlayButtonLayout;

    @BindView(R.id.player_prev_button)
    ImageView _PrevButton;

    @BindView(R.id.player_play_button)
    ImageView _PlayButton;

    @BindView(R.id.player_next_button)
    ImageView _NextButton;

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
    RelativeLayout _BasePlayOtherAppEndLayout;

    @BindView(R.id.other_app_close_button)
    ImageView _OtherAppCloseButton;

    @BindView(R.id.other_app_title_text)
    TextView _OtherAppTitleText;

    @BindView(R.id.other_app_littlefox_eng_text)
    TextView _OtherAppEnglishText;

    @BindView(R.id.other_app_littlefox_song_text)
    TextView _OtherAppSongText;

    @BindView(R.id.other_app_littlefox_storybook_text)
    TextView _OtherAppStorybookText;

    @BindView(R.id.other_app_littlefox_ch_text)
    TextView _OtherAppChineseText;

    @BindView(R.id.player_caption_layout)
    ScalableLayout _CaptionLayout;

    @BindView(R.id.player_caption_title)
    TextView _CaptionTitleText;

    @BindView(R.id._playerListBaseLayout)
    LinearLayout _PlayerListBaseLayout;

    @BindView(R.id._playerListView)
    RecyclerView _PlayerListView;

    @BindView(R.id._playerListButton)
    ImageView _PlayerListButton;

    @BindView(R.id._playerSpeedListBaseLayout)
    LinearLayout _PlayerSpeedListBaseLayout;

    @BindView(R.id._playerSpeedListView)
    RecyclerView _PlayerSpeedListView;

    @BindView(R.id._playerSpeedButton)
    ImageView _PlayerSpeedButton;

    @BindView(R.id._playerSpeedText)
    TextView _PlayerSpeedText;

    @BindView(R.id._playerOptionBackground)
    ImageView _PlayerOptionBackground;

    @BindView(R.id._playListTitleText)
    TextView _PlayListTitleText;

    @BindView(R.id._playSpeedListTitleText)
    TextView _PlaySpeedListTitleText;


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
            mMainHandler.sendEmptyMessage(MESSAGE_UPDATE_PAID_UI);
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
                case MESSAGE_SELECT_PLAY:
                    prepareVideo(VIDEO_REPLAY);
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
                case MESSAGE_RESUME_SHOW_MENU:
                    showMenu(true);
                    break;
                case MESSAGE_FINISH:
                    finish();
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
    private static final int VIDEO_REPLAY		= 1;

    private static final int MESSAGE_PROGRESS_UI 				= 0;
    private static final int MESSAGE_SELECT_PLAY                = 1;
    private static final int MESSAGE_LAYOUT_SETTING 			= 2;
    private static final int MESSAGE_VIDEO_VISIBLE				= 3;
    private static final int MESSAGE_LOCK_MODE_READY			= 4;
    private static final int MESSAGE_LOCK_MODE_SET				= 5;
    private static final int MESSAGE_UPDATE_PAID_UI 			= 6;
    private static final int MESSAGE_RESUME_SHOW_MENU			= 7;
    private static final int MESSAGE_FINISH						= 8;

    private static final int LAYOUT_TYPE_DEFAULT 				= 0;
    private static final int LAYOUT_TYPE_SUBTITLE_DIALOG		= 1;
    private static final int LAYOUT_TYPE_PLAY_EXECUTE_APP_END 	= 2;

    private static final int DURATION_VIEW_ANIMATION 			= 500;
    private static final int DURATION_VIEW_INIT					= 1500;

    private static final int PLAYER_RESUME = 0;
    private static final int PLAYER_PAUSE	= 1;

    private static final String CAPTION = "caption";
    private static final float[] PLAY_SPEED_LIST = { 0.7f, 0.85f, 1.0f, 1.15f, 1.3f };
    private static final int DEFAULT_SPEED_INDEX = 2;

    private InitInformationObject mInformationObject;
    private FadeAnimationController mFadeAnimationController;
    private boolean isPlayComplete = false;
    private boolean isVideoLoadingComplete = false;
    private Timer mStudyRecordTimer = null;
    private Timer mUiCurrentTimer = null;
    private String mCurrentPlayUrl = "";
    private int mCurrentLayoutType = LAYOUT_TYPE_DEFAULT;
    private Vibrator mVibrator;
    private String mCurrentPlayContentId = "";
    static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
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

    /**
     * 자막을 보고있는 중인지 아닌지를 체크
     */
    private boolean isCaptionPlaying = false;

    private GoogleAnalyticsHelper mGoogleAnalyticsHelper;
    private PlayerListAdapter mPlayerListAdapter;
    private PlayerSpeedListAdapter mPlayerSpeedListAdapter;
    private int mCurrentPlaySpeedIndex = 0;
    private int mCurrentPlayPosition = 0;
    private int mCurrentPlayDuration = 0;
    private SimpleExoPlayer mPlayer;
    private boolean isVideoPrepared = false;
    private PlayerStatus mCurrentPlayerStatus = PlayerStatus.STOP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(Feature.IS_TABLET && Feature.IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY)
        {
            Log.f("NOT SUPPORT DISPLAY");
            setContentView(R.layout.player_main_not_support_display_hls);
        }
        else
        {
            setContentView(R.layout.player_main_hls);
        }
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.f("");
        mInformationObject = getIntent().getParcelableExtra(Common.INTENT_PLAYER_PARAMS);
        mCaptionInformationResult = mInformationObject.getCaptionInformationResult();

        try
        {
            Log.f("data size : "+ mCaptionInformationResult.getCaptionDetailInformationList().size());
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        initFont();
        initText();
        init();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.f("");
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            if(mCurrentPlayerStatus == PlayerStatus.PAUSE)
            {
                Log.f("Build.VERSION.SDK_INT ERROR : "+Build.VERSION.SDK_INT);
                isVideoPrepared = false;
                mCurrentCaptionIndex = 0;
                _CaptionTitleText.setText("");
                startMovie();
            }
        }
        else
        {
            resumePlayer();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.f("");
       // isLockDisplay = false;
       // setLockModeUI();
        pausePlayer();
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

        enableTimer(false);
        mMainHandler.removeCallbacksAndMessages(null);
    }

    private void init()
    {
        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        //_LockButton.setOnTouchListener(mLockControlListener);

        Log.i("Feature.IS_TABLET : "+CommonUtils.getInstance(this).getPixel(45));

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

        _PlayerListBaseLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        _PlayerSpeedListBaseLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        _PlayerView.setOnTouchListener(mMenuVisibleListener);
        registerFadeControllerView();
        initCaptionLayout();
        initViewSetting();
        prepareVideo(VIDEO_INIT_PLAY);
        initPlayListView();
        initPlaySpeedListView();
    }


    private void initViewSetting()
    {
        showMenuWithoutAnimation(false);
        settingLayout(LAYOUT_TYPE_DEFAULT);
    }

    private void initFont()
    {
        _CurrentPlayTimeText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _RemainPlayTimeText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _SubTitleNormalText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _SubTitleUseText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _TopTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());

        _OtherAppEnglishText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _OtherAppSongText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _OtherAppStorybookText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _OtherAppChineseText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _CaptionTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _PlayListTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
        _PlaySpeedListTitleText.setTypeface(Font.getInstance(this).getRobotoMedium());
    }

    private void initText()
    {
        _SubTitleNormalText.setText(getResources().getString(R.string.button_subtitle_default));
        _SubTitleUseText.setText(getResources().getString(R.string.button_subtitle_have));
        _PlayListTitleText.setText(getResources().getString(R.string.title_play_list));
        _PlaySpeedListTitleText.setText(getResources().getString(R.string.title_playing_speed));
    }

    private void initPlayListView()
    {
        String[] thumbnail  = mInformationObject.getThumbnalUrl().split("");
        String[] title      = mInformationObject.getTitle().split("");

        mPlayerListAdapter = new PlayerListAdapter(this, 0, thumbnail, title);
        mPlayerListAdapter.setOnPlayEventListener(mPlayerEventListener);
        _PlayerListView.setLayoutManager(new LinearLayoutScrollerManager(this));
        _PlayerListView.setAdapter(mPlayerListAdapter);
        //forceScrollView(mCurrentPlayPosition);
    }

    private void initPlaySpeedListView()
    {
        Log.f("");
        mCurrentPlaySpeedIndex = (int) CommonUtils.getInstance(this).getSharedPreference(Common.PARAMS_PLAYER_SPEED_INDEX, Common.TYPE_PARAMS_INTEGER);
        if(mCurrentPlaySpeedIndex == -1)
        {
            mCurrentPlaySpeedIndex = DEFAULT_SPEED_INDEX;
        }
        mPlayerSpeedListAdapter = new PlayerSpeedListAdapter(this, mCurrentPlaySpeedIndex);
        mPlayerSpeedListAdapter.setPlayerEventListener(mPlayerEventListener);
        _PlayerSpeedListView.setLayoutManager(new LinearLayoutScrollerManager(this));
        _PlayerSpeedListView.setAdapter(mPlayerSpeedListAdapter);
    }

    private void setupPlayVideo()
    {
        if(mPlayer == null)
        {
            mPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext());
            _PlayerView.setPlayer(mPlayer);
        }

        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) { }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
            {
                Log.f("playWhenReady : "+playWhenReady+", playbackState : "+playbackState);
                Log.f("Max Duration : "+mPlayer.getDuration());
                switch (playbackState)
                {
                    case Player.STATE_IDLE:
                        break;
                    case Player.STATE_BUFFERING:
                        if(playWhenReady)
                        {
                            showLoading(true);
                            setPlayIconStatus(PLAYER_PAUSE);
                        }
                        break;
                    case Player.STATE_READY:
                        if(playWhenReady)
                        {
                            showLoading(false);
                            setPlayIconStatus(PLAYER_RESUME);
                        }
                        if(isVideoPrepared)
                        {
                            return;
                        }
                        isVideoPrepared = true;
                        setVideoPrepared();
                        break;
                    case Player.STATE_ENDED:
                        Log.f("Play Complete");
                        if(playWhenReady)
                        {
                            showLoading(false);
                        }
                        setVideoCompleted();
                        break;
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error)
            {
                Log.f("Play Error : "+ error.getMessage());
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }

            @Override
            public void onSeekProcessed()
            {
                Log.f("Max Duration : "+mPlayer.getDuration());
                Log.f("isLoading : "+mPlayer.isLoading()
                        +" , getPlayWhenReady : "+mPlayer.getPlayWhenReady()
                        +", getPlaybackState : "+mPlayer.getPlaybackState());
            }
        });
    }

    private void startMovie()
    {
        Log.f("");
        mCurrentPlayerStatus = PlayerStatus.PLAY;

        MediaSource source = buildMediaSource(Uri.parse(mCurrentPlayUrl));
        mPlayer.prepare(source, true, false);
        _PlayerView.requestFocus();
    }

    private void pausePlayer()
    {
        Log.f("status : "+mCurrentPlayerStatus);

        if(mCurrentPlayerStatus == PlayerStatus.COMPELTE)
        {
            return;
        }
        //isLockDisplay = false;
        // setLockModeUI();
        if(mPlayer != null && isPlaying())
        {
            mCurrentPlayDuration = (int) mPlayer.getCurrentPosition();
            mPlayer.setPlayWhenReady(false);
            setPlayIconStatus(PLAYER_PAUSE);
            enableTimer(false);
            mCurrentPlayerStatus = PlayerStatus.PAUSE;
        }

    }

    private void resumePlayer()
    {
        Log.f("status : " + mCurrentPlayerStatus);
        if(mCurrentPlayerStatus == PlayerStatus.PAUSE)
        {
            mPlayer.seekTo(mCurrentPlayDuration);
            mPlayer.setPlayWhenReady(true);
            enableTimer(true);
            setPlayIconStatus(PLAYER_RESUME);
            mCurrentPlayerStatus = PlayerStatus.PLAY;
        }
    }

    private boolean isPlaying()
    {
        Log.f("playWhenReady : "+ mPlayer.getPlayWhenReady()+", state : "+ mPlayer.getPlaybackState());
        return mPlayer.getPlayWhenReady() && mPlayer.getPlaybackState() == Player.STATE_READY;
    }

    private void setVideoPrepared()
    {
        Log.f("mCurrentPlayerStatus : "+mCurrentPlayerStatus);

        isVideoLoadingComplete = true;

        if (mCurrentPlayerStatus == PlayerStatus.COMPELTE)
        {
            return;
        }

        if(mCurrentPlayerStatus ==  PlayerStatus.PLAY)
        {
            setVideoInformation();
            setPlayIconStatus(PLAYER_RESUME);
        }
        //플레이 타이머 보여줌
        mPlayer.setPlayWhenReady(true);
        enableTimer(true);
        mMainHandler.sendEmptyMessageDelayed(MESSAGE_VIDEO_VISIBLE, DURATION_GONE_BACKGROUND);
    }

    private void setVideoCompleted()
    {
        mCurrentPlayerStatus = PlayerStatus.COMPELTE;
        enableTimer(false);
        showMenu(false);

        if(isMenuVisible())
        {
            Message msg = Message.obtain();
            msg.what = MESSAGE_LAYOUT_SETTING;
            msg.arg1 = LAYOUT_TYPE_PLAY_EXECUTE_APP_END;
            mMainHandler.sendMessageDelayed(msg, DURATION_VIEW_ANIMATION);
        }
        else
        {
            settingLayout(LAYOUT_TYPE_PLAY_EXECUTE_APP_END);
        }
    }




    private MediaSource buildMediaSource(Uri uri)
    {
        String userAgent = Util.getUserAgent(this, Common.PACKAGE_NAME);
        if(uri.getLastPathSegment().contains("mp4"))
        {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri);
        }
        else if(uri.getLastPathSegment().contains("m3u8"))
        {
            return new HlsMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri);
        }
        else
        {
            return new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent)).createMediaSource(uri);
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

    private void setVideoInformation()
    {
        _TopTitleText.setText(mInformationObject.getTitle());
        _CurrentPlayTimeText.setText(CommonUtils.getInstance(this).getMillisecondTime(0));
        _RemainPlayTimeText.setText(CommonUtils.getInstance(this).getMillisecondTime((int) mPlayer.getDuration()));
        _ThumbSeekbar.setMax((int) (mPlayer.getDuration() / SECOND));
    }

    private void initCaptionLayout()
    {
        if(mInformationObject.isCaptionDataUse())
        {
            setCaptionControlButtonVisible(true);
            setCaptionLayoutStatus(isCaptionPlaying);
            if(mCaptionInformationResult.getCaptionDetailInformationList().size() > 0)
            {
                setCaptionLayoutVisible(isCaptionPlaying);
            }
            else
            {
                setCaptionLayoutVisible(false);
            }
        }
        else
        {
            setCaptionControlButtonVisible(false);
        }


    }

    private void setCaptionLayoutStatus(boolean isVisible)
    {
        //TODO : 자막 상태에 따라 화면에 자막을 보이게 안보이게 하는 작업
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
        _BasePlayOtherAppEndLayout.clearAnimation();
        switch(type)
        {
            case LAYOUT_TYPE_DEFAULT:
                _BaseSubTitleLayout.setVisibility(View.GONE);
                _BasePlayOtherAppEndLayout.setVisibility(View.GONE);
                break;
            case LAYOUT_TYPE_SUBTITLE_DIALOG:
                _BaseSubTitleLayout.setVisibility(View.VISIBLE);
                _BasePlayOtherAppEndLayout.setVisibility(View.GONE);
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
            case LAYOUT_TYPE_PLAY_EXECUTE_APP_END:
                _BaseSubTitleLayout.setVisibility(View.GONE);
                _BasePlayOtherAppEndLayout.setVisibility(View.VISIBLE);
                _BasePlayOtherAppEndLayout.startAnimation(CommonUtils.getInstance(this).getTranslateYAnimation(DURATION_VIEW_ANIMATION, -CommonUtils.getInstance(this).getPixel(1080), 0, new LinearOutSlowInInterpolator()));
                _BasePlayOtherAppEndLayout.setOnTouchListener(new OnTouchListener()
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
        if(isCaptionPlaying)
        {
            mCurrentPlayContentId = mInformationObject.getContentId()+"_caption";
            mCurrentPlayUrl = mInformationObject.getPlayUrlCaption();
        }
        else
        {
            mCurrentPlayContentId = mInformationObject.getContentId();
            mCurrentPlayUrl = mInformationObject.getPlayUrl();
        }

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
            //_LockButton.setImageResource(R.drawable.player_icon_lock);
            _TopCaptionSettingButton.setVisibility(View.INVISIBLE);
            _TopCloseButton.setVisibility(View.INVISIBLE);
            _CurrentPlayTimeText.setVisibility(View.INVISIBLE);
            _ThumbSeekbar.setVisibility(View.INVISIBLE);
            _RemainPlayTimeText.setVisibility(View.INVISIBLE);
        }
        else
        {
           // _LockButton.setImageResource(R.drawable.player_icon_unlock);
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
        if((_TopViewLayout.getVisibility()  == View.VISIBLE || _BottomViewLayout.getVisibility() == View.VISIBLE || _PlayButtonLayout.getVisibility()== View.VISIBLE ))
        {
            return true;
        }
        else
        {
            return false;
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
        _PrevButton.setVisibility(View.GONE);
        _NextButton.setVisibility(View.GONE);


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



    private void prepareVideo(int playType)
    {
        isVideoPrepared = false;
        enableTimer(false);
        mStudyTime = 0;
        showMenu(false);
        showLoading(true);
        isVideoLoadingComplete = false;
        _BackgroundDiscoverImage.setVisibility(View.VISIBLE);


        settingPlayMenu();

        _ThumbSeekbar.setThumbOffset(CommonUtils.getInstance(this).getPixel(0));
        _ThumbSeekbar.setProgress(0);
        _ThumbSeekbar.setSecondaryProgress(0);


        requestCurrentPlayVideoUrlInformation();

        Log.f("Current Play Title : "+ mInformationObject.getTitle());
        //Log.f("Current Play Url : "+ mContentPlayObject.getPlayObjectList().get(mCurrentPlayPosition).getPlayUrl());
        //Log.f("Current Play Url ~~ : "+ mCurrentPlayUrl);
        //
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


    private long getMillisecond(int second)
    {
        return second * SECOND;
    }


    private void updateUI()
    {
        _ThumbSeekbar.post(new Runnable(){

            @Override
            public void run()
            {
                try {
                    _ThumbSeekbar.setProgress((int) (mPlayer.getCurrentPosition() / SECOND));
                } catch (Exception e) {
                }

                _CurrentPlayTimeText.setText(CommonUtils.getInstance(PlayerHlsActivity.this).getMillisecondTime((int) mPlayer.getCurrentPosition()));

                if (Feature.IS_CAPTION_USED) {
                    if (isTimeForCaption() == true) {
                        _CaptionTitleText.setText(Html.fromHtml(mCaptionInformationResult.getCaptionDetailInformationList().get(mCurrentCaptionIndex).getText()));
                        mCurrentCaptionIndex++;
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
            if( visibleTime <= (float) mPlayer.getCurrentPosition())
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
        if(visibleTime > (float) mPlayer.getCurrentPosition())
        {
            return 0;
        }
        if(mCurrentCaptionIndex < mCaptionInformationResult.getCaptionDetailInformationList().size())
        {
            for(int i = 0; i < mCaptionInformationResult.getCaptionDetailInformationList().size(); i++)
            {
                visibleTime = mCaptionInformationResult.getCaptionDetailInformationList().get(i).getStartTime();;
                if( visibleTime >= (float) mPlayer.getCurrentPosition())
                {
                    return i;
                }
            }
        }


        return -1;
    }

    @OnClick({R.id.subtitle_normal_layout, R.id.subtitle_use_layout,R.id.subtitle_close_button})
    public void onDisplayButtonClick(View view)
    {
        if(mMainHandler.hasMessages(MESSAGE_SELECT_PLAY))
        {
            return;
        }

        switch(view.getId())
        {
            case R.id.subtitle_close_button:
                Log.f("Player Subtitle Page Close Button Click");
                initViewSetting();
                break;
            case R.id.subtitle_normal_layout:
                Log.f("Player Subtitle Page Not Use Button Click");
                isCaptionPlaying = false;
                initViewSetting();
                setCaptionLayoutStatus(isCaptionPlaying);
                FileUtils.deleteAllFileInPath(Common.PATH_MP4_SAVE);
                mMainHandler.sendEmptyMessageDelayed(MESSAGE_SELECT_PLAY, DURATION_PLAY);
                break;
            case R.id.subtitle_use_layout:
                Log.f("Player Subtitle Page Use Button Click");
                isCaptionPlaying = true;
                initViewSetting();
                setCaptionLayoutStatus(isCaptionPlaying);
                FileUtils.deleteAllFileInPath(Common.PATH_MP4_SAVE);
                mMainHandler.sendEmptyMessageDelayed(MESSAGE_SELECT_PLAY, DURATION_PLAY);
                break;
        }
    }

    @OnClick({R.id.player_close_button, R.id.player_next_button, R.id.player_play_button, R.id.player_prev_button, R.id.player_subtitle_button,
            R.id.other_app_close_button, R.id.button_other_app_littlefox_ch, R.id.button_other_app_littlefox_eng, R.id.button_other_app_littlefox_song, R.id.button_other_app_rocket_girl})
    public void onPlayerButtonClick(View view)
    {
        switch(view.getId())
        {
            case R.id.player_close_button:
            case R.id.other_app_close_button:
                Log.f("Player Main Close Button Click");
                finish();
                break;
            case R.id.player_play_button:
                if(isPlaying())
                {
                    Log.f("Player Pause Button Click");
                    setPlayIconStatus(PLAYER_PAUSE);
                    mPlayer.setPlayWhenReady(false);
                    enableTimer(false);
                }
                else
                {
                    Log.f("Player Play Button Click");
                    _PlayButton.setImageResource(R.drawable.selector_player_pause_button);
                    setPlayIconStatus(PLAYER_RESUME);
                    mPlayer.setPlayWhenReady(true);
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
            case R.id.button_other_app_littlefox_ch:
                mGoogleAnalyticsHelper.getInstance(PlayerHlsActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_CHINESE);
                CommonUtils.getInstance(PlayerHlsActivity.this).startLinkMove(Common.URL_LITTLEFOX_CHINESE);
                break;
            case R.id.button_other_app_littlefox_eng:
                mGoogleAnalyticsHelper.getInstance(PlayerHlsActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_ENGLISH);
                CommonUtils.getInstance(PlayerHlsActivity.this).startLinkMove(Common.URL_LITTLEFOX_ENGLISH);
                break;
            case R.id.button_other_app_littlefox_song:
                mGoogleAnalyticsHelper.getInstance(PlayerHlsActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_SONG);
                CommonUtils.getInstance(PlayerHlsActivity.this).startLinkMove(Common.URL_LITTLEFOX_SONG);
                break;
            case R.id.button_other_app_rocket_girl:
                mGoogleAnalyticsHelper.getInstance(PlayerHlsActivity.this).sendCurrentEvent(Common.ANALYTICS_CATEGORY_DEFAULT, Common.ANALYTICS_ACTION_OTHER_APP, Common.ANALYTICS_LABEL_STORYBOOK_ROCKET_GIRL);
                CommonUtils.getInstance(PlayerHlsActivity.this).startLinkMove(Common.URL_LITTLEFOX_ROCKET_GIRL);
                break;
        }
    }

    private PlayerEventListener mPlayerEventListener = new PlayerEventListener()
    {
        @Override
        public void onClickPlayItem(int index)
        {
           /* Log.f("index : "+index);
            mCurrentPlayPosition = index;
            enablePlayListAnimation(false);
            settingLayout(LAYOUT_TYPE_DEFAULT);
            mMainHandler.sendEmptyMessageDelayed(MESSAGE_SELECT_PLAY, DURATION_PLAY);*/
        }

        @Override
        public void onClickSpeedIndex(int index)
        {
            /*Log.f("index : "+index);
            CommonUtils.getInstance(PlayerHlsActivity.this).setSharedPreference(Common.PARAMS_PLAYER_SPEED_INDEX, index);
            mCurrentPlaySpeedIndex = index;
            settingVideoSpeed();
            enablePlaySpeedListAnimation(false);
            enableBackgroudAnimation(false);*/
        }
    };

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

                if(mCurrentLayoutType != LAYOUT_TYPE_DEFAULT)
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
            mPlayer.seekTo(seekBar.getProgress() * SECOND);
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

    /*private ProgressiveMediaListener mProgressPlayerListener = new ProgressiveMediaListener()
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


            if(isMenuVisible())
            {
                Message msg = Message.obtain();
                msg.what = MESSAGE_LAYOUT_SETTING;
                msg.arg1 = LAYOUT_TYPE_PLAY_EXECUTE_APP_END;
                mMainHandler.sendMessageDelayed(msg, DURATION_VIEW_ANIMATION);
            }
            else
            {
                settingLayout(LAYOUT_TYPE_PLAY_EXECUTE_APP_END);
            }

        }

        @Override
        public void onSeekComplete(int progress)
        {
            Log.f("progress : "+progress);
            _ThumbSeekbar.setProgress(progress);
            _CurrentPlayTimeText.setText(CommonUtils.getInstance(PlayerHlsActivity.this).getMillisecondTime(_ProgressMediaPlayer.getCurrentPlayerDuration()));
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
        }

    };*/

}
