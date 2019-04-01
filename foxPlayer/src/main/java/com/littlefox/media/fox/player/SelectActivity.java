package com.littlefox.media.fox.player;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.littlefox.library.view.text.SeparateTextView;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.base.BaseActivity;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.Feature;
import com.littlefox.media.fox.player.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity
{
	@BindView(R.id.root_layout)
	LinearLayout _RootLayout;
	
	@BindView(R.id.margin_layout)
	ScalableLayout _MarginLayout;
	
	@BindView(R.id.main_title_text)
	SeparateTextView _TitleText;
	
	@BindView(R.id.choice_title_text)
	SeparateTextView _ChoiceInformationText;
	
	@BindView(R.id.text_littlefox_eng)
	TextView _ButtonEnglishText;
	
	@BindView(R.id.text_littlefox_ch)
	TextView _ButtonChineseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        ButterKnife.bind(this);
        initFont();
    }

    
    private void initFont()
    {
    	_ChoiceInformationText.setPaintFlags(_ChoiceInformationText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
    	_ChoiceInformationText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_ChoiceInformationText.setSeparateText(getResources().getString(R.string.message_choice_title_1), getResources().getString(R.string.message_choice_title_2))
		.setSeparateTextSize(CommonUtils.getInstance(this).getPixel(90), CommonUtils.getInstance(this).getPixel(60))
		.setSeparateTextStyle(Font.getInstance(this).getRobotoBold(), Font.getInstance(this).getRobotoMedium())
		.showView();
    	
    	_TitleText.setPaintFlags(_TitleText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
    	_TitleText.setTypeface(Font.getInstance(this).getRobotoBold());
    	_TitleText.setSeparateText(getResources().getString(R.string.title_intro_littlefox), " " +getResources().getString(R.string.title_intro_player))
		.setSeparateColor(getResources().getColor(R.color.color_ffffff), getResources().getColor(R.color.color_fff305))
		.setSeparateTextSize(CommonUtils.getInstance(this).getPixel(52), CommonUtils.getInstance(this).getPixel(52))
		.showView();
    	
    	_ButtonEnglishText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	_ButtonChineseText.setTypeface(Font.getInstance(this).getRobotoMedium());
    	
    	if(Feature.IS_TABLET)
    	{
    		if(Feature.IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY)
        	{
        		Log.i("IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY");
        		_RootLayout.setBackground(getResources().getDrawable(R.drawable.main_bg_tablet1024));
        		_MarginLayout.setVisibility(View.VISIBLE);
        	}
    		else
    		{
    			_RootLayout.setBackground(getResources().getDrawable(R.drawable.main_bg_tablet));
    		}
    	}
    	
    	
    }
    
	@Override
	protected void onResume() 
	{
		super.onResume();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}
	
    private void startMainActivity(int type)
    {
    	Intent intent = new Intent(this, MainActivity.class);
    	intent.putExtra(Common.INTENT_SELECT_TYPE, type);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    	finish();
    }
	
	@OnClick({R.id.button_littlefox_eng, R.id.button_littlefox_ch})
	public void OnSelectClick(View view)
	{
		switch(view.getId())
		{
		case R.id.button_littlefox_eng:
			startMainActivity(Common.TYPE_ENGLISH);
			break;
		case R.id.button_littlefox_ch:
			startMainActivity(Common.TYPE_CHINESE);
			break;
		}
	}
}
