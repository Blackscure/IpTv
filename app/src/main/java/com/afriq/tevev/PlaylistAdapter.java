package com.afriq.tevev;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ItemHolder> {
    private List<M3UItem> mItem = new ArrayList<M3UItem>();
    private Context mContext;
    private LayoutInflater mInflater;

    public PlaylistAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        de.hdodenhof.circleimageview.CircleImageView circleImageView;
        LinearLayout linearLayout;
        RelativeLayout relativeLayout;

        public ItemHolder(View view) {
            super(view);
            relativeLayout = view.findViewById(R.id.view);
            name = view.findViewById(R.id.channelName);
            circleImageView = view.findViewById(R.id.channelLogo);
            linearLayout = view.findViewById(R.id.textLayout);
        }

        public void update(final M3UItem item) {

            name.setText(item.getItemName());
            Glide.with(mContext).load(item.getItemIcon()).into(circleImageView);
            if (item.getItemIcon().isEmpty()) {
                circleImageView.setImageResource(R.drawable.live_tv);
            } else {
                Glide.with(mContext).load(item.getItemIcon()).into(circleImageView);
            }
            Log.i("URL", item.getItemUrl());
            Log.i("TIME", item.getItemDuration());
            Log.i("ICON", item.getItemIcon());
            Log.i("NAME", item.getItemName());
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String channelUrl = "http://102.69.224.246:5999";
                        Request request = new Request.Builder()
                                .url(channelUrl)
                                .build();
                        OkHttpClient client = new OkHttpClient();
                        Response response = client.newCall(request).execute();
                        final String res = response.body().string();
                        Log.i("SERVER RESPONSE", res);

                    } catch (Exception e) {
                        Log.i("----SERVER ERROR-----", e.getMessage());
                    }

                }
            });
            thread.start();
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,PlayerActivity.class);
                    intent.putExtra("iconUrl", item.getItemIcon());
                    intent.putExtra("itemUrl", item.getItemUrl());
                    intent.putExtra("name", item.getItemName());
                    intent.putExtra("tvChannel", item.getItemName());

                    if (item.getItemDuration().contains("news")) {
                        intent.putExtra("actionName", "news channel");
                    }else{
                        intent.putExtra("actionName", "channel");

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    name.getContext().startActivity(intent);

                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,PlayerActivity.class);
                    intent.putExtra("iconUrl", item.getItemIcon());
                    intent.putExtra("itemUrl", item.getItemUrl());
                    intent.putExtra("name", item.getItemName());
                    intent.putExtra("tvChannel", item.getItemName());

                    if (item.getItemDuration().contains("news")) {
                        intent.putExtra("actionName", "news channel");
                    }else{
                        intent.putExtra("actionName", "channel");

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    name.getContext().startActivity(intent);

                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,PlayerActivity.class);
                    intent.putExtra("iconUrl", item.getItemIcon());
                    intent.putExtra("itemUrl", item.getItemUrl());
                    intent.putExtra("tvChannel", item.getItemName());

                    if (item.getItemDuration().contains("news")) {
                        intent.putExtra("actionName", "news channel");
                    }else{
                        intent.putExtra("actionName", "channel");

                    }
                    intent.putExtra("name", item.getItemName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    name.getContext().startActivity(intent);


                }
            });
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,PlayerActivity.class);
                    intent.putExtra("iconUrl", item.getItemIcon());
                    intent.putExtra("itemUrl", item.getItemUrl());
                    intent.putExtra("name", item.getItemName());
                    intent.putExtra("tvChannel", item.getItemName());

                    if (item.getItemDuration().contains("news")) {
                        intent.putExtra("actionName", "news channel");
                    }else{
                        intent.putExtra("actionName", "channel");

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    name.getContext().startActivity(intent);

                }
            });
        }


    }


    @NotNull
    @Override
    public ItemHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        final View sView = mInflater.inflate(R.layout.dialog_movie_list, parent, false);
        return new ItemHolder(sView);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        final M3UItem item = mItem.get(position);
        if (item != null) {
            holder.update(item);
        }
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }


    public void update(List<M3UItem> _list) {
        this.mItem = _list;
        notifyDataSetChanged();
    }
}
