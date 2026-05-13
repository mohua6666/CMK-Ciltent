package com.mk.pvp.client.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.PVPClient;

import java.util.ArrayList;
import java.util.List;

public class PVPClientAdapter extends RecyclerView.Adapter<PVPClientAdapter.ViewHolder> {
    private Context context;
    private List<PVPClient> clients = new ArrayList<>();
    private OnPVPClientActionListener listener;

    public interface OnPVPClientActionListener {
        void onDownloadClick(PVPClient client);
        void onInstallClick(PVPClient client);
    }

    public PVPClientAdapter(Context context, OnPVPClientActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setClients(List<PVPClient> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pvp_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(clients.get(position));
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView nameText;
        TextView descText;
        TextView sizeText;
        Button actionButton;
        View recommendedBadge;

        ViewHolder(View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.client_icon);
            nameText = itemView.findViewById(R.id.client_name);
            descText = itemView.findViewById(R.id.client_desc);
            sizeText = itemView.findViewById(R.id.client_size);
            actionButton = itemView.findViewById(R.id.action_button);
            recommendedBadge = itemView.findViewById(R.id.recommended_badge);
        }

        void bind(PVPClient client) {
            nameText.setText(client.getName());
            descText.setText(client.getDescription());
            sizeText.setText(client.getFormattedSize());

            if (client.isRecommended()) {
                recommendedBadge.setVisibility(View.VISIBLE);
            } else {
                recommendedBadge.setVisibility(View.GONE);
            }

            if (client.isInstalled()) {
                actionButton.setText("已安装");
                actionButton.setEnabled(false);
                actionButton.setBackgroundColor(context.getResources().getColor(R.color.success, null));
            } else {
                actionButton.setText("下载");
                actionButton.setEnabled(true);
                actionButton.setBackgroundColor(context.getResources().getColor(R.color.primary, null));
            }

            actionButton.setOnClickListener(v -> {
                if (listener != null) {
                    if (client.isInstalled()) {
                        listener.onInstallClick(client);
                    } else {
                        listener.onDownloadClick(client);
                    }
                }
            });
        }
    }
}
