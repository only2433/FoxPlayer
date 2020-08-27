package com.littlefox.media.fox.player.coroutine;

import android.content.Context;

import com.google.gson.Gson;
import com.littlefox.library.system.coroutine.BaseCoroutine;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.result.InitInformationResult;

import org.jetbrains.annotations.NotNull;

public class InitInformationCoroutine extends BaseCoroutine
{
    public InitInformationCoroutine(@NotNull Context context)
    {
        super(context, Common.ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST);
    }

    @NotNull
    @Override
    public Object doInBackground()
    {
        if (isRunning())
        {
            return null;
        }
        InitInformationResult result = null;

        synchronized (mSync)
        {
            setRunning(true);

            try
            {
                String response = NetworkUtil.requestServerPair(mContext, Common.URI_GET_DAILY_CONTENTS, null, NetworkUtil.GET_METHOD);
                result = new Gson().fromJson(response, InitInformationResult.class);
            }catch(Exception e)
            {
                e.printStackTrace();
                getAsyncListener().onErrorListener(Common.ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST, e.getMessage());
            }
        }

        return result;
    }

    @Override
    public void setData(@NotNull Object... objects) { }
}
