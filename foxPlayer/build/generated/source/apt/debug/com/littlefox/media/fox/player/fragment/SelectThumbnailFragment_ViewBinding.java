// Generated code from Butter Knife. Do not modify!
package com.littlefox.media.fox.player.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.littlefox.media.fox.player.R;
import com.ssomai.android.scalablelayout.ScalableLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectThumbnailFragment_ViewBinding implements Unbinder {
  private SelectThumbnailFragment target;

  private View view2131296304;

  private View view2131296302;

  private View view2131296309;

  @UiThread
  public SelectThumbnailFragment_ViewBinding(final SelectThumbnailFragment target, View source) {
    this.target = target;

    View view;
    target._MovieTitleText = Utils.findRequiredViewAsType(source, R.id.main_title_text, "field '_MovieTitleText'", TextView.class);
    target._MovieDateText = Utils.findRequiredViewAsType(source, R.id.main_day_text, "field '_MovieDateText'", TextView.class);
    target._ThumbnailImage = Utils.findRequiredViewAsType(source, R.id.thumbnail_image, "field '_ThumbnailImage'", ImageView.class);
    target._IndicateEnglishLayout = Utils.findRequiredViewAsType(source, R.id.littlefox_eng_move_layout, "field '_IndicateEnglishLayout'", ScalableLayout.class);
    target._IndicateChineseLayout = Utils.findRequiredViewAsType(source, R.id.littlefox_ch_move_layout, "field '_IndicateChineseLayout'", ScalableLayout.class);
    target._IndicateEnglishText = Utils.findRequiredViewAsType(source, R.id.button_eng_text, "field '_IndicateEnglishText'", TextView.class);
    target._IndicateChineseText = Utils.findRequiredViewAsType(source, R.id.button_ch_text, "field '_IndicateChineseText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.button_littlefox_eng_move, "method 'onSelectClick'");
    view2131296304 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_littlefox_ch_move, "method 'onSelectClick'");
    view2131296302 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSelectClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_play_icon, "method 'onSelectClick'");
    view2131296309 = view;
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
    SelectThumbnailFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._MovieTitleText = null;
    target._MovieDateText = null;
    target._ThumbnailImage = null;
    target._IndicateEnglishLayout = null;
    target._IndicateChineseLayout = null;
    target._IndicateEnglishText = null;
    target._IndicateChineseText = null;

    view2131296304.setOnClickListener(null);
    view2131296304 = null;
    view2131296302.setOnClickListener(null);
    view2131296302 = null;
    view2131296309.setOnClickListener(null);
    view2131296309 = null;
  }
}
