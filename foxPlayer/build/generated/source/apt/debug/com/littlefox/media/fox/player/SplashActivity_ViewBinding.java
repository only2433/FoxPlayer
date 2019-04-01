// Generated code from Butter Knife. Do not modify!
package com.littlefox.media.fox.player;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.littlefox.library.view.dialog.ProgressWheel;
import com.littlefox.library.view.text.SeparateTextView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashActivity_ViewBinding implements Unbinder {
  private SplashActivity target;

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target, View source) {
    this.target = target;

    target._TitleText = Utils.findRequiredViewAsType(source, R.id.intro_title_text, "field '_TitleText'", SeparateTextView.class);
    target._ProgressView = Utils.findRequiredViewAsType(source, R.id.intro_loading_layout, "field '_ProgressView'", ProgressWheel.class);
    target._BaseLayout = Utils.findRequiredViewAsType(source, R.id.root_base_layout, "field '_BaseLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._TitleText = null;
    target._ProgressView = null;
    target._BaseLayout = null;
  }
}
