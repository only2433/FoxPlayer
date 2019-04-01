package com.littlefox.media.fox.player.common;

import java.io.File;

import android.os.Environment;

/**
 * Created by 정재현 on 2015-07-07.
 */
public class Common
{
    public static final String PACKAGE_NAME             = "com.littlefox.media.fox.player";
    
	public static final String FILE_LOG_PATH 			= Environment.getExternalStorageDirectory() + File.separator + "foxPlayer"+ File.separator;
    public static final String PATH_APP_ROOT			= "/data/data/" + PACKAGE_NAME + "/files/";
    public static final String PATH_MP4_SAVE			= PATH_APP_ROOT +"mp4/";
    public static final String PATH_QUIZ_INFO			= PATH_APP_ROOT +"quiz/";
    public static final String LOG_FILE					= "fox_player_log.txt";

    public static final String BASE_PUBLIC_URI										= "https://app.littlefox.com/api_player_v2/";
    public static final String BASE_SERVER_DEVELOP_URI 								= "https://app.smile45.littlefox.com/api_player_v2/";
    
    public static final String BASE_URI 											= BASE_PUBLIC_URI;

    public static final String URI_GET_INIT_VIDEO_INFORMATION						= BASE_URI + "init-app";
    public static final String URI_GET_DAILY_CONTENTS								= BASE_URI + "get_daily_contents";
    public static final String URI_AUTH_CONTENT_PLAY								= BASE_URI + "auth-content-play";
    public static final String URI_PLAY_SAVE_RECORD									= BASE_URI + "save-study-record";
    public static final String URI_CAPTION_TEST										= BASE_URI + "caption_test/C0006012";
    public static final String URI_UPDATE_VERSION									= BASE_URI + "app-info";
    
	/** 개발자 이메일 */
	public static final String DEVELOPER_EMAIL 		= "app.support@littlefox.com";
	public static final String DEVELOPER_EMAIL_KR 	= "app.support@littlefox.co.kr";
    
    public static final String URL_LITTLEFOX_CHANNEL 		= "https://www.youtube.com/channel/UCXRPWm72nrs5sK7fNTSXZrw";
    public static final String URL_LITTLEFOX_ROCKET_GIRL 	= "https://play.google.com/store/apps/details?id=com.littlefox.storybook.rocketgirl";
    public static final String URL_LITTLEFOX_ENGLISH 		= "https://play.google.com/store/apps/details?id=net.littlefox.lf_app_fragment";
    public static final String URL_LITTLEFOX_CHINESE 		= "https://play.google.com/store/apps/details?id=com.littlefox.chinese.edu";
    public static final String URL_LITTLEFOX_SONG			= "https://play.google.com/store/apps/details?id=com.littlefox.song";
    

    public static final int ICE_CREAM_SANDWICH 				= 14;
    public static final int JELLYBEAN_CODE 					= 16;
    public static final int JELLYBEAN_CODE_4_3 				= 18;
    public static final int KITKAT							= 19;
    public static final int LOLLIPOP						= 21;
    public static final int MALSHMALLOW						= 23;
    
    public static final int TYPE_ENGLISH = 0;
    public static final int TYPE_CHINESE = 1;
    
    /**
     * 1024 x 768 사이즈 태블릿을 대응하기위한 용도 
     */
    public static final float MINIMUM_TABLET_DISPLAY_RADIO = 1.4f;
    
    /**
     * 저장공간 확인 :  앱을 사용하기 위한 최저치를 나타낸다. 250MB. 해당될때 동영상을 비디오가 다운될만큼 용량을 확보한다.
     */
    public static final int MIN_PLAYED_STORAGE_SIZE = 250;
    
    /**
     * 앱을 사용하기위한 사이즈. 동영상을 받기위해 1GB를 유지한다. 
     */
    public static final int VIDEO_DOWNLOAD_STORAGE_SIZE = 1000;

    public static final int TYPE_PARAMS_BOOLEAN 		= 0;
    public static final int TYPE_PARAMS_INTEGER 		= 1;
    public static final int TYPE_PARAMS_STRING			= 2;

    public static final String PACKAGE_KAKAO_TALK 	= "com.kakao.talk";
    public static final String PACKAGE_KAKAO_STORY 	= "com.kakao.story";
    
    public static final String INTENT_PLAYER_PARAMS 					= "player_object_params";
    public static final String INTENT_SELECT_TYPE						= "select_type";
    
    
    public static final String PARAMS_DISPLAY_METRICS					= "display_metrics";
    public static final String PARAMS_APP_USER_PK						= "app_user_pk";
    public static final String PARAMS_GCM_REGISTERATION_ID 				= "gcm_registeration_id";
    public static final String PARAMS_REGISTER_APP_VERSION 				= "current_app_version";
    public static final String PARAMS_APP_INFO							= "app_info";
    public static final String PARAMS_ACCESS_TOKEN						= "access_token";
    public static final String PARAMS_IS_ENG_LANGUAGE					= "language";
    
	/** 앱 별점 주기 링크 */
	public static final String APP_LINK = "https://play.google.com/store/apps/details?id=com.littlefox.media.fox.player";
	
	/**
	 * 저장 할 로그 파일 사이즈
	 */
	public static final long MAXIMUM_LOG_FILE_SIZE = 1024 * 1024 * 2;
	
