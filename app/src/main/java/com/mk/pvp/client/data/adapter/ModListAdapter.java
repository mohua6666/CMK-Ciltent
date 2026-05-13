package com.mk.pvp.client.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.ModInfo;

import java.util.ArrayList;
import java.util.List;

public class ModListAdapter extends RecyclerView.Adapter<ModListAdapter.ViewHolder> {
    private Context context;
    private List<ModInfo> mods = new ArrayList<>();
    private OnModActionListener listener;

    public interface OnModActionListener {
        void onToggle(ModInfo mod, boolean enable);
        void onDelete(ModInfo mod);
    }

    public ModListAdapter(Context context, OnModActionListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void setMods(List<ModInfo> mods) {
        this.mods = mods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mod, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mods.get(position));
    }

    @Override
    public int getItemCount() {
        return mods.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView sizeText;
        Switch enableSwitch;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.mod_name);
            sizeText = itemView.findViewById(R.id.mod_size);
            enableSwitch = itemView.findViewById(R.id.enable_switch);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(ModInfo mod) {
            nameText.setText(mod.getName());
            sizeText.setText(mod.getFormattedSize());
            enableSwitch.setChecked(mod.isEnabled());

            enableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (listener != null) {
                    listener.onToggle(mod, isChecked);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(mod);
                }
            });
        }
    }
}