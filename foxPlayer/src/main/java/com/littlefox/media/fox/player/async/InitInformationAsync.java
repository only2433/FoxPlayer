package com.littlefox.media.fox.player.async;

import android.content.Context;

import com.google.gson.Gson;
import com.littlefox.library.system.async.BaseAsync;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.result.InitInformationResult;

public class InitInformationAsync extends BaseAsync
{
	public InitInformationAsync(Context context)
	{
		super(context, Common.ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST);
	}

	@Override
	protected Object doInBackground(Void... params)
	{
		if (isRunning == true)
		{
			return null;
		}
		InitInformationResult result = null;

		synchronized (mSync)
		{
			isRunning = true;
			
			try
			{
				String response = NetworkUtil.requestServerPair(mContext, Common.URI_GET_DAILY_CONTENTS, null, NetworkUtil.GET_METHOD);
				result = new Gson().fromJson(response, InitInformationResult.class);
			}catch(Exception e)
			{
				e.printStackTrace();
				mAsyncListener.onErrorListener(Common.ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST, e.getMessage());
			}
		}
		
		return result;
	}

	@Override
	public void setData(Object... object)
	{

	}
}
