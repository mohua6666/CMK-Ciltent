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
import com.mk.pvp.client.data.adapter.PVPClientAdapter;
import com.mk.pvp.client.data.model.PVPClient;
import com.mk.pvp.client.data.repository.FileRepository;

import java.util.List;

public class PVPClientActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyText;

    private FileRepository fileRepository;
    private PVPClientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp_client);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadClients();
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
            getSupportActionBar().setTitle(R.string.pvp_clients);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PVPClientAdapter(this, new PVPClientAdapter.OnPVPClientActionListener() {
            @Override
            public void onDownloadClick(PVPClient client) {
                downloadClient(client);
            }

            @Override
            public void onInstallClick(PVPClient client) {
                installClient(client);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadClients() {
        progressBar.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.GONE);

        new Thread(() -> {
            List<PVPClient> clients = fileRepository.getDefaultPVPClients();

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                adapter.setClients(clients);

                if (clients.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }

    private void downloadClient(PVPClient client) {
        Toast.makeText(this, "正在下载 " + client.getName() + "...", Toast.LENGTH_LONG).show();
    }

    private void installClient(PVPClient client) {
        client.setInstalled(true);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, client.getName() + " 已安装", Toast.LENGTH_SHORT).show();
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