    /**
     * 로그를 저장하는 날짜 : 하루마다 삭제 생성한다.
     */
    public static final String LOG_FILE_SAVE_DATE = "";
    
    public static final String PARAMS_WEB_INIT_INFORMATION = "web_init_information";
   
    public static  String HTTP_HEADER_DEVICE_PHONE 			= "Phone";
    public static  String HTTP_HEADER_DEVICE_TABLET 		= "Tablet";
    public static  String HTTP_HEADER 						= "LF_APP_PLAYER_Android";
    
    public static final String HTTP_HEADER_ANDROID	= "Android";
    
    public static final String ASYNC_CODE_GCM_REGISTER 								= "async_gcm_register";
    public static final String ASYNC_CODE_INIT_VIDEO_INFORMATION					= "async_init_video_information";
    public static final String ASYNC_CODE_GET_DAILY_CONTENTS_REQUEST 				= "async_get_daily_contents";
    public static final String ASYNC_CODE_SAVE_PAYMENT								= "save_payment";
    public static final String ASYNC_CODE_AUTH_CONTENT_PLAY							= "auth_content_play";
    public static final String ASYNC_CODE_PLAY_SAVE_RECORD							= "play_save_record";
    public static final String ASYNC_CODE_SUBTITLE_DOWNLOAD							= "subtitle_download";
    public static final String ASYNC_CODE_UPDATE_VERSION							= "update_version";

    public static final int REQUEST_CONTENT_TYPE_MOVIE 						= 1;
    public static final int REQUEST_CONTENT_TYPE_MOVIE_WITH_CAPTION 		= 2;
    public static final int REQUEST_CONTENT_TYPE_SONG						= 3;
    public static final int REQUEST_CONTENT_TYPE_SONG_WITH_CAPTION			= 4;
    
    /**
     * 한편보기
     */
    public static final String VIDEO_PLAY_ONE = "1";
    /**
     * 여러편 보기
     */
    public static final String VIDEO_PLAY_MULTI = "2";
    
    public static final int PLAY_TYPE_SONG 				= 0;
    public static final int PLAY_TYPE_SERIES_STORY 		= 1; // 스토리 중 시리즈
    public static final int PLAY_TYPE_SHORT_STORY  		= 2; // 스토리 중 단편
    public static final int PLAY_TYPE_STUDY_DATA		= 3; //학습 자료실
    public static final int PLAY_TYPE_EXECUTE_APP		= 4; //앱에서 실행했을때
    
    public static final String PLAY_TYPE_FEATURE_SERIES_STORY 		= "series";
    public static final String PLAY_TYPE_FEATURE_SHORT_STORY 		= "single";
    public static final String PLAY_TYPE_FEATURE_SONG				= "song";
    
    /**
     * 플레이 가능한지 여부를 판별하는 정보. 1: 사용 가능 , 2: 중복 실행 , 3: 미디어 없음, 무료사용자의 유료 컨텐츠 접근으로 실행 불가
     */
    public static final String AUTH_RESULT_AVAILABLE 			= "1";
    public static final String AUTH_RESULT_DUPLICATE_EXECUTE 	= "2";
    public static final String AUTH_RESULT_NO_MEDIA				= "3";
    
    public static final int USER_TYPE_FREE 	= 1;
    public static final int USER_TYPE_PAID	= 3;
    
    /**
     * 언어선택
     */
    public static final int LANGUAGE_KOREA 	= 0;
    public static final int LANGUAGE_ENGLISH	= 1;
	
    public static final int TARGET_DISPLAY_TABLET_WIDTH = 1920;
  	public static final int TARGET_DISPLAY_WIDTH = 1080;
 
  	public static final String ANALYTICS_CATEGORY_DEFAULT = "기본기능";
  	public static final String ANALYTICS_CATEGORY_PLAYER = "플레이어";
  	
  	public static final String ANALYTICS_ACTION_STORY 				= "동화";
  	public static final String ANALYTICS_ACTION_SONG				= "동요";
  	public static final String ANALYTICS_ACTION_STORY_PREVIEW 		= "동화 미리보기";
  	public static final String ANALYTICS_ACTION_SONG_PREVIEW		= "동요 미리보기";
  	public static final String ANALYTICS_ACTION_ENGLISH_CONTENTS	= "리틀팍스 영어 콘텐츠";
  	public static final String ANALYTICS_ACTION_CHINESE_CONTENTS	= "리틀팍스 중국어 콘텐츠";
  	public static final String ANALYTICS_ACTION_OTHER_APP			= "다른앱 보기";
  	
  	public static final String ANALYTICS_LABEL_PLAY 	= "재생";
  	public static final String ANALYTICS_LABEL_ENGLISH 	= "리틀팍스 영어";
  	public static final String ANALYTICS_LABEL_CHINESE 	= "리틀팍스 중국어";
  	public static final String ANALYTICS_LABEL_SONG		= "리틀팍스 동요";
  	public static final String ANALYTICS_LABEL_STORYBOOK_ROCKET_GIRL = "리틀팍스 스토리 북 Rocket Girl";
  			
  	public static final String CAPTION_USE = "Y";
  	public static final String CAPTION_NOT_HAVE = "N";
}
