package com.broscr.iptvplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.databinding.ChannelListRowBinding;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.utils.ChannelOnClick;
import com.bumptech.glide.Glide;

import java.util.List;

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelListAdapter.ChannelListVHolder> {

    private final Context context;
    private final List<Channel> channelList;
    private final ChannelOnClick channelOnClick;

    public ChannelListAdapter(Context context, List<Channel> channelList, ChannelOnClick channelOnClick) {
        this.context = context;
        this.channelList = channelList;
        this.channelOnClick = channelOnClick;
    }

    @NonNull
    @Override
    public ChannelListVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChannelListVHolder(ChannelListRowBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelListVHolder holder, int position) {
        Channel channel = channelList.get(position);

        if (channel != null) {
            holder.binding.channelListRowId.setText(String.valueOf(position + 1));
            holder.binding.channelListRowName.setText(channel.getChannelName());

            if (channel.getChannelImg() != null && !channel.getChannelImg().equals("")) {
                Glide.with(context).load(channel.getChannelImg())
                        .override(60, 60)
                        .into(holder.binding.channelListRowImg);
            } else {
                holder.binding.channelListRowImg
                        .setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                                R.drawable.ic_image, null));
            }
            holder.binder(channelOnClick, channel);
        }
    }

    @Override
    public int getItemCount() {
        if (channelList != null && channelList.size() > 0) {
            return channelList.size();
        } else {
            return 0;
        }
    }

    class ChannelListVHolder extends RecyclerView.ViewHolder {

        private final ChannelListRowBinding binding;

        public ChannelListVHolder(@NonNull ChannelListRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void binder(ChannelOnClick channelOnClick, Channel channel) {
            itemView.setOnClickListener(view -> channelOnClick.channelOnClick(channel));
        }
    }
}
