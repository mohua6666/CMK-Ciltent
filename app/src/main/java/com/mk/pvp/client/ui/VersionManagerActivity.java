package com.mk.pvp.client.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.adapter.VersionAdapter;
import com.mk.pvp.client.data.model.GameVersion;
import com.mk.pvp.client.data.repository.FileRepository;

import java.util.List;

public class VersionManagerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private Button downloadButton;

    private FileRepository fileRepository;
    private VersionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_manager);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadVersions();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        emptyText = findViewById(R.id.empty_text);
        downloadButton = findViewById(R.id.download_button);

        downloadButton.setOnClickListener(v -> showDownloadDialog());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.version_manager);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VersionAdapter(null, version -> {
            Toast.makeText(this, "已选择版本: " + version.getId(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadVersions() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        new Thread(() -> {
            List<GameVersion> versions = fileRepository.getInstalledVersions();

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter = new VersionAdapter(versions, version -> {
                    Toast.makeText(this, "已选择版本: " + version.getId(), Toast.LENGTH_SHORT).show();
                });
                recyclerView.setAdapter(adapter);

                if (versions.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    private void showDownloadDialog() {
        Toast.makeText(this, "正在获取可用版本列表...", Toast.LENGTH_SHORT).show();
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