package com.littlefox.media.fox.player.object;

import com.littlefox.logmonitor.Log;

/**
 * 플레이 정보를 기록하기 위해 만든 정보
 * Created by 정재현 on 2015-07-06.
 */
public class PlayedContentInformation
{

    private String mContentID           = "";
    /**
     * 플레이 될 때 마다 업데이트 시킨다. 그래서 특정기한이 지나거나 용량이 어느정도 되엇을 때 삭제한다.
     */
    private String mRecentPlayTime      = "";
    private String mFilePath            = "";
    private String mTotalPlayTime			= "";
    private boolean isDownloadComplete = false;


    public PlayedContentInformation(String contentID, String recentPlayTime, String filePath, String totalPlayTime, boolean isDownloadComplete)
    {
        mContentID             		= contentID;
        mRecentPlayTime         	= recentPlayTime;
        mFilePath               	= filePath;
        mTotalPlayTime				= totalPlayTime;
        this.isDownloadComplete = isDownloadComplete;
    }

    public String getContentID()
    {
        return mContentID;
    }

    public void setContentID(String mContentID)
    {
        this.mContentID = mContentID;
    }

    public String getRecentPlayTime()
    {
        return mRecentPlayTime;
    }

    public void setRecentPlayTime(String mRecentPlayTime)
    {
        this.mRecentPlayTime = mRecentPlayTime;
    }

    public String getFilePath()
    {
        return mFilePath;
    }

    public void setFilePath(String mFilePath)
    {
        this.mFilePath = mFilePath;
    }

	public String getTotalPlayTime()
	{
		return mTotalPlayTime;
	}

	public void setTotalPlayTime(String previewTime)
	{
		this.mTotalPlayTime = previewTime;
	}

	public boolean isDownloadComplete()
	{
		return isDownloadComplete;
	}

	public void setDownloadComplete(boolean isDownloadComplete)
	{
		this.isDownloadComplete = isDownloadComplete;
	}

	

}
