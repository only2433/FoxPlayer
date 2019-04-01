package com.littlefox.media.fox.player.object.result;

import com.littlefox.media.fox.player.object.base.BaseResult;

public class AuthContentResult extends BaseResult
{
	private String video_url = "";
	private CaptionInformationResult caption_data = null;
	
	public String getVideoUrl()
	{
		return video_url;
	}
	
	public CaptionInformationResult getCaptionInformationResult()
	{
		return caption_data;
	}
}
