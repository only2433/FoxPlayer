package com.littlefox.media.fox.player.object.result;

import com.littlefox.media.fox.player.object.base.BaseResult;

public class UpdateVersionResult extends BaseResult
{
	public static final String NORMAL_UPDATE 	= "U";
	public static final String CRITICAL_UPDATE 	= "C";
	
	public UpdateVersionDetailObject app_info;
	
	
	public String getVersion()
	{
		if(app_info != null)
		{
			return app_info.version;
		}
		
		return "";
	}
	
	public String getUpdateStatus()
	{
		if(app_info != null)
		{
			return app_info.update_status;
		}
		
		return "";
	}
	
	public class UpdateVersionDetailObject
	{
		private String version = "";
		private String update_status = "";
	}
}
