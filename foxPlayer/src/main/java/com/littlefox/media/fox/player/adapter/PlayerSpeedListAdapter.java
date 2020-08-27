package com.littlefox.media.fox.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.adapter.listener.PlayerEventListener;
import com.littlefox.media.fox.player.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerSpeedListAdapter extends RecyclerView.Adapter<PlayerSpeedListAdapter.ViewHolder>
{
    private Context mContext;
    private PlayerEventListener mPlayerEventListener;
    private String[] mSpeedTextList = null;
    private int mCurrentSpeedIndex = 0;
    public PlayerSpeedListAdapter(Context context, int currentSpeedIndex)
    {
        mContext = context;
        mCurrentSpeedIndex = currentSpeedIndex;
        mSpeedTextList = getSpeedTextList();
    }

    public void setPlayerEventListener(PlayerEventListener listener)
    {
        mPlayerEventListener= listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.player_speed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(mCurrentSpeedIndex == position)
        {
            holder._SelectIcon.setImageResource(R.drawable.player__speed_select);
        }
        else
        {
            holder._SelectIcon.setImageResource(R.drawable.player__speed_select_default);
        }

        holder._SelectSpeedText.setText(mSpeedTextList[position]);

        holder._ItemBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentSpeedIndex = position;
                notifyDataSetChanged();
                mPlayerEventListener.onClickSpeedIndex(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return mSpeedTextList.length;
    }

    private String[] getSpeedTextList()
    {
        return mContext.getResources().getStringArray(R.array.text_list_speed);
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id._itemBaseLayout)
        ScalableLayout _ItemBaseLayout;

        @BindView(R.id._selectIcon)
        ImageView _SelectIcon;

        @BindView(R.id._selectSpeedText)
        TextView _SelectSpeedText;

        public ViewHolder(@NonNull View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            initFont();
        }

        private void initFont()
        {
            _SelectSpeedText.setTypeface(Font.getInstance(mContext).getRobotoRegular());
        }
    }
}

