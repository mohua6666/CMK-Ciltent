package com.mk.pvp.client.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.adapter.AccountAdapter;
import com.mk.pvp.client.data.model.Account;
import com.mk.pvp.client.data.repository.SettingsManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private FloatingActionButton addButton;

    private SettingsManager settingsManager;
    private AccountAdapter adapter;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        settingsManager = SettingsManager.getInstance(this);
        accounts = settingsManager.getAccounts();

        initViews();
        setupToolbar();
        setupRecyclerView();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        emptyText = findViewById(R.id.empty_text);
        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> showAddAccountDialog());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.accounts);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(this, accounts, new AccountAdapter.OnAccountActionListener() {
            @Override
            public void onAccountClick(Account account) {
                settingsManager.selectAccount(account.getId());
                adapter.notifyDataSetChanged();
                Toast.makeText(AccountActivity.this, "已选择: " + account.getUsername(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteAccount(Account account) {
                confirmDeleteAccount(account);
            }
        });
        recyclerView.setAdapter(adapter);

        if (accounts.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

    private void showAddAccountDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_account, null);
        EditText usernameInput = dialogView.findViewById(R.id.username_input);
        EditText passwordInput = dialogView.findViewById(R.id.password_input);
        RadioGroup modeGroup = dialogView.findViewById(R.id.mode_group);
        Button loginButton = dialogView.findViewById(R.id.login_button);

        new AlertDialog.Builder(this)
            .setTitle("添加账号")
            .setView(dialogView)
            .setNegativeButton("取消", null)
            .show();

        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            boolean isOnline = modeGroup.getCheckedRadioButtonId() == R.id.online_mode;

            if (username.isEmpty()) {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isOnline && password.isEmpty()) {
                Toast.makeText(this, "在线模式需要密码", Toast.LENGTH_SHORT).show();
                return;
            }

            Account account = new Account(username, password, isOnline);
            settingsManager.addAccount(account);
            settingsManager.selectAccount(account.getId());
            accounts.add(account);
            adapter.notifyDataSetChanged();
            emptyText.setVisibility(View.GONE);
            Toast.makeText(this, "账号已添加", Toast.LENGTH_SHORT).show();

            ((AlertDialog) ((View) v.getParent().getParent().getParent().getParent()).getParent()).dismiss();
        });
    }

    private void confirmDeleteAccount(Account account) {
        new AlertDialog.Builder(this)
            .setTitle("删除账号")
            .setMessage("确定要删除账号 " + account.getUsername() + " 吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                settingsManager.removeAccount(account.getId());
                accounts.remove(account);
                adapter.notifyDataSetChanged();
                if (accounts.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, "账号已删除", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("取消", null)
            .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
