package com.littlefox.media.fox.player.async;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Settings.Secure;

import com.google.gson.Gson;
import com.littlefox.library.system.async.BaseAsync;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.VideoInformationResult;
import com.littlefox.media.fox.player.object.WebInitInformation;

import java.util.Locale;

public class InitVideoInformationAsync extends BaseAsync
{
	private int mCurrentPlayPosition = -1;
	public InitVideoInformationAsync(Context context)
	{
		super(context, Common.ASYNC_CODE_INIT_VIDEO_INFORMATION);

	}

	@Override
	public void setData(Object... object)
	{
		mCurrentPlayPosition = (int) object[0];
	}

	@Override
	protected Object doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			return null;
		}
		
		VideoInformationResult result = null;
		synchronized (mSync)
		{
			isRunning = true;
			try
			{
				WebInitInformation webInitInformation = (WebInitInformation) CommonUtils.getInstance(mContext).getPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, WebInitInformation.class);
				ContentValues list = new ContentValues();
				list.put("device_id", Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID));
				list.put("locale", Locale.getDefault().toString());
				list.put("user_id", webInitInformation.getUserID());
				list.put("service_id", webInitInformation.getServiceCode());
				list.put("network_type", NetworkUtil.getConnectivityStatus(mContext) == NetworkUtil.TYPE_WIFI ? "W" : "X");
				list.put("session_key1", webInitInformation.getClientKey());
				list.put("session_key2", webInitInformation.getServerKey());
				list.put("content_id", webInitInformation.getContentID(mCurrentPlayPosition));
				list.put("auth_type", webInitInformation.getContentType(mCurrentPlayPosition));
				list.put("user_type", webInitInformation.getUserType());

				String response = NetworkUtil.requestServerPair(mContext, Common.URI_AUTH_CONTENT_PLAY, list, NetworkUtil.POST_METHOD);
				result = new Gson().fromJson(response, VideoInformationResult.class);
			}catch(Exception e)
			{
				e.printStackTrace();
				mAsyncListener.onErrorListener(Common.ASYNC_CODE_INIT_VIDEO_INFORMATION, e.getMessage());
			}
		}
		return result;
	}
}
