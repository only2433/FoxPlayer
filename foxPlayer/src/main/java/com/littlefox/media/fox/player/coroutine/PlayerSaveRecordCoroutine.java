package com.littlefox.media.fox.player.coroutine;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.littlefox.library.system.coroutine.BaseCoroutine;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.common.NetworkUtil;
import com.littlefox.media.fox.player.object.WebInitInformation;
import com.littlefox.media.fox.player.object.base.BaseResult;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PlayerSaveRecordCoroutine extends BaseCoroutine
{
    private int mCurrentPosition = -1;
    private int mPlayTime = -1;
    public PlayerSaveRecordCoroutine(@NotNull Context context)
    {
        super(context, Common.ASYNC_CODE_PLAY_SAVE_RECORD);
    }

    @NotNull
    @Override
    public Object doInBackground()
    {
        if(isRunning())
        {
            return null;
        }
        BaseResult result = null;

        synchronized (mSync)
        {
            setRunning(true);

            WebInitInformation webInitInformation = (WebInitInformation) CommonUtils.getInstance(mContext).getPreferenceObject(Common.PARAMS_WEB_INIT_INFORMATION, WebInitInformation.class);
            ContentValues list = new ContentValues();
            list.put("locale", Locale.getDefault().toString());
            list.put("service_id", webInitInformation.getServiceCode());
            list.put("record_type", webInitInformation.getContentType(mCurrentPosition));
            list.put("play_type", webInitInformation.getPlayItemCount() > 1 ? Common.VIDEO_PLAY_MULTI : Common.VIDEO_PLAY_ONE);
            list.put("content_id", webInitInformation.getContentID(mCurrentPosition));
            list.put("play_time_sec", String.valueOf(mPlayTime));
            list.put("user_id", webInitInformation.getUserID());
            list.put("session_key1", webInitInformation.getClientKey());

            try
            {
                String response = NetworkUtil.requestServerPair(mContext, Common.URI_PLAY_SAVE_RECORD, list, NetworkUtil.POST_METHOD);
                result = new Gson().fromJson(response, BaseResult.class);
            }catch(Exception e)
            {
                e.printStackTrace();
                getAsyncListener().onErrorListener(Common.ASYNC_CODE_PLAY_SAVE_RECORD, e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void setData(@NotNull Object... objects)
    {
        mCurrentPosition = (int) objects[0];
        mPlayTime = (int) objects[1];
    }
}
