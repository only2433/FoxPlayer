package com.littlefox.media.fox.player.object.result;

import java.io.Serializable;
import java.util.ArrayList;

public class CaptionInformationResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<CaptionDetailInforamtion> caption_list = new ArrayList<CaptionDetailInforamtion>();
	
	public ArrayList<CaptionDetailInforamtion> getCaptionDetailInformationList()
	{
		if(caption_list == null)
		{
			caption_list = new ArrayList<CaptionDetailInforamtion>();
		}
		return caption_list;
	}
	
	public class CaptionDetailInforamtion implements Serializable
	{
		private static final long serialVersionUID = 1L;
		
		String start_time = "";
		String text = "";
		String end_time = "";
		
		public float getStartTime()
		{
			return Float.valueOf(start_time);
		}
		
		public float getEndTime()
		{
			return Float.valueOf(end_time);
		}
		
		public String getText()
		{
			return text;
		}
	}
}
