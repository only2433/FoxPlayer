package com.littlefox.media.fox.player.coroutine;

import android.content.Context;

import com.google.gson.Gson;
import com.littlefox.library.system.coroutine.BaseCoroutine;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.result.UpdateVersionResult;

import org.jetbrains.annotations.NotNull;

public class UpdateVersionCoroutine extends BaseCoroutine
{
    public UpdateVersionCoroutine(@NotNull Context context)
    {
        super(context, Common.ASYNC_CODE_UPDATE_VERSION);
    }

    @NotNull
    @Override
    public Object doInBackground()
    {
        if(isRunning())
        {
            return null;
        }
        UpdateVersionResult result = null;

        synchronized (mSync)
        {
            setRunning(true);

            try
            {
                String response = NetworkUtil.requestServerPair(mContext, Common.URI_UPDATE_VERSION, null, NetworkUtil.GET_METHOD);
                result = new Gson().fromJson(response, UpdateVersionResult.class);

            }catch(Exception e)
            {
                e.printStackTrace();
                getAsyncListener().onErrorListener(Common.ASYNC_CODE_UPDATE_VERSION, e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void setData(@NotNull Object... objects) {

    }
}
