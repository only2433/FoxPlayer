package com.littlefox.media.fox.player.dialog.listener;

public interface DialogListener
{
	/** TempleteAlertDialog 에서 사용하는 이벤트 </p>
	 *  첫번째 버튼을 눌렀을때 이벤트를 받으면 TempleteAlertDialog </p>
	 *  내부에 있는 이벤트를 전달받아서 사용하여야한다.
	 */
	public static final int FIRST_BUTTON_CLICK			= 100;
	
	/** TempleteAlertDialog 에서 사용하는 이벤트 </p>
	 *  두번째 버튼을 눌렀을때 이벤트를 받으면 TempleteAlertDialog </p>
	 *  내부에 있는 이벤트를 전달받아서 사용하여야한다.
	 */
	public static final int SECOND_BUTTON_CLICK			= 101;
	
	
	/**
	 * Flexible Dialog에서 사용하는 메소드 
	 * @param messageButtonType 선택한 버튼 
	 * @param subMessageType 보낼 Dialog Status Type
	 * @param sendObject 보낼 객체
	 */
	public void onItemClick(int messageButtonType, int messageType, Object sendObject);
}
