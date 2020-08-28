package com.littlefox.media.fox.player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.littlefox.logmonitor.Log;
import com.littlefox.media.fox.player.R;
import com.littlefox.media.fox.player.adapter.listener.PlayerEventListener;
import com.littlefox.media.fox.player.common.Font;
import com.ssomai.android.scalablelayout.ScalableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder>
{
    private Context mContext;
    private int mCurrentPosition = 0;
    private String[] mThumbnailList;
    private String[] mTitleList;
    private int maxItemCount = 0;
    private PlayerEventListener mPlayerEventListener;
    public PlayerListAdapter(Context context, int currentPosition, String[] thumbnailList, String[] titleList)
    {
        mContext            = context;
        mCurrentPosition    = currentPosition;
        mThumbnailList      = thumbnailList;
        mTitleList          = titleList;
        maxItemCount        = mTitleList.length;
    }

    public void setCurrentPlayPosition(int index)
    {
        mCurrentPosition = index;
        notifyDataSetChanged();
    }

    public void setOnPlayEventListener(PlayerEventListener listener)
    {
        mPlayerEventListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.player_list_item_landscape, parent,  false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Glide.with(mContext).load(mThumbnailList[position]).transition(withCrossFade()).into(holder._ItemTitleImage);
        holder._ItemTitleText.setText(mTitleList[position]);

        if(mCurrentPosition == position)
        {
            holder._ItemBackground.setImageResource(R.drawable.box_yellow);
        }
        else
        {
            holder._ItemBackground.setImageResource(R.drawable.box);
        }

        holder._ItemBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mCurrentPosition = position;
                Log.f("mCurrentPlayIndex : "+ mCurrentPosition);
                mPlayerEventListener.onClickPlayItem(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return maxItemCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id._itemBaseLayout)
        ScalableLayout _ItemBaseLayout;

        @BindView(R.id._itemBackground)
        ImageView _ItemBackground;

        @BindView(R.id._itemTitleImage)
        ImageView _ItemTitleImage;

        @BindView(R.id._itemTitleText)
        TextView _ItemTitleText;

        public ViewHolder(@NonNull View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            initFont();
        }
        
        private void initFont()
        {
            _ItemTitleText.setTypeface(Font.getInstance(mContext).getRobotoRegular());
        }
    }
}
