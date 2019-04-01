package com.littlefox.media.fox.player.database;

import java.util.ArrayList;

import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.object.PlayedContentInformation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 플레이 한 동영상 파일들을 관리하기 위한 Database. 플레이가 끝났을때 Insert , 플레이 시작시 Update
 */
public class PlayedContentDBHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "play_content_base";
	private static final int MAX_SEARCH_LIMIT = 200;
	
	public static final String TEXT_TRUE = "true";
	public static final String TEXT_FALSE = "false";
	
	public static final String ORDER_ASC ="ASC";
	public static final String ORDER_DESC ="DESC";
	
	public static final int DOWNLOAD_NOT_COMPLETE = 0;
	public static final int DOWNLOAD_COMPLETE = 1;

	//DB 의 키
	public static final String KEY_ID 					= "content_id";

	/**
	 * 플레이 될 때 마다 업데이트 시킨다. 그래서 특정기한이 지나거나 용량이 어느정도 되엇을 때 삭제한다.
	 */
	public static final String KEY_RECENT_PLAY_TIME 	= "recent_play_time";

	/**
	 * 동영상을 저장한 File Path
	 */
	public static final String KEY_FILE_PATH			= "file_path";
	
	/**
	 * 총 플레이 타임
	 */
	public static final String KEY_TOTAL_PLAY_TIME	= "total_play_time";
	
	/**
	 * 다운로드가 다 되었는지 체크. 다운로드가 다 되었다면, 프로그래시브 다운로드 의 파일 검사 로직을 피하기 위해
	 */
	public static final String KEY_DOWNLOAD_COMPLETE = "download_complete";
	
	public static PlayedContentDBHelper sPlayedContentDBHelper = null;
	
	public static PlayedContentDBHelper getInstance(Context context)
	{
		if(sPlayedContentDBHelper == null)
		{
			sPlayedContentDBHelper = new PlayedContentDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		return sPlayedContentDBHelper;
	}
	
	public PlayedContentDBHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_CONTENT_TABLE =
				"CREATE TABLE " + DATABASE_NAME + "("
				+ KEY_ID +" TEXT PRIMARY KEY,"
				+ KEY_RECENT_PLAY_TIME + " TEXT,"
				+ KEY_FILE_PATH + " TEXT,"
				+ KEY_TOTAL_PLAY_TIME + " TEXT,"
				+ KEY_DOWNLOAD_COMPLETE + " INTEGER"+")";

		db.execSQL(CREATE_CONTENT_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

		// Create tables again
		onCreate(db);
	}
	
	public void release()
	{
		if(sPlayedContentDBHelper != null)
		{
			sPlayedContentDBHelper.close();
		}
	}

	public PlayedContentInformation getContentInformation(String contentId)
	{
		SQLiteDatabase database = this.getReadableDatabase();
		PlayedContentInformation result = null;
		Cursor cursor;

		cursor = database.query(DATABASE_NAME,
				new String[]{KEY_ID, KEY_RECENT_PLAY_TIME,KEY_FILE_PATH, KEY_TOTAL_PLAY_TIME, KEY_DOWNLOAD_COMPLETE}
				, KEY_ID + "=?", new String[]{contentId}, null, null, null, null);
		
		Log.i("contentId : "+contentId);
		if(cursor == null || cursor.getCount() == 0)
		{
			Log.i("VALUE NOT HAVE");
			return null;
		}
		else
		{
			cursor.moveToFirst();
			Log.i("VALUE HAVE");
		}
		boolean isDownloadComplete = cursor.getInt(4) == DOWNLOAD_COMPLETE ? true : false;
		
		result  = new PlayedContentInformation(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),isDownloadComplete);
		cursor.close();
		return result;
	}

	
	public void addPlayedContent(PlayedContentInformation playedContentInformation)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, playedContentInformation.getContentID());
		values.put(KEY_RECENT_PLAY_TIME, playedContentInformation.getRecentPlayTime());
		values.put(KEY_FILE_PATH, playedContentInformation.getFilePath());
		values.put(KEY_TOTAL_PLAY_TIME, playedContentInformation.getTotalPlayTime());
		
		int downloadStatus = playedContentInformation.isDownloadComplete() == true ? DOWNLOAD_COMPLETE : DOWNLOAD_NOT_COMPLETE;
		values.put(KEY_DOWNLOAD_COMPLETE, downloadStatus);
		
		database.insert(DATABASE_NAME, null, values);
	}

	/**
	 * Played 된 Content 의 값을 Update 시킨다.
	 * @param contentId Primary Key 인 컨텐트 아이디
	 * @param key 업데이트 시킬 Key
	 * @param value 업데이트 시킬 value
	 */
	public void updatePlayedContent(String contentId, String key, String value)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(key,value);
		database.update(DATABASE_NAME, values, KEY_ID + " = ?" ,new String[] { contentId});
	}
	
	/**
	 * Played 된 Content 의 값을 Update 시킨다.
	 * @param contentId Primary Key 인 컨텐트 아이디
	 * @param key 업데이트 시킬 Key
	 * @param value 업데이트 시킬 value
	 */
	public void updatePlayedContent(String contentId, String key, int value)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(key,value);
		database.update(DATABASE_NAME, values, KEY_ID + " = ?" ,new String[] { contentId});
	}

	/**
	 * Played 된 Content 를 삭제 한다.
	 * @param contentId Primary Key 인 컨텐트 아이디
	 */
	public void deletePlayedContent(String contentId)
	{
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete(DATABASE_NAME, KEY_ID + " = ?" ,new String[] {contentId});
		
	}

	public ArrayList<PlayedContentInformation> getPlayedContentList(String key, String orderBy)
	{
		ArrayList<PlayedContentInformation> resultList = new ArrayList<PlayedContentInformation>();
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor cursor;
		
		cursor = database.query(DATABASE_NAME,
				new String[]{KEY_ID, KEY_RECENT_PLAY_TIME,KEY_FILE_PATH, KEY_TOTAL_PLAY_TIME, KEY_DOWNLOAD_COMPLETE}
				, null, null, null, null,  key+" "+orderBy, String.valueOf(MAX_SEARCH_LIMIT));
		
		if(cursor == null || cursor.getCount() == 0)
		{
			return resultList;
		}
		
		if (cursor.moveToFirst()) {
			do {
				boolean isDownloadComplete = cursor.getInt(4) == DOWNLOAD_COMPLETE ? true : false;
				resultList.add(new PlayedContentInformation(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),isDownloadComplete));
			}while (cursor.moveToNext());
		}

		cursor.close();
		
		return resultList;
	}
	
	public void deleteAllTable()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL("delete from "+DATABASE_NAME);
	}
	
	public void databaseClose()
	{
		SQLiteDatabase database = this.getReadableDatabase();
		database.close();
	}


}
