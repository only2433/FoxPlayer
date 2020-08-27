package com.littlefox.media.fox.player.base;


import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.littlefox.logmonitor.ExceptionCheckHandler;
import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;

public class BaseActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionCheckHandler(this));
		
    	if(Build.VERSION.SDK_INT >= Common.LOLLIPOP)
		{
			CommonUtils.getInstance(this).setStatusBar(getResources().getColor(R.color.color_3e95c1));
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
	protected void onDestroy()
	{
		super.onDestroy();

	}

	@Override
	public void finish()
	{
		super.finish();
	}



}
