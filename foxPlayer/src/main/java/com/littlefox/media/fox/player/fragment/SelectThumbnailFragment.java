package com.littlefox.media.fox.player.fragment;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.MainActivity.OnMainCommnuicateListener;
import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SelectThumbnailFragment extends Fragment
{
	@BindView(R.id.main_title_text)
	TextView _MovieTitleText;
	
	@BindView(R.id.main_day_text)
	TextView _MovieDateText;
	
	@BindView(R.id.thumbnail_image)
	ImageView _ThumbnailImage;
	
	@BindView(R.id.littlefox_eng_move_layout)
	ScalableLayout _IndicateEnglishLayout;
	
	@BindView(R.id.littlefox_ch_move_layout)
	ScalableLayout _IndicateChineseLayout;
	
	@BindView(R.id.button_eng_text)
	TextView _IndicateEnglishText;
	
	@BindView(R.id.button_ch_text)
	TextView _IndicateChineseText;
	
	private Context mContext;
	private OnMainCommnuicateListener mOnMainCommnuicateListener;

	private int mCurrentPlayType;
	private String mCurrentPlayTitle;
	private String mCurrentImageUrl;
	
	public static SelectThumbnailFragment getInstance()
	{
		return new SelectThumbnailFragment();
	}
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		mContext = context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}
	
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view;
		view = inflater.inflate(R.layout.fragment_play_select, container, false);
		ButterKnife.bind(this, view);
		init();
		initFont();
		return view;
	}
	
	private void init()
	{
		settingTodayDate();
		
		settingInformation();
		
		switch(mCurrentPlayType)
		{
		case Common.TYPE_ENGLISH:
			_IndicateEnglishLayout.setVisibility(View.GONE);
			break;
		case Common.TYPE_CHINESE:
			_IndicateChineseLayout.setVisibility(View.GONE);
			break;
		}
		
	}
	
	private void initFont()
	{
		_MovieTitleText.setTypeface(Font.getInstance(mContext).getRobotoBold());
		_MovieTitleText.setPaintFlags(_MovieTitleText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		
		_MovieDateText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
		_IndicateEnglishText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
		_IndicateChineseText.setTypeface(Font.getInstance(mContext).getRobotoMedium());
	}
	
	private void settingInformation()
	{
		_MovieTitleText.setText(mCurrentPlayTitle);
		Glide.with(mContext)
				.load(mCurrentImageUrl)
				.transition(withCrossFade())
				.into(_ThumbnailImage);

		
		_IndicateEnglishText.setText(mContext.getResources().getString(R.string.title_littlefox_eng));
		_IndicateChineseText.setText(mContext.getResources().getString(R.string.title_littlefox_ch));
	}
	
	private void settingTodayDate()
	{
		String[] monthName 	= mContext.getResources().getStringArray(R.array.name_month);
		String[] dayName	= mContext.getResources().getStringArray(R.array.name_day);
		String date = "";
		
		Log.i("Locale.getDefault().toString() : "+Locale.getDefault().toString());
		Calendar calendar = Calendar.getInstance();
		if(Locale.getDefault().toString().equals(Locale.KOREA.toString()))
		{
			date = calendar.get(Calendar.DATE)+"일";
		}
		else
		{
			date = String.valueOf(calendar.get(Calendar.DATE));
		}
		
		
		_MovieDateText.setText(monthName[calendar.get(Calendar.MONTH)]+" "+ date +" | "+dayName[calendar.get(Calendar.DAY_OF_WEEK + 1)]);
	}
	
	@OnClick({R.id.button_littlefox_eng_move, R.id.button_littlefox_ch_move, R.id.button_play_icon})
	public void onSelectClick(View view)
	{
		switch(view.getId())
		{
		case R.id.button_littlefox_eng_move:
		case R.id.button_littlefox_ch_move:
			mOnMainCommnuicateListener.movePlayTypeLayout();
			break;
		case R.id.button_play_icon:
			mOnMainCommnuicateListener.playVideo(mCurrentPlayType);
			break;
		}
	}

	public void settingType(int type, String title, String imageUrl)
	{
		mCurrentPlayType 	= type;
		mCurrentPlayTitle 	= title;
		mCurrentImageUrl	= imageUrl;
		
		
		
	}
	
	public void setMainCommunicateListener(OnMainCommnuicateListener onMainCommnuicateListener)
	{
		mOnMainCommnuicateListener = onMainCommnuicateListener;
	}
	
}
