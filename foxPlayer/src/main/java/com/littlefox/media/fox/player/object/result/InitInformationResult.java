package com.littlefox.media.fox.player.object.result;

import java.util.ArrayList;

import com.littlefox.media.fox.player.object.InitInformationObject;
import com.littlefox.media.fox.player.object.base.BaseResult;

public class InitInformationResult extends BaseResult
{
	private InitInformationObject CN = null;
	private InitInformationObject EN = null;
	
	public InitInformationObject getChineseInformation()
	{
		return CN;
	}
	
	public InitInformationObject getEnglishInformation()
	{
		return EN;
	}
	
}
