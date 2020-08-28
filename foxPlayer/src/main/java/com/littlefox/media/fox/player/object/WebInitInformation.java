package com.littlefox.media.fox.player.object;

public class WebInitInformation
{
	/**
	 * fc_id 들이 들어 있는 String 배열
	 */
	private String[] mContentIDList; 
	
	/**
	 * auth_type 들이 들어 있는 String 배열
	 */
	private String[] mContentTypeList;

	/**
	 * 영상 리스트 썸네일 배열
	 */
	private String[] mThumbNailList;

	/**
	 * 영상 타이틀 배열
	 */
	private String[] mTitleList;
	
	/**
	 * fu_id
	 */
	private String mUserID 	= "";
	
	private String mUserType 	= "";
	
	private String mClientKey 	= "";
	
	private String mServerKey 	= "";
	
	private String mServiceCode = "";
	
	private int mPlayItemCount = -1;
	
	public WebInitInformation(String[] contentIDList, String[] contentTypeList, String foxUserID,
							  String userType, String clientKey, String serverKey, String serviceCode,
							String[] thumbnailList, String[] titleList)
	{
		mContentIDList 		= contentIDList;
		mContentTypeList	= contentTypeList;
		mUserID				= foxUserID;
		mUserType			= userType;
		mClientKey			= clientKey;
		mServerKey			= serverKey;
		mServiceCode		= serviceCode;
		mThumbNailList		= thumbnailList;
		mTitleList			= titleList;
		mPlayItemCount		= mContentIDList.length;
	}

	public String getContentID(int position)
	{
		if(mContentIDList.length <= position)
		{
			return null;
		}
		
		return mContentIDList[position];
	}
	
	public String[] getContentIDList()
	{
		return mContentIDList;
	}
	
	public String getContentType(int position)
	{
		if(mContentTypeList.length <= position)
		{
			return null;
		}
		
		return mContentTypeList[position];
	}
	
	public void setContentType(int position, String type)
	{
		if(mContentTypeList.length <= position)
		{
			return;
		}
		
		mContentTypeList[position] = type;
	}

	public String[] getContentTypeList()
	{
		return mContentTypeList;
	}

	public String[] getThumbNailList()
	{
		return mThumbNailList;
	}

	public String[] getTitleList()
	{
		return mTitleList;
	}

	public String getUserID()
	{
		return mUserID;
	}

	public String getUserType()
	{
		return mUserType;
	}

	public String getClientKey()
	{
		return mClientKey;
	}

	public String getServerKey()
	{
		return mServerKey;
	}

	public String getServiceCode()
	{
		return mServiceCode;
	}
	
	public int getPlayItemCount()
	{
		return mPlayItemCount;
	}
	
	/**
	 * 여러편 플레이 인지 의 여부
	 * @return
	 */
	public boolean isMultiVideoPlay()
	{
		return mPlayItemCount > 1 ? true : false;
	}
}
