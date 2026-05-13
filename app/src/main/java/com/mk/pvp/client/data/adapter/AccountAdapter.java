package com.mk.pvp.client.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private Context context;
    private List<Account> accounts;
    private OnAccountActionListener listener;

    public interface OnAccountActionListener {
        void onAccountClick(Account account);
        void onDeleteAccount(Account account);
    }

    public AccountAdapter(Context context, List<Account> accounts, OnAccountActionListener listener) {
        this.context = context;
        this.accounts = accounts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarView;
        TextView nameText;
        TextView modeText;
        RadioButton selectedRadio;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.account_avatar);
            nameText = itemView.findViewById(R.id.account_name);
            modeText = itemView.findViewById(R.id.account_mode);
            selectedRadio = itemView.findViewById(R.id.selected_radio);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        void bind(Account account) {
            nameText.setText(account.getUsername());
            modeText.setText(account.isOnline() ? "在线模式" : "离线模式");
            selectedRadio.setChecked(account.isSelected());

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteAccount(account);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAccountClick(account);
                }
            });
        }
    }
}
