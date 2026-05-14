package com.mk.pvp.client.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.KeyMapping;
import com.mk.pvp.client.data.repository.FileRepository;

import java.util.ArrayList;
import java.util.List;

public class KeyMappingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button saveButton;
    private Button resetButton;

    private FileRepository fileRepository;
    private List<KeyMapping> keyMappings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_mapping);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        loadKeyMappings();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        saveButton.setOnClickListener(v -> saveKeyMappings());
        resetButton.setOnClickListener(v -> resetKeyMappings());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.key_mapping);
        }
    }

    private void loadKeyMappings() {
        keyMappings = fileRepository.getSavedKeyMappings();
    }

    private void saveKeyMappings() {
        boolean success = fileRepository.saveKeyMappings(keyMappings);
        if (success) {
            Toast.makeText(this, "按键映射已保存", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetKeyMappings() {
        keyMappings.clear();
        fileRepository.saveKeyMappings(keyMappings);
        Toast.makeText(this, "已重置按键映射", Toast.LENGTH_SHORT).show();
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