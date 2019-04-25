package com.littlefox.media.fox.player;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.littlefox.library.system.async.listener.AsyncListener;
import com.littlefox.library.view.dialog.ProgressWheel;
import com.littlefox.library.view.text.SeparateTextView;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.async.InitInformationAsync;
import com.littlefox.media.fox.player.async.UpdateVersionAsync;
import com.littlefox.media.fox.player.base.BaseActivity;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.Feature;
import com.littlefox.media.fox.player.common.Font;
import com.littlefox.media.fox.player.dialog.TempleteAlertDialog;
import com.littlefox.media.fox.player.dialog.listener.DialogListener;
import com.littlefox.media.fox.player.object.WebInitInformation;
import com.littlefox.media.fox.player.object.base.BaseResult;
import com.littlefox.media.fox.player.object.result.InitInformationResult;
import com.littlefox.media.fox.player.object.result.UpdateVersionResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity
{
	@BindView(R.id.intro_title_text)
	SeparateTextView _TitleText;
	
	@BindView(R.id.intro_loading_layout)
	ProgressWheel _ProgressView;
	
	@BindView(R.id.root_base_layout)
	RelativeLayout _BaseLayout;
	
	private static final int DIALOG_EVENT_UPDATE_NORMAL 	= 0x01;
	private static final int DIALOG_EVENT_UPDATE_CRITICAL 	= 0x02;
	
	private static final int PERMISSION_REQUEST_STORAGE = 1001;
	
	private static final int DURATION_INIT				= 1000;
	private static final int DURATION_COMPLETE			= 2000;
	
	private static final int URI_EXTRA_INDEX_USER_ID 	= 0;
	private static final int URI_EXTRA_INDEX_USER_TYPE 	= 1;
	private static final int URI_EXTRA_INDEX_CLIENT_KEY = 2;
	private static final int URI_EXTRA_INDEX_SERVER_KEY = 3;
	
	private static final int MESSAGE_INIT				= 0x00;
	private static final int MESSAGE_START_SELECT		= 0x01;
	
	private Uri mInformationURI;
	
	private Handler mMainHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case MESSAGE_INIT:
				requestInitInformation();
				break;
			case MESSAGE_START_SELECT:
				_ProgressView.setVisibility(View.GONE);
				startSelectActivity();
				break;
			}
		}
		
	};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Log.init(Common.LOG_FILE);
        
        mInformationURI = getIntent().getData();
        
        if(mInformationURI == null)
        {
        	_BaseLayout.setVisibility(View.VISIBLE);
        }
        else
        {
        	_BaseLayout.setVisibility(View.INVISIBLE);
        }
        
        
        if(Build.VERSION.SDK_INT >= Common.MALSHMALLOW)
		{
			checkPermission();
		}
		else
		{
			settingLogFile();
			init();
		}

    }
    
    private void init()
    {
    	
    	if(CommonUtils.getInstance(this).isTablet())
		{
			Feature.IS_TABLET = true;
		}
		else
		{
			Feature.IS_TABLET = false;
		}
    	
		CommonUtils.getInstance(this).getWindowInfo();
		CommonUtils.getInstance(this).showDeviceInfo();
		
		
		
		if(Feature.IS_TABLET)
		{
			if((float)CommonUtils.getInstance(this).getDisplayWidthPixel() / (float)CommonUtils.getInstance(this).getDisplayHeightPixel() < Common.MINIMUM_TABLET_DISPLAY_RADIO)
			{
				Log.i("4 : 3 비율 ");
				Feature.IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY = true;
			}
			else
			{
				Log.i("16 : 9 비율 ");
				Feature.IS_MINIMUM_SUPPORT_TABLET_RADIO_DISPLAY = false;
			}
		}
		
		initFont();
		
		requestUpdateVersionInformation();
    }
    
    private void startProcess()
    {
		if(mInformationURI == null)
		{
			mMainHandler.sendEmptyMessageDelayed(MESSAGE_INIT, DURATION_INIT);
		}
		else
		{
			Log.f("uri : "+ mInformationURI.toString());
			String[] contentIDList  = mInformationURI.getQueryParameter("param1").split("/");
			String[] authTypeList	= mInformationURI.getQueryParameter("param2").split("/");
			String[] extraList		= mInformationURI.getQueryParameter("param3").split("/");
			String serviceCode		= mInformationURI.getQueryParameter("param4");
			
			if(extraList[URI_EXTRA_INDEX_USER_TYPE].equals(String.valueOf(Common.USER_TYPE_FREE)))
			{
				Feature.IS_FREE_USER = true;
			}
			else
			{
				Feature.IS_FREE_USER = false;
			}
			
			WebInitInformation information = new WebInitInformation(
					contentIDList, 
					authTypeList, 
					extraList[URI_EXTRA_INDEX_USER_ID], 
					extraList[URI_EXTRA_INDEX_USER_TYPE],
					extraList[URI_EXTRA_INDEX_CLIENT_KEY],
					extraList[URI_EXTRA_INDEX_SERVER_KEY],
					serviceCode);
			
			CommonUtils.getInstance(this).setPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, information);	
			startMobilePlayerActivity();
		}
    }
    
    private void initFont()
    {
    	_TitleText.setPaintFlags(_TitleText.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
    	_TitleText.setTypeface(Font.getInstance(this).getRobotoBold());
    	_TitleText.setSeparateText(getResources().getString(R.string.title_intro_littlefox), " " +getResources().getString(R.string.title_intro_player))
		.setSeparateColor(getResources().getColor(R.color.color_ffffff), getResources().getColor(R.color.color_fff305))
		.setSeparateTextSize(CommonUtils.getInstance(this).getPixel(52), CommonUtils.getInstance(this).getPixel(52))
		.showView();
    }
    
	private void settingLogFile()
	{
		long logfileSize = Log.getLogfileSize();
		Log.f("Log file Size : " + logfileSize);
		if(logfileSize > Common.MAXIMUM_LOG_FILE_SIZE || logfileSize == 0L)
		{
			Log.initWithDeleteFile(Common.LOG_FILE);
		}
	}
    
	
	/**
	 * Permission check.
	 */
	private void checkPermission()
	{
		if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
				|| checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
		{
			// Should we show an explanation?
			if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{
				// Explain to the user why we need to write the permission.
				Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
			}

			requestPermissions(new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE,
					android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSION_REQUEST_STORAGE);

			// MY_PERMISSION_REQUEST_STORAGE is an
			// app-defined int constant

		} else
		{
			// 다음 부분은 항상 허용일 경우에 해당이 됩니다.
			// writeFile();
			settingLogFile();
			init();
			
		}
	}
	
	private void requestUpdateVersionInformation()
	{
		UpdateVersionAsync request = new UpdateVersionAsync(this);
		request.setAsyncListener(mRequestAsyncListener);
		request.execute();
	}
	
    private void requestInitInformation()
    {
    	_ProgressView.setVisibility(View.VISIBLE);
    	InitInformationAsync request = new InitInformationAsync(this);
		request.setAsyncListener(mRequestAsyncListener);
    	request.execute();
    }
    
    private void saveInitInformation(InitInformationResult result)
    {
    	CommonUtils.getInstance(this).setPreferenceObject(Common.PARAMS_APP_INFO, result);
    	mMainHandler.sendEmptyMessageDelayed(MESSAGE_START_SELECT, DURATION_COMPLETE);
    }
    
    private void checkUpdateVersion(UpdateVersionResult result)
    {
    	if(result.getVersion().equals(CommonUtils.getInstance(this).getPackageVersionName(Common.PACKAGE_NAME)) == false)
    	{
    		if(result.getUpdateStatus().equals(UpdateVersionResult.NORMAL_UPDATE))
    		{
    			showTempleteAlertDialog(DIALOG_EVENT_UPDATE_NORMAL, TempleteAlertDialog.DEFAULT_BUTTON_TYPE_2, getResources().getString(R.string.message_new_version_update));
    		}
    		else if(result.getUpdateStatus().equals(UpdateVersionResult.CRITICAL_UPDATE))
    		{
    			showTempleteAlertDialog(DIALOG_EVENT_UPDATE_CRITICAL, TempleteAlertDialog.DEFAULT_BUTTON_TYPE_1, getResources().getString(R.string.message_critical_version_update));
    		}
    	}
    	else
    	{
    		Log.f("App is Current Version");
    		startProcess();
    	}
    }
    
    private void showTempleteAlertDialog(int type, int buttonType, String message)
    {
    	TempleteAlertDialog dialog = new TempleteAlertDialog(this, message);
    	dialog.setDialogMessageSubType(type);
    	dialog.setCancelable(false);
    	dialog.setButtonText(buttonType);
    	dialog.setDialogListener(mDialogListener);
    	dialog.show();
    }
    
    private void startSelectActivity()
    {
    	Intent intent = new Intent(this, SelectActivity.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    	finish();
    }
    
    private void startMobilePlayerActivity()
    {
    	Intent intent = new Intent(this, PlayerMobileWebActivity.class);
    	startActivity(intent);
    	finish();
    }
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
	    switch (requestCode) {
	        case PERMISSION_REQUEST_STORAGE:
	            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
	                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

	            	settingLogFile();
	            	init();
	                // permission was granted, yay! do the
	                // calendar task you need to do.

	            } else {

	                Log.d("Permission always deny");
	                finish();
	                // permission denied, boo! Disable the
	                // functionality that depends on this permission.
	            }
	            break;
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
		
		mMainHandler.removeCallbacksAndMessages(null);
	}
	
	private DialogListener mDialogListener = new DialogListener()
	{
		@Override
		public void onItemClick(int messageButtonType, int messageType, Object sendObject)
		{
			Log.i("messageButtonType : "+messageButtonType+", messageType : "+messageType);
			switch(messageType)
			{
			case DIALOG_EVENT_UPDATE_NORMAL:
				if(messageButtonType == FIRST_BUTTON_CLICK)
				{
					Log.f("NORMAL App Update Cancel.");
					startProcess();
				}
				else if(messageButtonType == SECOND_BUTTON_CLICK)
				{
					Log.f("NORMAL App Update.");
					CommonUtils.getInstance(SplashActivity.this).startLinkMove(Common.APP_LINK);
					finish();
				}
				break;
			case DIALOG_EVENT_UPDATE_CRITICAL:
				Log.f("CRITICAL App Update.");
				CommonUtils.getInstance(SplashActivity.this).startLinkMove(Common.APP_LINK);
				finish();
				break;
			}
		}
		
		
	};
	
	private AsyncListener mRequestAsyncListener = new AsyncListener()
	{

		@Override
		public void onRunningStart(String code) {

		}

		@Override
		public void onRunningEnd(String code, Object mObject)
		{
			if(((BaseResult)mObject).getResult().equals(BaseResult.RESULT_OK))
			{
				switch(code)
				{
					case Common.ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST:
						saveInitInformation((InitInformationResult) mObject);
						break;
					case Common.ASYNC_CODE_UPDATE_VERSION:
						checkUpdateVersion((UpdateVersionResult) mObject);
						break;
				}
			}
			else
			{
				finish();
				Toast.makeText(SplashActivity.this,((BaseResult)mObject).getMessage() , Toast.LENGTH_LONG).show();
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
