package com.littlefox.media.fox.player.object.base;

import com.littlefox.logmonitor.Log;

public class BaseResult
{
	
	
	public static final int FAIL_CODE_UNSUPPORTED_ENCORDING_EXCEPTION 	= 100;
	public static final int FAIL_CODE_CLIENT_PROTOCOL_EXCEPTION 			= 101;
	public static final int FAIL_CODE_SOCKET_TIMEOUT_EXCEPTION			= 102;
	public static final int FAIL_CODE_FILE_NOT_FOUND_EXCEPTION				= 103;
	public static final int FAIL_CODE_IO_EXCEPTION									= 104;
	public static final int FAIL_CODE_NETWORK_NOT_CONNECT					= 105;
	
	public static final int FAIL_CODE_USER_AUTHENTICATION							= 900;
	public static final int FAIL_CODE_USER_AUTHENTICATION_NO_TAKEN		= 901;
	
	
	public static final int SUCCESS_CODE_OK											= 200;

	public static final String RESULT_OK = "ok";
	public static final String RESULT_FAIL = "fail";
	
	public int code = SUCCESS_CODE_OK;
	public String result 	= RESULT_OK;
	public String message = "";
	
	
	public int getCode()
	{
		return code;
	}
	
	public String getResult()
	{
		return result;
	}
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Access Token이 없거나 다른 사용자가 사용시 TRUE . 아닐경우 FALSE
	 * @return
	 */
	public boolean isAuthenticationBroken()
	{
		if(code == FAIL_CODE_USER_AUTHENTICATION || code == FAIL_CODE_USER_AUTHENTICATION_NO_TAKEN)
		{
			Log.f("Access Token 을 사용할 수 없다.");
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
