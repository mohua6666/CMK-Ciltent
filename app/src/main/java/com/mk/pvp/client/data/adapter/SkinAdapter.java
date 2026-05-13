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
import com.mk.pvp.client.data.model.Skin;

import java.util.List;

public class SkinAdapter extends RecyclerView.Adapter<SkinAdapter.ViewHolder> {
    private Context context;
    private List<Skin> skins;
    private OnSkinActionListener listener;

    public interface OnSkinActionListener {
        void onSkinClick(Skin skin);
        void onDeleteSkin(Skin skin);
    }

    public SkinAdapter(Context context, List<Skin> skins, OnSkinActionListener listener) {
        this.context = context;
        this.skins = skins;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_skin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(skins.get(position));
    }

    @Override
    public int getItemCount() {
        return skins.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView skinView;
        TextView nameText;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            skinView = itemView.findViewById(R.id.skin_image);
            nameText = itemView.findViewById(R.id.skin_name);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(Skin skin) {
            nameText.setText(skin.getName());

            if (skin.isCustom()) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteSkin(skin);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSkinClick(skin);
                }
            });
        }
    }
}
