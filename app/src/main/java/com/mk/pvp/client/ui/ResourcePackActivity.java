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
import com.mk.pvp.client.data.adapter.ResourcePackListAdapter;
import com.mk.pvp.client.data.model.ResourcePack;
import com.mk.pvp.client.data.repository.FileRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ResourcePackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;
    private FloatingActionButton addButton;

    private FileRepository fileRepository;
    private ResourcePackListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_pack);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadResourcePacks();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        emptyText = findViewById(R.id.empty_text);
        addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> {
            Toast.makeText(this, "请通过文件管理器添加材质包", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.resource_pack);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ResourcePackListAdapter(this, new ResourcePackListAdapter.OnResourcePackActionListener() {
            @Override
            public void onSelect(ResourcePack pack) {
                Toast.makeText(ResourcePackActivity.this, "已选择材质包: " + pack.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(ResourcePack pack) {
                fileRepository.deleteResourcePack(pack);
                loadResourcePacks();
                Toast.makeText(ResourcePackActivity.this, "材质包已删除", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadResourcePacks() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        new Thread(() -> {
            List<ResourcePack> packs = fileRepository.getInstalledResourcePacks();

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter.setResourcePacks(packs);

                if (packs.isEmpty()) {
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