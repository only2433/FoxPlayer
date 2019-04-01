// Generated code from Butter Knife. Do not modify!
package com.littlefox.media.fox.player;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.littlefox.library.view.media.ProgressiveMediaPlayer;
import com.ssomai.android.scalablelayout.ScalableLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PlayerActivity_ViewBinding implements Unbinder {
  private PlayerActivity target;

  private View view2131296434;

  private View view2131296424;

  private View view2131296430;

  private View view2131296428;

  private View view2131296427;

  private View view2131296499;

  private View view2131296501;

  private View view2131296505;

  private View view2131296400;

  private View view2131296305;

  private View view2131296306;

  private View view2131296307;

  private View view2131296308;

  @UiThread
  public PlayerActivity_ViewBinding(PlayerActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PlayerActivity_ViewBinding(final PlayerActivity target, View source) {
    this.target = target;

    View view;
    target._ProgressMediaPlayer = Utils.findRequiredViewAsType(source, R.id.progressive_player, "field '_ProgressMediaPlayer'", ProgressiveMediaPlayer.class);
    target._BackgroundDiscoverImage = Utils.findRequiredViewAsType(source, R.id.player_background, "field '_BackgroundDiscoverImage'", ImageView.class);
    target._LoadingLayout = Utils.findRequiredViewAsType(source, R.id.progress_wheel_layout, "field '_LoadingLayout'", ScalableLayout.class);
    target._TopViewLayout = Utils.findRequiredViewAsType(source, R.id.player_top_base_layout, "field '_TopViewLayout'", ScalableLayout.class);
    target._TopTitleText = Utils.findRequiredViewAsType(source, R.id.player_top_title, "field '_TopTitleText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.player_subtitle_button, "field '_TopCaptionSettingButton' and method 'onPlayerButtonClick'");
    target._TopCaptionSettingButton = Utils.castView(view, R.id.player_subtitle_button, "field '_TopCaptionSettingButton'", ImageView.class);
    view2131296434 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.player_close_button, "field '_TopCloseButton' and method 'onPlayerButtonClick'");
    target._TopCloseButton = Utils.castView(view, R.id.player_close_button, "field '_TopCloseButton'", ImageView.class);
    view2131296424 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    target._BottomViewLayout = Utils.findRequiredViewAsType(source, R.id.player_bottom_base_layout, "field '_BottomViewLayout'", ScalableLayout.class);
    target._CurrentPlayTimeText = Utils.findRequiredViewAsType(source, R.id.player_current_play_time, "field '_CurrentPlayTimeText'", TextView.class);
    target._ThumbSeekbar = Utils.findRequiredViewAsType(source, R.id.seekbar_play, "field '_ThumbSeekbar'", SeekBar.class);
    target._RemainPlayTimeText = Utils.findRequiredViewAsType(source, R.id.player_remain_play_time, "field '_RemainPlayTimeText'", TextView.class);
    target._LockButton = Utils.findRequiredViewAsType(source, R.id.player_lock_button, "field '_LockButton'", ImageView.class);
    target._PlayButtonLayout = Utils.findRequiredViewAsType(source, R.id.player_play_button_layout, "field '_PlayButtonLayout'", ScalableLayout.class);
    view = Utils.findRequiredView(source, R.id.player_prev_button, "field '_PrevButton' and method 'onPlayerButtonClick'");
    target._PrevButton = Utils.castView(view, R.id.player_prev_button, "field '_PrevButton'", ImageView.class);
    view2131296430 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.player_play_button, "field '_PlayButton' and method 'onPlayerButtonClick'");
    target._PlayButton = Utils.castView(view, R.id.player_play_button, "field '_PlayButton'", ImageView.class);
    view2131296428 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.player_next_button, "field '_NextButton' and method 'onPlayerButtonClick'");
    target._NextButton = Utils.castView(view, R.id.player_next_button, "field '_NextButton'", ImageView.class);
    view2131296427 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    target._BaseSubTitleLayout = Utils.findRequiredViewAsType(source, R.id.subtitle_setting_layout, "field '_BaseSubTitleLayout'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.subtitle_close_button, "field '_SubtitleCloseButton' and method 'onDisplayButtonClick'");
    target._SubtitleCloseButton = Utils.castView(view, R.id.subtitle_close_button, "field '_SubtitleCloseButton'", ImageView.class);
    view2131296499 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDisplayButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.subtitle_normal_layout, "field '_SubTitleNormalButton' and method 'onDisplayButtonClick'");
    target._SubTitleNormalButton = Utils.castView(view, R.id.subtitle_normal_layout, "field '_SubTitleNormalButton'", ScalableLayout.class);
    view2131296501 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDisplayButtonClick(p0);
      }
    });
    target._SubTitleNormalIcon = Utils.findRequiredViewAsType(source, R.id.subtitle_normal_icon, "field '_SubTitleNormalIcon'", ImageView.class);
    target._SubTitleNormalText = Utils.findRequiredViewAsType(source, R.id.subtitle_normal_text, "field '_SubTitleNormalText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.subtitle_use_layout, "field '_SubTitleUseButton' and method 'onDisplayButtonClick'");
    target._SubTitleUseButton = Utils.castView(view, R.id.subtitle_use_layout, "field '_SubTitleUseButton'", ScalableLayout.class);
    view2131296505 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDisplayButtonClick(p0);
      }
    });
    target._SubTitleUseIcon = Utils.findRequiredViewAsType(source, R.id.subtitle_use_icon, "field '_SubTitleUseIcon'", ImageView.class);
    target._SubTitleUseText = Utils.findRequiredViewAsType(source, R.id.subtitle_use_text, "field '_SubTitleUseText'", TextView.class);
    target._BasePlayOtherAppEndLayout = Utils.findRequiredViewAsType(source, R.id.play_end_layout, "field '_BasePlayOtherAppEndLayout'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.other_app_close_button, "field '_OtherAppCloseButton' and method 'onPlayerButtonClick'");
    target._OtherAppCloseButton = Utils.castView(view, R.id.other_app_close_button, "field '_OtherAppCloseButton'", ImageView.class);
    view2131296400 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    target._OtherAppTitleText = Utils.findRequiredViewAsType(source, R.id.other_app_title_text, "field '_OtherAppTitleText'", TextView.class);
    target._OtherAppEnglishText = Utils.findRequiredViewAsType(source, R.id.other_app_littlefox_eng_text, "field '_OtherAppEnglishText'", TextView.class);
    target._OtherAppSongText = Utils.findRequiredViewAsType(source, R.id.other_app_littlefox_song_text, "field '_OtherAppSongText'", TextView.class);
    target._OtherAppStorybookText = Utils.findRequiredViewAsType(source, R.id.other_app_littlefox_storybook_text, "field '_OtherAppStorybookText'", TextView.class);
    target._OtherAppChineseText = Utils.findRequiredViewAsType(source, R.id.other_app_littlefox_ch_text, "field '_OtherAppChineseText'", TextView.class);
    target._CaptionLayout = Utils.findRequiredViewAsType(source, R.id.player_caption_layout, "field '_CaptionLayout'", ScalableLayout.class);
    target._CaptionTitleText = Utils.findRequiredViewAsType(source, R.id.player_caption_title, "field '_CaptionTitleText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_ch, "method 'onPlayerButtonClick'");
    view2131296305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_eng, "method 'onPlayerButtonClick'");
    view2131296306 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_song, "method 'onPlayerButtonClick'");
    view2131296307 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_rocket_girl, "method 'onPlayerButtonClick'");
    view2131296308 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPlayerButtonClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    PlayerActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._ProgressMediaPlayer = null;
    target._BackgroundDiscoverImage = null;
    target._LoadingLayout = null;
    target._TopViewLayout = null;
    target._TopTitleText = null;
    target._TopCaptionSettingButton = null;
    target._TopCloseButton = null;
    target._BottomViewLayout = null;
    target._CurrentPlayTimeText = null;
    target._ThumbSeekbar = null;
    target._RemainPlayTimeText = null;
    target._LockButton = null;
    target._PlayButtonLayout = null;
    target._PrevButton = null;
    target._PlayButton = null;
    target._NextButton = null;
    target._BaseSubTitleLayout = null;
    target._SubtitleCloseButton = null;
    target._SubTitleNormalButton = null;
    target._SubTitleNormalIcon = null;
    target._SubTitleNormalText = null;
    target._SubTitleUseButton = null;
    target._SubTitleUseIcon = null;
    target._SubTitleUseText = null;
    target._BasePlayOtherAppEndLayout = null;
    target._OtherAppCloseButton = null;
    target._OtherAppTitleText = null;
    target._OtherAppEnglishText = null;
    target._OtherAppSongText = null;
    target._OtherAppStorybookText = null;
    target._OtherAppChineseText = null;
    target._CaptionLayout = null;
    target._CaptionTitleText = null;

    view2131296434.setOnClickListener(null);
    view2131296434 = null;
    view2131296424.setOnClickListener(null);
    view2131296424 = null;
    view2131296430.setOnClickListener(null);
    view2131296430 = null;
    view2131296428.setOnClickListener(null);
    view2131296428 = null;
    view2131296427.setOnClickListener(null);
    view2131296427 = null;
    view2131296499.setOnClickListener(null);
    view2131296499 = null;
    view2131296501.setOnClickListener(null);
    view2131296501 = null;
    view2131296505.setOnClickListener(null);
    view2131296505 = null;
    view2131296400.setOnClickListener(null);
    view2131296400 = null;
    view2131296305.setOnClickListener(null);
    view2131296305 = null;
    view2131296306.setOnClickListener(null);
    view2131296306 = null;
    view2131296307.setOnClickListener(null);
    view2131296307 = null;
    view2131296308.setOnClickListener(null);
    view2131296308 = null;
  }
}
