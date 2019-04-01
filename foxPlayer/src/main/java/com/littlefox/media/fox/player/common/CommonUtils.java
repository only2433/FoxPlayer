package com.littlefox.media.fox.player.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.application.MainApplication;
import com.littlefox.media.fox.player.object.DisPlayMetricsObject;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by 정재현 on 2015-07-07.
 */
public class CommonUtils
{
	public static CommonUtils sCommonUtils = null;
	public static Context sContext = null;
	
	public static CommonUtils getInstance(Context context)
	{
		if(sCommonUtils == null)
		{
			sCommonUtils = new CommonUtils();
		}
		sContext = context;
		
		return sCommonUtils;
	}
	

    public String getDateTime(long timeMs) {

    	Date date = new Date(timeMs); 
    	String todayString  = new SimpleDateFormat("MM/dd/yyyy").format(date);
    	return todayString;
    }
    
    public String getDatePayTime(long timeMs) {

    	Date date = new Date(timeMs); 
    	String todayString  = new SimpleDateFormat("yyyy.MM.dd").format(date);
    	return todayString;
    }
    
    /**
     * 밀리세컨드를 시간 String  으로 리턴한다.
     * @param timeMs 밀리세컨드
     * @return 시간 String Ex) HH:MM:TT
     */
    public  String getMillisecondTime(int timeMs) {

        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
    
    /**
     * 세컨드를 시간 String  으로 리턴한다.
     * @param time 밀리세컨드
     * @return 시간 String Ex) HH:MM:TT
     */
    public String getSecondTime(int time)
    {
    	 StringBuilder mFormatBuilder = new StringBuilder();
         Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

         int seconds = time % 60;
         int minutes = (time / 60) % 60;
         int hours   = time / 3600;


         
         mFormatBuilder.setLength(0);
         if (hours > 0) {
             return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
         } else {
             return mFormatter.format("%02d:%02d", minutes, seconds).toString();
         }
    }

    public void showDeviceInfo()
    {
    	Log.f("BRAND : "+Build.BRAND);
    	Log.f("DEVICE : "+ Build.DEVICE);
    	Log.f("MODEL : "+ Build.MODEL);
    	Log.f("VERSION SDK : "+ Build.VERSION.SDK_INT);
    	Log.f("APP VERSION : "+getPackageVersionName(Common.PACKAGE_NAME));
    	Log.f("WIDTH PIXEL : " + MainApplication.sDisPlayMetrics.widthPixels+", HEIGHT PIXEL : " + MainApplication.sDisPlayMetrics.heightPixels);
    }

    /**
     * <pre>
     * Window 의 정보를 얻어온다.
     * </pre>
     *
     * @return
     */
    public void getWindowInfo()
    {
    	int width = 0;
    	int height = 0;
        if(MainApplication.sDisPlayMetrics == null)
        {
            MainApplication.sDisPlayMetrics  = new DisplayMetrics();
        }
        ((Activity) sContext).getWindowManager().getDefaultDisplay().getMetrics(MainApplication.sDisPlayMetrics);
       
        width = MainApplication.sDisPlayMetrics.widthPixels;
        height = MainApplication.sDisPlayMetrics.heightPixels;
        
        /**
         * 방어코드 가끔 OS 결함으로 width , height 가 잘못들어올때 방어코드 처리 
         */
        if(Feature.IS_TABLET)
        {
        	if(MainApplication.sDisPlayMetrics.widthPixels < MainApplication.sDisPlayMetrics.heightPixels)
            {
        		MainApplication.sDisPlayMetrics.widthPixels = height;
        		MainApplication.sDisPlayMetrics.heightPixels = width;
            }
        }
        else
        {
        	if(MainApplication.sDisPlayMetrics.widthPixels > MainApplication.sDisPlayMetrics.heightPixels)
            {
        		MainApplication.sDisPlayMetrics.widthPixels = height;
        		MainApplication.sDisPlayMetrics.heightPixels = width; 
            }
        }
        
        DisPlayMetricsObject object = new DisPlayMetricsObject(MainApplication.sDisPlayMetrics.widthPixels, MainApplication.sDisPlayMetrics.heightPixels);
        setPreferenceObject(Common.PARAMS_DISPLAY_METRICS, object);
    }
   

    /**
     * 1080 * 1920  기준으로 멀티 해상도의 픽셀을 계산한다. Tablet 은 1920 * 1200
     * @param value 1080 * 1920  의 픽셀
     * @return
     */
    public int getPixel(int value)
    {
		try
		{
			if (MainApplication.sDisplayFactor == 0.0f)
			{
    			if(Feature.IS_TABLET)
    				MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.widthPixels / 1920.0f;
    			else
    				MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.widthPixels / 1080.0f;
			}
		}
		catch (NullPointerException e)
		{
			DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
			if(Feature.IS_TABLET)
				MainApplication.sDisplayFactor = object.widthPixel / 1920.0f;
			else
				MainApplication.sDisplayFactor = object.widthPixel / 1080.0f;
		}
		
		
        return (int)(value * MainApplication.sDisplayFactor);
    }

    /**
     * 1080 * 1920  기준으로 멀티 해상도의 픽셀을 계산한다. Tablet 은 1920 * 1200
     * @param value 1080 * 1920  의 픽셀
     * @return
     */
	public float getPixel(float value)
	{
		try
		{
			if (MainApplication.sDisplayFactor == 0.0f)
			{
    			if(Feature.IS_TABLET)
    				MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.widthPixels / 1920.0f;
    			else
    				MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.widthPixels / 1080.0f;
			}
		}
		catch (NullPointerException e)
		{
			DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
			if(Feature.IS_TABLET)
				MainApplication.sDisplayFactor = object.widthPixel / 1920.0f;
			else
				MainApplication.sDisplayFactor = object.widthPixel / 1080.0f;
		}

		return value * MainApplication.sDisplayFactor;
	}

    /**
     * 1080 * 1920 기준으로 멀티 해상도의 픽셀을 계산한다. Tablet 은 1920 * 1200
     * @param value 1080 * 1920 의 픽셀
     * @return
     */
    public int getHeightPixel(int value)
    {
    	try
		{
			if (MainApplication.sDisplayFactor == 0.0f)
			{
				if(Feature.IS_TABLET)
					MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.heightPixels / 1200.0f;
				else
					MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.heightPixels / 1920.0f;
			}
		}
		catch (NullPointerException e)
		{
			DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
			if(Feature.IS_TABLET)
				MainApplication.sDisplayFactor = object.heightPixel / 1200.0f;
			else
				MainApplication.sDisplayFactor = object.heightPixel / 1080.0f;
		}
		return (int)(value * MainApplication.sDisplayFactor);
    }

    /**
     * 1080 * 1920기준으로 멀티 해상도의 픽셀을 계산한다.
     * @param value 1080 * 1920 의 픽셀
     * @return
     */
    public float getHeightPixel(float value)
    {
		try
		{
			if (MainApplication.sDisplayFactor == 0.0f)
			{
				if(Feature.IS_TABLET)
					MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.heightPixels / 1200.0f;
				else
					MainApplication.sDisplayFactor = MainApplication.sDisPlayMetrics.heightPixels / 1920.0f;
			}
		}
		catch (NullPointerException e)
		{
			DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
			if(Feature.IS_TABLET)
				MainApplication.sDisplayFactor = object.heightPixel / 1200.0f;
			else
				MainApplication.sDisplayFactor = object.heightPixel / 1080.0f;
		}
		return value * MainApplication.sDisplayFactor;
    }
    
    /**
     * 최소 지원 해상도를 리턴
     * @return
     */
    public int getMinDisplayWidth()
    {
    	if(Feature.IS_TABLET)
    	{
    		return 1280;
    	}
    	else
    	{
    		return 1080;
    	}
    }
    
    public float getDisplayWidth()
    {
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
    	if(object != null)
    	{
    		return object.widthPixel;
    	}
    	return 0f;
    }
    
    public float getDisplayHeight()
    {
    	DisPlayMetricsObject object = (DisPlayMetricsObject) getPreferenceObject(Common.PARAMS_DISPLAY_METRICS, DisPlayMetricsObject.class);
    	if(object != null)
    	{
    		return object.heightPixel;
    	}
    	return 0f;
    }

    public DisplayMetrics getDisPlayMetrics()
    {
        return MainApplication.sDisPlayMetrics;
    }

    /**
     * 저장한 프리퍼런스를 불러온다.
     * @param key  해당 값의 키값
     * @param type 데이터의 타입
     * @return
     */
    public Object getSharedPreference(String key, int type)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);

