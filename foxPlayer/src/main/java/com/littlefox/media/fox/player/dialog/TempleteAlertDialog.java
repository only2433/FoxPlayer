package com.littlefox.media.fox.player.dialog;

import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.common.Common;
import com.littlefox.media.fox.player.common.CommonUtils;
import com.littlefox.media.fox.player.dialog.listener.DialogListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TempleteAlertDialog 
{

	/** 다이얼로그 이벤트를 굳이 받지않아도 되는 경우 사용 */
	public static final int DIALOG_EVENT_DEFAULT 													= 0;
	/**
	 * Confirm 버튼 하나
	 */
	public static final int DEFAULT_BUTTON_TYPE_1 = 0;
	
	/**
	 * Confirm 버튼, Cancel 버튼
	 */
	public static final int DEFAULT_BUTTON_TYPE_2 = 1;
	
	public static final int MODE_TEXT 				= 0;
	public static final int MODE_TITLE_HAVE_TEXT 	= 1;

	public static final int TEXT_SIZE = 36;
	
	private Context mContext;
	
	protected boolean isCancelable = true;
	protected String mTitle;
	protected String mMessage;
	protected String mFirstButtonText;
	protected String mSecondButtonText;
	protected int mButtonCount = -1;
	protected int mCurrentDialogMessageSubType = -1;
	protected int mIconResource = -1;
	protected DialogListener mDialogListener;
	protected AlertDialog.Builder mAlertDialogBuilder;
	
	private LinearLayout _BaseTitleLayout;
	private TextView _TitleText;
	private ImageView _ImageView;
	
	public TempleteAlertDialog(Context context)
	{
		mContext = context;
		mTitle	 = "";
		mMessage = "";
		isCancelable = true;
	}
	
	public TempleteAlertDialog(Context context, String message)
	{
		mContext = context;
		mTitle	 = "";
		mMessage = message;
		isCancelable = true;
	}
	
	public TempleteAlertDialog(Context context, String title, String message)
	{
		mContext = context;
		mTitle	 = title;
		mMessage = message;
		isCancelable = true;
	}
	

	
	/**
	 * 리스너에서 전달해주는 Event 타입
	 * @param type Event타입
	 */
	public void setDialogMessageSubType(int type)
	{
		mCurrentDialogMessageSubType = type;
	}
	
	public void setIconResource(int icon)
	{
		mIconResource = icon;
	}
	
	public void setButtonText(int buttonType)
	{
		if(buttonType == DEFAULT_BUTTON_TYPE_1)
		{
			setButtonText(mContext.getResources().getString(R.string.button_confirm),"");
		}
		else
		{
			setButtonText(mContext.getResources().getString(R.string.button_cancel) , mContext.getResources().getString(R.string.button_confirm));
		}
	}
	
	public void setButtonText(String firstButtonText)
	{
		setButtonText(firstButtonText, "");
	}
	
	public void setButtonText(String firstButtonText, String secondButtonText)
	{
		mFirstButtonText = firstButtonText;
		mSecondButtonText = secondButtonText;
		if(secondButtonText.equals(""))
		{
			mButtonCount = 1;
		}
		else
		{
			mButtonCount = 2;
		}
		
	}
	
	public void setCancelable(boolean isCancelable)
	{
		this.isCancelable = isCancelable;
	}
	
	public void setDialogListener(DialogListener dialogListener)
	{
		mDialogListener = dialogListener;
	}
	
	public void show()
	{
		mAlertDialogBuilder = new AlertDialog.Builder(mContext);
		if(mTitle.equals("") == false)
		{
			mAlertDialogBuilder.setTitle(mTitle);
		}
		mAlertDialogBuilder.setMessage(mMessage);
		
		if(isCancelable)
		{
			mAlertDialogBuilder.setCancelable(true);
		}
		else
		{
			mAlertDialogBuilder.setCancelable(false);
		}
		
		if(mIconResource != -1)
		{
			if(CommonUtils.getInstance(mContext).getDisplayWidth() > Common.TARGET_DISPLAY_WIDTH)
			{
				Bitmap bitmap = CommonUtils.getInstance(mContext).getBitmapFromDrawable(mContext.getResources().getDrawable(mIconResource), CommonUtils.getInstance(mContext).getPixel(60), CommonUtils.getInstance(mContext).getPixel(60));
				Drawable drawable = CommonUtils.getInstance(mContext).getDrawableFromBitmap(bitmap);
				mAlertDialogBuilder.setIcon(drawable);
			}
			else
			{
				mAlertDialogBuilder.setIcon(mIconResource);
			}
			
		}
		
		mAlertDialogBuilder.setNegativeButton(mFirstButtonText, new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				try
				{
					mDialogListener.onItemClick(DialogListener.FIRST_BUTTON_CLICK, mCurrentDialogMessageSubType, null);
				}catch(NullPointerException e)
				{
					
				}
				
			}
		});
		if(mButtonCount == 2)
		{
			mAlertDialogBuilder.setPositiveButton(mSecondButtonText, new OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					try
					{
						mDialogListener.onItemClick(DialogListener.SECOND_BUTTON_CLICK, mCurrentDialogMessageSubType, null);
					}catch(NullPointerException e)
					{
						
					}
					
				}
			});
		}
		
		//mAlertDialogBuilder.show();
		
		AlertDialog dialog = mAlertDialogBuilder.show();
		
		

		TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
		
		dialog.show();
	}

}
