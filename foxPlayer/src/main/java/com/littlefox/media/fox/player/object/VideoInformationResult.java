package com.littlefox.media.fox.player.object;

import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.object.base.BaseResult;
import com.littlefox.media.fox.player.object.result.CaptionInformationResult;

public class VideoInformationResult extends BaseResult
{
	private String auth_result = "";
	private String video_url = "";
	private String pre_view_time = "";
	private String pre_view_flag = "";
	private String title = "";
	private String caption_use_yn = "";
	private CaptionInformationResult caption_data = new CaptionInformationResult();
	
	public String getAuthResult()
	{
		return auth_result;
	}
	
	/**
	 * 미디어를 사용가능 한지의 여부를 전달
	 * @return true : 사용 가능 , false : 사용 불가능
	 */
	public boolean isAuthResultPlayAvailable()
	{
		if(auth_result.equals(Common.AUTH_RESULT_AVAILABLE))
		{
			return true;
		}
		
		return false;
	}
	
	public String getVideoUrl()
	{
		return video_url;
	}
	
	public String getFreeViewTime()
	{
		return pre_view_time;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public CaptionInformationResult getCaptionInformationResult()
	{
		return caption_data;
	}
	
	public boolean isCaptionDataUse()
	{
		return caption_use_yn.equalsIgnoreCase(Common.CAPTION_USE) ? true : false;
	}
	
	/**
	 * 무료사용자가 유료컨텐츠를 볼때만 미리보기를 제공
	 * @return
	 */
	public boolean isFreeViewAvailable()
	{
		return pre_view_flag.equals("Y");
	}
}
