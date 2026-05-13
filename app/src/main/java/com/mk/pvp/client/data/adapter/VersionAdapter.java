package com.mk.pvp.client.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.GameVersion;

import java.util.List;

public class VersionAdapter extends RecyclerView.Adapter<VersionAdapter.ViewHolder> {
    private List<GameVersion> versions;
    private OnVersionClickListener listener;
    private String selectedVersionId;

    public interface OnVersionClickListener {
        void onVersionClick(GameVersion version);
    }

    public VersionAdapter(List<GameVersion> versions, OnVersionClickListener listener) {
        this.versions = versions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_version, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(versions.get(position));
    }

    @Override
    public int getItemCount() {
        return versions.size();
    }

    public void setSelectedVersionId(String versionId) {
        this.selectedVersionId = versionId;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView versionName;
        TextView versionSize;

        ViewHolder(View itemView) {
            super(itemView);
            versionName = itemView.findViewById(R.id.version_name);
            versionSize = itemView.findViewById(R.id.version_size);
        }

        void bind(GameVersion version) {
            versionName.setText(version.getId());
            versionSize.setText(version.getFormattedSize());

            if (version.getId().equals(selectedVersionId)) {
                itemView.setBackgroundResource(R.drawable.circle_background);
            } else {
                itemView.setBackgroundColor(0);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVersionClick(version);
                }
            });
        }
    }
}