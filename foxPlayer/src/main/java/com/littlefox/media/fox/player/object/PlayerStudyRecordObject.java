package com.littlefox.media.fox.player.object;

import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.object.base.BaseResult;

/**
 * 학습 정보를 서버로 전달하기 위한 오브젝트
 * @author 정재현
 *
 */
public class PlayerStudyRecordObject extends BaseResult
{
	/**
	 * 동화
	 */
	private final int RECORD_STORY 	= 1;
	
	/**
	 * 동요
	 */
	private final int RECORD_SONG 	= 3;
	
	/**
	 * 한편듣기
	 */
	private final int PLAY_SINGLE = 1;
	
	/**
	 * 여러편 이어서 플레이
	 */
	private final int PLAY_MULTI = 2;
	
	/**
	 * 동요 와 동화의 구분
	 */
	private int mRecordType = -1;
	
	/**
	 * 한편듣기 인지 이어듣기 인지의 구분
	 */
	private int mPlayType = -1;
	
	private String mContentId = "";
	
	private int mPlayTime = -1;
	
	public PlayerStudyRecordObject(int recordType , int playType, String contentId, int playTime)
	{
		if(recordType == Common.PLAY_TYPE_SONG)
		{
			mRecordType = RECORD_SONG;
		}
		else
		{
			mRecordType = RECORD_STORY;
		}
		
		if(playType == -1)
		{
			mPlayType = PLAY_MULTI;
		}
		else
		{
			mPlayType = PLAY_SINGLE;
		}
		
		mContentId = contentId;
		mPlayTime = playTime;
	}

	public int getRecordType()
	{
		return mRecordType;
	}

	public int getPlayType()
	{
		return mPlayType;
	}

	public String getContentId()
	{
		return mContentId;
	}

	public int getPlayTime()
	{
		return mPlayTime;
	}
	
	
}