        switch (type)
        {
            case Common.TYPE_PARAMS_BOOLEAN:
                return pref.getBoolean(key, false);
            case Common.TYPE_PARAMS_INTEGER:
                return pref.getInt(key, -1);
            case Common.TYPE_PARAMS_STRING:
                return pref.getString(key, "");
        }

        return pref.getBoolean(key, false);
    }

    /**
     * 해당 프리퍼런스를 저장한다.
     * @param key 해당 값의 키값
     * @param object 저장할 데이터
     */
    public void setSharedPreference(String key, Object object)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
        SharedPreferences.Editor editor = pref.edit();

        if(object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        }
        else if(object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        }
        else if(object instanceof String)
        {
            editor.putString(key, (String) object);

        }

        editor.commit();

    }
    
    
    /**
     * 현재 디스플레이의 가로 해상도를 리턴
     * @return
     */
    public int getDisplayWidthPixel()
    {
    	if(MainApplication.sDisPlayMetrics == null)
    	{
    		return 0;
    	}
    	
    	return MainApplication.sDisPlayMetrics.widthPixels;
    }
    
    /**
     * 현재 디스플레이의 세로 해상도를 리턴
     * @return
     */
    public int getDisplayHeightPixel()
    {
    	if(MainApplication.sDisPlayMetrics == null)
    	{
    		return 0;
    	}
    	
    	return MainApplication.sDisPlayMetrics.heightPixels;
    }
    
    /**
     * 해상도가 특정 이하의 해상도인지 확인하는 메소드
     * @return TRUE : Minimum 보다 이하 , FALSE : Minimum 보다 이상
     */
	public  boolean isDisplayMinimumSize()
	{
		Log.i("CommonUtils.getDisplayWidthPixel(context) : "+getDisplayWidthPixel());
		if(getMinDisplayWidth() > getDisplayWidthPixel())
		{
			return true;
		}
		
		return false;
	}

    /**
     * 현재 모델이 타블릿인지 아닌지 확인
     * @return
     */
    public boolean isTablet()
    {
        if (Build.VERSION.SDK_INT >= 19)
        {
            return checkTabletDeviceWithScreenSize(sContext) &&
                    checkTabletDeviceWithProperties() &&
                    checkTabletDeviceWithUserAgent(sContext);
        }
        else
        {
            return checkTabletDeviceWithScreenSize(sContext) &&
                    checkTabletDeviceWithProperties() ;

        }
    }

    private boolean checkTabletDeviceWithScreenSize(Context context) {
        boolean device_large = ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        Log.f("device_large : "+device_large);
        if (device_large)
        {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Log.f("metrics.densityDpi : "+metrics.densityDpi);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_360) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTabletDeviceWithProperties()
    {
        try
        {
            InputStream ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream();
            byte[] bts = new byte[1024];
            ism.read(bts);
            ism.close();

            boolean isTablet = new String(bts).toLowerCase().contains("tablet");
            return isTablet;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return false;
        }
    }

    private boolean checkTabletDeviceWithUserAgent(Context context)
    {
        try
        {
            WebView webView = new WebView(context);
            String ua = webView.getSettings().getUserAgentString();
            webView = null;
            if (ua.contains("Mobile Safari"))
            {
                return false;
            } else
            {
                return true;
            }
        } catch (Exception e)
        {
            return false;
        }
    }



    /**
     * 패키지버젼코드 확인
     *
     * @return 패키지 버젼 코드
     */
    public int getPackageVersionCode()
    {
        int result = -1;
        try
        {
            PackageInfo pi = sContext.getPackageManager().getPackageInfo(Common.PACKAGE_NAME, 0);
            if (pi != null)
                result = pi.versionCode;
        }
        catch (Exception ex)
        {
        }
        return result;
    }

    /**
     * 패키지 버전 네임 확인
     * @return 패키지 버전 네임
     */
    public String getPackageVersionName(String packageName)
    {
        String result = "";
        try
        {
            PackageInfo pi = sContext.getPackageManager().getPackageInfo(packageName, 0);
            if (pi != null)
                result = pi.versionName;
        }
        catch (Exception ex)
        {
        }
        return result;
    }

    /**
     * 인스톨 되어있나 검색한다.
     * @param packageName 해당 패키지 명
     * @return
     */
    public boolean isInstalledPackage(String packageName)
    {
        boolean result = true;
        try
        {
            Intent intent = sContext.getPackageManager().getLaunchIntentForPackage(packageName);


            if(intent == null)
            {
                result = false;
            }
        }catch(Exception e)
        {
            result = false;
        }

        return result;
    }

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a dialog that allows users to download the APK from the Google Play Store or enable it in the device's
	 * system settings.
	 */

	public static boolean checkPlayServices()
	{
		final int googlePlayServicesCheck = GooglePlayServicesUtil.isGooglePlayServicesAvailable((Activity) sContext);
		switch (googlePlayServicesCheck)
		{
		case ConnectionResult.SUCCESS:
			return true;
		case ConnectionResult.SERVICE_DISABLED:
		case ConnectionResult.SERVICE_INVALID:
		case ConnectionResult.SERVICE_MISSING:
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCheck, (Activity) sContext, 0);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface dialogInterface)
				{
					((Activity) sContext).finish();
				}
			});
			dialog.show();
		}
		return false;
	}


    /**
     * 맥 어드레스를 받아온다.
     * @return
     */
    public String getMacAddress()
    {
        WifiManager wifimanager = (WifiManager)sContext.getSystemService(Context.WIFI_SERVICE);
        return wifimanager.getConnectionInfo().getMacAddress();
    }

   

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public float convertDpToPixel(float dp){
        Resources resources = sContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public float convertPixelsToDp(float px){
        Resources resources = sContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    
    public long getAvailableStorageSize()
    {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        long result;
        if(Build.VERSION.SDK_INT > Common.JELLYBEAN_CODE_4_3)
        {
            result = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        }
        else
        {
            result = (long)stat.getAvailableBlocks()* (long)stat.getBlockSize();
        }


        return result/(1024 * 1024);
    }

    /**
     * 비디오파일이 다운로드된 Internal Storage폴더의 사이즈를 리턴
     */
	public long getSizeVideoFileStorage()
	{
		long totalSize = 0;
		File appBaseFolder = new File(Common.PATH_APP_ROOT);
		for (File f : appBaseFolder.listFiles())
		{
			if (f.isDirectory())
			{
				long dirSize = browseFiles(f);
				totalSize += dirSize;
			}
			else
			{
				totalSize += f.length();
			}
		}
		Log.f("App uses " + totalSize + " total bytes");
		
		return totalSize/(1024 * 1024);
	}

	private long browseFiles(File dir)
	{
		long dirSize = 0;
		for (File f : dir.listFiles())
		{
			dirSize += f.length();
			if (f.isDirectory())
			{
				dirSize += browseFiles(f);
			}
		}
		return dirSize;
	}

	public int getDrawableResourceFromString(Context context, String name)
    {
        return context.getResources().getIdentifier(name, "drawable", context.getApplicationContext().getPackageName());
    }
    
    public Bitmap getBitmapFromDrawable(Drawable mDrawable, int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);

		mDrawable.setBounds(0, 0, width, height);

		mDrawable.draw(canvas);

		return bitmap;

	}


    public Bitmap getRoundedCornerBitmap(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public Drawable getRoundedCornerRect(int width, int height, int color)
    {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, getPixel(20), getPixel(20), paint);

        return getDrawableFromBitmap(output);
    }

    public Drawable getDrawableFromBitmap(Bitmap mBitmap)
    {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(sContext.getResources(), mBitmap);

        return bitmapDrawable;
    }

    public  Drawable getScaledDrawable(int width, int height, int drawable)
    {
        Bitmap bitmap 	= BitmapFactory.decodeResource(sContext.getResources(), drawable);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return getDrawableFromBitmap(bitmap);
    }


    /**
     * 앱버젼이 같은지 확인
     * @param appVersion 서버의 버젼
     * @return TRUE : 현재 로컬버젼과 같다. </p> FALSE : 현재 로컬버젼과 다르다.
     */
    public boolean isAppVersionEqual(String appVersion)
    {
        return appVersion.equals(getPackageVersionName(Common.PACKAGE_NAME));
    }

    /**
     * 현재 버젼과 업데이트 되는 버젼을 비교하기 위해
     * 이전에 기억하는 팝업 정보도 삭제한다.
     * @return TRUE : 현재 버젼과 업데이트 되는 버젼과 같다. <p> FALSE : 현재 버젼과 업데이트 되는 버젼과 다르다.
     */
    public boolean verifyCurrentVersionCode()
    {
        int registerVersion = (Integer)getSharedPreference(Common.PARAMS_REGISTER_APP_VERSION, Common.TYPE_PARAMS_INTEGER);

        int currentVersion = getPackageVersionCode();

        Log.i("registerVersion : "+registerVersion+", currentVersion : "+currentVersion);

        if(currentVersion != registerVersion)
        {
            setSharedPreference(Common.PARAMS_REGISTER_APP_VERSION, currentVersion);
            return false;
        }

        return true;
    }
    
    /**
     * 오브젝트 클래스를 불러오는 프리퍼런스
     * @param key 키값
     * @param className 클래스 네임
     * @return
     */
    public Object getPreferenceObject(String key, Class className)
    {
    	Object result = null;
    	 SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
    	 String loadObjectString = pref.getString(key, "");
    	 
    	 if(loadObjectString.equals("") == false)
    	 {
    		 result = new Gson().fromJson(loadObjectString, className);
    	 }
    	
    	 return result;
    }
    
    /**
     * 오브젝트 클래스를 저장하는 프리퍼런스
     * @param key 키값
     * @param object 저장할 오브젝트
     */
    public void setPreferenceObject(String key, Object object)
    {
    	String saveObjectString = "";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(sContext);
        SharedPreferences.Editor editor = pref.edit();
        
        if(object != null)
        {
        	saveObjectString = new Gson().toJson(object);
        }
        
        editor.putString(key, saveObjectString);
        editor.commit();
    }
	

	public Animation getTranslateYAnimation(int duration, float fromYValue, float toYValue)
	{
		return getTranslateYAnimation(duration , fromYValue, toYValue, null);
	}
	

	public Animation getTranslateYAnimation(int duration, float fromYValue, float toYValue, Interpolator interpolator)
	{
		Animation anim = null;
		anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,fromYValue, Animation.ABSOLUTE, toYValue);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		if(interpolator != null)
		{
			anim.setInterpolator(interpolator);
		}
		return anim;
	}
	
	public Animation getAlphaAnimation(int duration, float fromValue, float toValue)
	{
		Animation anim = null;
		anim = new AlphaAnimation(fromValue, toValue);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		return anim;
	}
	
	public Animation getScaleAnimation(int duration, float fromScaleValue, float toScaleValue)
	{
		Animation anim = null;
		anim = new ScaleAnimation(fromScaleValue, toScaleValue, fromScaleValue, toScaleValue, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		return anim;
	}
	
	public void setStatusBar(int color )
	{
		Window window = ((Activity) sContext).getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(color);
	}
	
	public void showSnackMessage(CoordinatorLayout coordinatorLayout, String message, int color)
	{
		showSnackMessage(coordinatorLayout, message, color, -1);
	}
	
	public void showSnackMessage(CoordinatorLayout coordinatorLayout, String message, int color , int gravity)
	{
		Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
		View view = snackbar.getView();
		TextView textView = (TextView)view.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(color);
		if(gravity != -1)
			textView.setGravity(gravity);
		snackbar.show();
	}
	
	public void showSnackMessage(CoordinatorLayout coordinatorLayout, String[] message, int[] color)
	{
		int beforeCount = 0;
		String messageText = "";
		SpannableStringBuilder spannableStringBuilder;
		for(String s : message)
		{
			messageText += s;
		}
		
		spannableStringBuilder = new SpannableStringBuilder(messageText);
		for(int i = 0; i < message.length; i++)
		{
			int currentCount = 0;
			for(int j = 0; j < i +1; j++)
			{
				currentCount += message[j].length();
			}
			spannableStringBuilder.setSpan(new ForegroundColorSpan(color[i]) ,beforeCount , currentCount , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			beforeCount = currentCount;
		}
		
		Snackbar snackbar = Snackbar.make(coordinatorLayout, messageText, Snackbar.LENGTH_SHORT);
		View view = snackbar.getView();
		TextView textView = (TextView)view.findViewById(android.support.design.R.id.snackbar_text);
		textView.setText(spannableStringBuilder);
		snackbar.show();
	}
	

	
	/**
	 * 플레이 시간으로 프리뷰 시간을 구하는 메소드
	 * @param totalPlayTime 토탈 플레이 시간
	 * @return
	 */
	public int getPreviewTime(int totalPlayTime)
	{
		final int MIN_PREVIEW_TIME = 10;
		final int MAX_PREVIEW_TIME = 60;
		final int TERM_PREVIEW_TIME = 10;
		
		final int MIN_TOTAL_PLAY_TIME = 40;
		final int MAX_TOTAL_PLAY_TIME = 120;
		final int TERM_PLAY_TIME = 20;
		
		int result = 0;
		int count = 0;
		
		while(result == 0)
		{
			if(totalPlayTime < MIN_TOTAL_PLAY_TIME + count * TERM_PLAY_TIME)
			{
				result = MIN_PREVIEW_TIME + TERM_PREVIEW_TIME * count;
			}
			else if(totalPlayTime >= MAX_TOTAL_PLAY_TIME)
			{
				result = MAX_PREVIEW_TIME;
			}
			count++;
		}
		
		return result;
	}
	

	
	/**
	 *  30일이 지났는 지 체크
	 * @param payEndMiliseconds 해당 시간
	 * @return TRUE: 30일 이 넘음 , FALSE : 30일이 지나지 않음
	 */
	public boolean isOverPayDay(long payEndMiliseconds)
	{
		Log.f("Today : " + getDateTime(System.currentTimeMillis()));
		Log.f("Pay End Day : " + getDateTime(payEndMiliseconds));
		
		if(System.currentTimeMillis() >=  payEndMiliseconds)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

	
	public long getAdded1Month(long currentPaidMilliseconds)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentPaidMilliseconds);
		calendar.add(Calendar.MONTH, 1);
		return calendar.getTimeInMillis();
	}
	
	public long getAdded1year(long currentPaidMilliseconds)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentPaidMilliseconds);
		calendar.add(Calendar.YEAR, 1);
		return calendar.getTimeInMillis();
	}
	
	
	public void startLinkMove(String link)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(link));
		sContext.startActivity(intent);
	}
	
	/**
	 * 소프트 네비게이션 바가 있는지 체크
	 * @return
	 */
	public boolean isHaveNavigationBar()
	{
		 Point appUsableSize = getAppUsableScreenSize();
		 Point realScreenSize = getRealScreenSize();
		 
		 if (appUsableSize.y < realScreenSize.y) 
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	}
	
	/**
	 * 네이게이션바 사이즈를 리턴한다.
	 * @return
	 */
	public  Point getNavigationBarSize() {
	    Point appUsableSize = getAppUsableScreenSize();
	    Point realScreenSize = getRealScreenSize();

	    // navigation bar on the right
	    if (appUsableSize.x < realScreenSize.x) 
	    {
	        return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
	    }

	    // navigation bar at the bottom
	    if (appUsableSize.y < realScreenSize.y) 
	    {
	        return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
	    }

	    // navigation bar is not present
	    return new Point();
	}

	public  Point getAppUsableScreenSize() {
	    WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
	    Display display = windowManager.getDefaultDisplay();
	    Point size = new Point();
	    display.getSize(size);
	    return size;
	}

	public  Point getRealScreenSize() {
	    WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
	    Display display = windowManager.getDefaultDisplay();
	    Point size = new Point();

	    if (Build.VERSION.SDK_INT >= 17) {
	        display.getRealSize(size);
	    } else if (Build.VERSION.SDK_INT >= 14) {
	        try {
	            size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
	            size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
	        } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
	    }
	    return size;
	}

	public void inquireForDeveloper()
	{
		Intent i;
		if (Locale.getDefault().toString().equals(Locale.KOREA.toString()))
		{
			i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Common.DEVELOPER_EMAIL, null));
		}
		else
		{
			i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Common.DEVELOPER_EMAIL, null));
		}

		i.putExtra(Intent.EXTRA_SUBJECT, "");

		i.putExtra(Intent.EXTRA_TEXT, "[" + Build.BRAND.toString() + "]" + " Model: " + Build.MODEL + ", OS: " + Build.VERSION.RELEASE + ", Ver: "
				+ getPackageVersionName(Common.PACKAGE_NAME));
		String strTitle = sContext.getResources().getString(R.string.app_name);
		sContext.startActivity(Intent.createChooser(i, strTitle));
	}
	
	public void finishApplication()
	{
		((Activity)sContext).finish();
	}
	
}
