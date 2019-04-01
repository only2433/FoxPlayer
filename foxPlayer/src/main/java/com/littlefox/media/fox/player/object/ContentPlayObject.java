package com.littlefox.media.fox.player.object;

import java.util.ArrayList;

import com.littlefox.media.fox.player.object.base.PlayObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 플레이어에서 사용할 객체 클래스
 * @author 정재현
 *
 */
public class ContentPlayObject implements Parcelable
{
	/**
	 * 플레이 타입 종류 : 동요, 단편, 시리즈
	 */
	private int mPlayItemType = -1;
	
	private int mSelectPosition = -1;
	
	/**
	 * Feature 시리즈 인지의 여부
	 */
	private boolean isFeatureSeries = false;
	
	private ArrayList<PlayObject> mPlayObjectList = new ArrayList<PlayObject>();
	
	public ContentPlayObject(int type)
	{
		mPlayItemType = type;
		mPlayObjectList.clear();
		isFeatureSeries = false;
	}
	
	public ContentPlayObject(int type, ArrayList<PlayObject> playObjectList)
	{
		mPlayItemType 	= type;
		mPlayObjectList = playObjectList;
		isFeatureSeries = false;
	}
	
	public ContentPlayObject(Parcel in)
	{
		mPlayItemType = in.readInt();
		mSelectPosition = in.readInt();
		isFeatureSeries = in.readInt() == 1 ? true : false;
		mPlayObjectList = (ArrayList<PlayObject>)in.readSerializable();
	}
	
	public void setSelectPosition(int position)
	{
		mSelectPosition = position;
	}
	
	public void addPlayObject(PlayObject playObject)
	{
		mPlayObjectList.add(playObject);
	}
	
	public void setFeatureSeries(boolean isFeature)
	{
		isFeatureSeries = isFeature;
	}
	
	public boolean isFeatureSeries()
	{
		return isFeatureSeries;
	}
	
	public void setPlayObjectList(ArrayList<PlayObject> playObjectList)
	{
		mPlayObjectList = playObjectList;
	}
	
	public int getPlayItemType()
	{
		return mPlayItemType;
	}
	
	public int getSelectedPosition()
	{
		return mSelectPosition;
	}
	
	public ArrayList<PlayObject> getPlayObjectList()
	{
		return mPlayObjectList;
	}
	
	public PlayObject getPlayObject(int position)
	{
		return mPlayObjectList.get(position);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(mPlayItemType);
		dest.writeInt(mSelectPosition);
		dest.writeInt(isFeatureSeries == true ? 1 : 0);
		dest.writeSerializable(mPlayObjectList);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source)
		{
			// TODO Auto-generated method stub
			return new ContentPlayObject(source);
		}

		@Override
		public Object[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new ContentPlayObject[size];
		}
		
	};
}
