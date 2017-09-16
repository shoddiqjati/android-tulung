package com.onevest.dev.tulung.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.main.activity.DetailsActivity;
import com.onevest.dev.tulung.models.Post;
import com.onevest.dev.tulung.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyHolder> {

    private Context context;
    private List<Post> postList;
    Post post;
    private static final String TAG = TimelineAdapter.class.getSimpleName();

    public TimelineAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.content_timeline, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final String name = postList.get(position).getName();
        final String desc = postList.get(position).getDesc();
        final String address = postList.get(position).getAddress();
        final String category = postList.get(position).getCategory();
        final String latitude = postList.get(position).getLatitude();
        final String longitude = postList.get(position).getLongitude();
        final String people = postList.get(position).getPeople();
        final String status = postList.get(position).getStatus();
        final String uuid = postList.get(position).getUuid();
        holder.tvName.setText(name);
        holder.tvContent.setText(desc);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constants.POST_NAME, name);
                intent.putExtra(Constants.POST_DESCRIPTION, desc);
                intent.putExtra(Constants.POST_ADDRESS, address);
                intent.putExtra(Constants.POST_CATEGORY, category);
                intent.putExtra(Constants.POST_LAT, latitude);
                intent.putExtra(Constants.POST_LONG, longitude);
                intent.putExtra(Constants.POST_PEOPLE, people);
                intent.putExtra(Constants.POST_STATUS, status);
                intent.putExtra(Constants.POST_UUID, uuid);
                context.startActivity(intent);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "[TULUNG]\n" + name + "\n\n" + desc + "\n\n" + address;
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[HELP]");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                context.startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (postList == null) {
            return 0;
        } else {
            return postList.size();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timeline_account_img)
        ImageView avatar;
        @BindView(R.id.timeline_share)
        ImageView share;

        @BindView(R.id.timeline_name)
        TextView tvName;
        @BindView(R.id.timeline_description)
        TextView tvContent;

        @BindView(R.id.card_view)
        CardView cardView;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
