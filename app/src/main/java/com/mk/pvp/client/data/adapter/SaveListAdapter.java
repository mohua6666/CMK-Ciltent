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
import com.mk.pvp.client.data.model.GameSave;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SaveListAdapter extends RecyclerView.Adapter<SaveListAdapter.ViewHolder> {
    private Context context;
    private List<GameSave> saves = new ArrayList<>();
    private OnSaveActionListener listener;

    public interface OnSaveActionListener {
        void onSelect(GameSave save);
        void onBackup(GameSave save);
        void onDelete(GameSave save);
    }

    public SaveListAdapter(Context context, OnSaveActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setSaves(List<GameSave> saves) {
        this.saves = saves;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_save, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(saves.get(position));
    }

    @Override
    public int getItemCount() {
        return saves.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconView;
        TextView nameText;
        TextView sizeText;
        TextView dateText;
        ImageButton backupButton;
        ImageButton menuButton;

        ViewHolder(View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.save_icon);
            nameText = itemView.findViewById(R.id.save_name);
            sizeText = itemView.findViewById(R.id.save_size);
            dateText = itemView.findViewById(R.id.save_date);
            backupButton = itemView.findViewById(R.id.backup_button);
            menuButton = itemView.findViewById(R.id.menu_button);
        }

        void bind(GameSave save) {
            nameText.setText(save.getName());
            sizeText.setText(save.getFormattedSize());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dateText.setText(sdf.format(new Date(save.getLastModified())));

            backupButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBackup(save);
                }
            });

            menuButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(save);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSelect(save);
                }
            });
        }
    }
}