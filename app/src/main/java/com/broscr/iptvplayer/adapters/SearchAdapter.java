package com.broscr.iptvplayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.broscr.iptvplayer.databinding.SearchRowBinding;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.utils.ChannelOnClick;
import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVHolder> {

    private final Context context;
    private final List<Channel> channelList;
    private final ChannelOnClick channelOnClick;

    public SearchAdapter(Context context, List<Channel> channelList, ChannelOnClick channelOnClick) {
        this.context = context;
        this.channelList = channelList;
        this.channelOnClick = channelOnClick;
    }

    @NonNull
    @Override
    public SearchVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchVHolder(SearchRowBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVHolder holder, int position) {
        Channel channel = channelList.get(position);

        if (channel != null) {
            if (channel.getChannelImg() != null && !channel.getChannelImg().equals("")) {
                Glide.with(context).load(channel.getChannelImg()).override(200, 200)
                        .into(holder.binding.searchRowImage);
            }

            holder.binding.searchRowName.setText(channel.getChannelName());

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

    class SearchVHolder extends RecyclerView.ViewHolder {

        private SearchRowBinding binding;

        public SearchVHolder(@NonNull SearchRowBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void binder(ChannelOnClick channelOnClick, Channel channel) {
            itemView.setOnClickListener(view -> channelOnClick.channelOnClick(channel));
        }
    }
}
