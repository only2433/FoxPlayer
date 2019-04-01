package com.littlefox.media.fox.player.async;

import android.content.Context;

import com.google.gson.Gson;
import com.littlefox.library.system.async.BaseAsync;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.result.UpdateVersionResult;

public class UpdateVersionAsync extends BaseAsync
{

	public UpdateVersionAsync(Context context)
	{
		super(context, Common.ASYNC_CODE_UPDATE_VERSION);

	}

	@Override
	protected Object doInBackground(Void... params)
	{
		if(isRunning == true)
		{
			return null;
		}
		UpdateVersionResult result = null;
		
		synchronized (mSync)
		{
			isRunning = true;
			
			try
			{
				String response = NetworkUtil.requestServerPair(mContext, Common.URI_UPDATE_VERSION, null, NetworkUtil.GET_METHOD);
				result = new Gson().fromJson(response, UpdateVersionResult.class);
						
			}catch(Exception e)
			{
				e.printStackTrace();
				mAsyncListener.onErrorListener(Common.ASYNC_CODE_UPDATE_VERSION, e.getMessage());
			}
		}
		return result;
	}

	@Override
	public void setData(Object... object) {

	}
}
