// Generated code from Butter Knife. Do not modify!
package com.littlefox.media.fox.player;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.littlefox.library.view.extra.SwipeDisableViewPager;
import com.littlefox.library.view.text.SeparateTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131296402;

  private View view2131296401;

  private View view2131296403;

  private View view2131296404;

  private View view2131296310;

  private View view2131296400;

  private View view2131296306;

  private View view2131296305;

  private View view2131296307;

  private View view2131296308;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target._OtherViewLayout = Utils.findRequiredViewAsType(source, R.id.include_other_app_layout, "field '_OtherViewLayout'", RelativeLayout.class);
    target._TitleText = Utils.findRequiredViewAsType(source, R.id.main_title_text, "field '_TitleText'", SeparateTextView.class);
    target._ViewPager = Utils.findRequiredViewAsType(source, R.id.player_viewpager, "field '_ViewPager'", SwipeDisableViewPager.class);
    target._OtherAppTitleText = Utils.findRequiredViewAsType(source, R.id.other_app_title_text, "field '_OtherAppTitleText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.other_app_littlefox_eng_text, "field '_OtherAppLittlefoxEngText' and method 'onSelectClick'");
    target._OtherAppLittlefoxEngText = Utils.castView(view, R.id.other_app_littlefox_eng_text, "field '_OtherAppLittlefoxEngText'", TextView.class);
    view2131296402 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_app_littlefox_ch_text, "field '_OtherAppLittlefoxChText' and method 'onSelectClick'");
    target._OtherAppLittlefoxChText = Utils.castView(view, R.id.other_app_littlefox_ch_text, "field '_OtherAppLittlefoxChText'", TextView.class);
    view2131296401 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_app_littlefox_song_text, "field '_OtherAppLittlefoxSongText' and method 'onSelectClick'");
    target._OtherAppLittlefoxSongText = Utils.castView(view, R.id.other_app_littlefox_song_text, "field '_OtherAppLittlefoxSongText'", TextView.class);
    view2131296403 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_app_littlefox_storybook_text, "field '_OtherAppLittlefoxStoryBookText' and method 'onSelectClick'");
    target._OtherAppLittlefoxStoryBookText = Utils.castView(view, R.id.other_app_littlefox_storybook_text, "field '_OtherAppLittlefoxStoryBookText'", TextView.class);
    view2131296404 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_setting, "method 'onSelectClick'");
    view2131296310 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.other_app_close_button, "method 'onSelectClick'");
    view2131296400 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_eng, "method 'onSelectClick'");
    view2131296306 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_ch, "method 'onSelectClick'");
    view2131296305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_littlefox_song, "method 'onSelectClick'");
    view2131296307 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_other_app_rocket_girl, "method 'onSelectClick'");
    view2131296308 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._OtherViewLayout = null;
    target._TitleText = null;
    target._ViewPager = null;
    target._OtherAppTitleText = null;
    target._OtherAppLittlefoxEngText = null;
    target._OtherAppLittlefoxChText = null;
    target._OtherAppLittlefoxSongText = null;
    target._OtherAppLittlefoxStoryBookText = null;

    view2131296402.setOnClickListener(null);
    view2131296402 = null;
    view2131296401.setOnClickListener(null);
    view2131296401 = null;
    view2131296403.setOnClickListener(null);
    view2131296403 = null;
    view2131296404.setOnClickListener(null);
    view2131296404 = null;
    view2131296310.setOnClickListener(null);
    view2131296310 = null;
    view2131296400.setOnClickListener(null);
    view2131296400 = null;
    view2131296306.setOnClickListener(null);
    view2131296306 = null;
    view2131296305.setOnClickListener(null);
    view2131296305 = null;
    view2131296307.setOnClickListener(null);
    view2131296307 = null;
    view2131296308.setOnClickListener(null);
    view2131296308 = null;
  }
}
