package com.littlefox.media.fox.player.coroutine;

import android.content.ContentValues;
import android.content.Context;
import android.provider.Settings;

import com.google.gson.Gson;
import com.littlefox.library.system.coroutine.BaseCoroutine;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.VideoInformationResult;
import com.littlefox.media.fox.player.object.WebInitInformation;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class InitVideoInformationCoroutine extends BaseCoroutine
{
    private int mCurrentPlayPosition = -1;
    public InitVideoInformationCoroutine(@NotNull Context context)
    {
        super(context, Common.ASYNC_CODE_INIT_VIDEO_INFORMATION);
    }

    @NotNull
    @Override
    public Object doInBackground()
    {
        if(isRunning())
        {
            return null;
        }

        VideoInformationResult result = null;
        synchronized (mSync)
        {
            setRunning(true);
            try
            {
                WebInitInformation webInitInformation = (WebInitInformation) CommonUtils.getInstance(mContext).getPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, WebInitInformation.class);
                ContentValues list = new ContentValues();
                list.put("device_id", Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID));
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
                getAsyncListener().onErrorListener(Common.ASYNC_CODE_INIT_VIDEO_INFORMATION, e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void setData(@NotNull Object... objects)
    {
        mCurrentPlayPosition = (int) objects[0];
    }
}
