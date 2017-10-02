package com.onevest.dev.tulung.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.onevest.dev.tulung.R;
import com.onevest.dev.tulung.models.Warning;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shoddiq Jati Premono on 17/09/2017.
 */

public class WarningAdapter extends RecyclerView.Adapter<WarningAdapter.MyHolder> {

    Context context;
    List<Warning> warningList;
    String phone;

    @Override
    public WarningAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.content_warning, parent, false);
        return new MyHolder(itemView);
    }

    public WarningAdapter(Context context, List<Warning> warningList) {
        this.context = context;
        this.warningList = warningList;
    }

    @Override
    public void onBindViewHolder(WarningAdapter.MyHolder holder, int position) {
        holder.tvName.setText(warningList.get(position).getName());
        holder.tvPhone.setText(warningList.get(position).getPhone());
        phone = warningList.get(position).getPhone();
        holder.call.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return warningList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.warning_name)
        TextView tvName;
        @BindView(R.id.warning_phone)
        TextView tvPhone;
        @BindView(R.id.warning_call)
        ImageView call;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
