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
import com.mk.pvp.client.data.adapter.SaveListAdapter;
import com.mk.pvp.client.data.model.GameSave;
import com.mk.pvp.client.data.repository.FileRepository;

import java.util.List;

public class SaveManagerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;

    private FileRepository fileRepository;
    private SaveListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_manager);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadSaves();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        emptyText = findViewById(R.id.empty_text);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.save_manager);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SaveListAdapter(this, new SaveListAdapter.OnSaveActionListener() {
            @Override
            public void onSelect(GameSave save) {
                Toast.makeText(SaveManagerActivity.this, "已选择存档: " + save.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackup(GameSave save) {
                boolean success = fileRepository.backupSave(save, fileRepository.getExternalStoragePath() + "/backups");
                if (success) {
                    Toast.makeText(SaveManagerActivity.this, "存档备份成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SaveManagerActivity.this, "存档备份失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDelete(GameSave save) {
                Toast.makeText(SaveManagerActivity.this, "删除功能: " + save.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadSaves() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        new Thread(() -> {
            List<GameSave> saves = fileRepository.getGameSaves();

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter.setSaves(saves);

                if (saves.isEmpty()) {
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