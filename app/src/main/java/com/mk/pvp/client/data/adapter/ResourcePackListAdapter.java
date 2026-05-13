package com.mk.pvp.client.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.ResourcePack;

import java.util.ArrayList;
import java.util.List;

public class ResourcePackListAdapter extends RecyclerView.Adapter<ResourcePackListAdapter.ViewHolder> {
    private Context context;
    private List<ResourcePack> resourcePacks = new ArrayList<>();
    private OnResourcePackActionListener listener;

    public interface OnResourcePackActionListener {
        void onSelect(ResourcePack pack);
        void onDelete(ResourcePack pack);
    }

    public ResourcePackListAdapter(Context context, OnResourcePackActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setResourcePacks(List<ResourcePack> resourcePacks) {
        this.resourcePacks = resourcePacks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_resource_pack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(resourcePacks.get(position));
    }

    @Override
    public int getItemCount() {
        return resourcePacks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView previewView;
        TextView nameText;
        TextView sizeText;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            previewView = itemView.findViewById(R.id.pack_preview);
            nameText = itemView.findViewById(R.id.pack_name);
            sizeText = itemView.findViewById(R.id.pack_size);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(ResourcePack pack) {
            nameText.setText(pack.getName());
            sizeText.setText(pack.getFormattedSize());

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(pack);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSelect(pack);
                }
            });
        }
    }
}