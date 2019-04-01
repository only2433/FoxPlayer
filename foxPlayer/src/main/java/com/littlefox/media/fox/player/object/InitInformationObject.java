package com.littlefox.media.fox.player.object;

import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.object.base.PlayObject;
import com.littlefox.media.fox.player.object.result.CaptionInformationResult;

import android.os.Parcel;
import android.os.Parcelable;

public class InitInformationObject  extends PlayObject implements Parcelable
{

	private static final long serialVersionUID = 1L;
	private String cont_type 		= "";
	private String play_url 		= "";
	private String play_url_caption = "";
	private String caption_use_yn = "";
	private CaptionInformationResult caption_data = new CaptionInformationResult();
	
	
	public InitInformationObject(Parcel in)
	{
		cont_type 			= in.readString();
		play_url 			= in.readString();
		play_url_caption 	= in.readString();
		fc_id 				= in.readString();
		image_url 			= in.readString();
		cont_name 			= in.readString();
		play_time 			= in.readString();
		caption_use_yn		= in.readString();
		caption_data		= (CaptionInformationResult) in.readSerializable();
	}
	
	public String getContentType()
	{
		return cont_type;
	}
	
	public String getPlayUrl()
	{
		return play_url;
	}
	
	public String getPlayUrlCaption()
	{
		return play_url_caption;
	}
	
	public CaptionInformationResult getCaptionInformationResult()
	{
		if(caption_data == null)
		{
			caption_data = new CaptionInformationResult();
		}
		return caption_data;
	}
	
	public boolean isCaptionDataUse()
	{
		return caption_use_yn.equalsIgnoreCase(Common.CAPTION_USE) ? true : false;
	}

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(cont_type);
		dest.writeString(play_url);
		dest.writeString(play_url_caption);
		dest.writeString(fc_id);
		dest.writeString(image_url);
		dest.writeString(cont_name);
		dest.writeString(play_time);
		dest.writeString(caption_use_yn);
		dest.writeSerializable(caption_data);
	}
	
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source)
		{
			// TODO Auto-generated method stub
			return new InitInformationObject(source);
		}

		@Override
		public Object[] newArray(int size)
		{
			// TODO Auto-generated method stub
			return new InitInformationObject[size];
		}
		
	};
	
}
