package com.mk.pvp.client.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.adapter.ModListAdapter;
import com.mk.pvp.client.data.model.ModInfo;
import com.mk.pvp.client.data.repository.FileRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ModManagerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private FloatingActionButton addButton;

    private FileRepository fileRepository;
    private ModListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_manager);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadMods();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        emptyText = findViewById(R.id.empty_text);
        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> {
            Toast.makeText(this, "请通过文件管理器添加MOD文件", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.mod_manager);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModListAdapter(this, new ModListAdapter.OnModActionListener() {
            @Override
            public void onToggle(ModInfo mod, boolean enable) {
                fileRepository.toggleMod(mod, enable);
                loadMods();
            }

            @Override
            public void onDelete(ModInfo mod) {
                fileRepository.deleteMod(mod);
                loadMods();
                Toast.makeText(ModManagerActivity.this, "MOD已删除", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadMods() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        new Thread(() -> {
            List<ModInfo> mods = fileRepository.getInstalledMods();

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter.setMods(mods);

                if (mods.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                } else {
                    emptyText.setVisibility(View.GONE);
                }
            });
        }).start();
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