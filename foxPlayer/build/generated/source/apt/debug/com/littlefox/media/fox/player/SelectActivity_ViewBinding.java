// Generated code from Butter Knife. Do not modify!
package com.littlefox.media.fox.player;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.littlefox.library.view.text.SeparateTextView;
import com.ssomai.android.scalablelayout.ScalableLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectActivity_ViewBinding implements Unbinder {
  private SelectActivity target;

  private View view2131296303;

  private View view2131296301;

  @UiThread
  public SelectActivity_ViewBinding(SelectActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SelectActivity_ViewBinding(final SelectActivity target, View source) {
    this.target = target;

    View view;
    target._RootLayout = Utils.findRequiredViewAsType(source, R.id.root_layout, "field '_RootLayout'", LinearLayout.class);
    target._MarginLayout = Utils.findRequiredViewAsType(source, R.id.margin_layout, "field '_MarginLayout'", ScalableLayout.class);
    target._TitleText = Utils.findRequiredViewAsType(source, R.id.main_title_text, "field '_TitleText'", SeparateTextView.class);
    target._ChoiceInformationText = Utils.findRequiredViewAsType(source, R.id.choice_title_text, "field '_ChoiceInformationText'", SeparateTextView.class);
    target._ButtonEnglishText = Utils.findRequiredViewAsType(source, R.id.text_littlefox_eng, "field '_ButtonEnglishText'", TextView.class);
    target._ButtonChineseText = Utils.findRequiredViewAsType(source, R.id.text_littlefox_ch, "field '_ButtonChineseText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.button_littlefox_eng, "method 'OnSelectClick'");
    view2131296303 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_littlefox_ch, "method 'OnSelectClick'");
    view2131296301 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.OnSelectClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._RootLayout = null;
    target._MarginLayout = null;
    target._TitleText = null;
    target._ChoiceInformationText = null;
    target._ButtonEnglishText = null;
    target._ButtonChineseText = null;

    view2131296303.setOnClickListener(null);
    view2131296303 = null;
    view2131296301.setOnClickListener(null);
    view2131296301 = null;
  }
}
