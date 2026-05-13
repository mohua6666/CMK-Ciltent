package com.mk.pvp.client.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.adapter.SkinAdapter;
import com.mk.pvp.client.data.model.Skin;
import com.mk.pvp.client.data.repository.FileRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private FloatingActionButton addButton;
    private ImageView previewImage;
    private TextView previewName;

    private FileRepository fileRepository;
    private SkinAdapter adapter;
    private List<Skin> skins = new ArrayList<>();

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    importSkin(uri);
                }
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadSkins();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        emptyText = findViewById(R.id.empty_text);
        addButton = findViewById(R.id.add_button);
        previewImage = findViewById(R.id.preview_image);
        previewName = findViewById(R.id.preview_name);

        addButton.setOnClickListener(v -> openFilePicker());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.skin);
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new SkinAdapter(this, skins, new SkinAdapter.OnSkinActionListener() {
            @Override
            public void onSkinClick(Skin skin) {
                selectSkin(skin);
            }

            @Override
            public void onDeleteSkin(Skin skin) {
                confirmDeleteSkin(skin);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void loadSkins() {
        skins.clear();
        skins.addAll(fileRepository.getSkins());
        adapter.notifyDataSetChanged();

        if (skins.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/png");
        filePickerLauncher.launch(intent);
    }

    private void importSkin(Uri uri) {
        Toast.makeText(this, "正在导入皮肤...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                File cacheDir = getCacheDir();
                String fileName = "temp_skin.png";
                File tempFile = new File(cacheDir, fileName);

                try (java.io.InputStream is = getContentResolver().openInputStream(uri);
                     java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }

                String skinName = "custom_skin_" + System.currentTimeMillis();
                boolean success = fileRepository.saveSkinImage(tempFile, skinName);
                tempFile.delete();

                Skin skin = new Skin();
                skin.setName(skinName);
                skin.setPath(fileRepository.getSkinsPath() + "/" + skinName + ".png");
                skin.setCustom(true);
                fileRepository.addSkin(skin);

                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "皮肤导入成功", Toast.LENGTH_SHORT).show();
                        loadSkins();
                    } else {
                        Toast.makeText(this, "皮肤导入失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, "导入失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void selectSkin(Skin skin) {
        previewName.setText(skin.getName());
        Toast.makeText(this, "已选择皮肤: " + skin.getName(), Toast.LENGTH_SHORT).show();
    }

    private void confirmDeleteSkin(Skin skin) {
        if (!skin.isCustom()) {
            Toast.makeText(this, "无法删除默认皮肤", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
            .setTitle("删除皮肤")
            .setMessage("确定要删除皮肤 " + skin.getName() + " 吗？")
            .setPositiveButton("删除", (dialog, which) -> {
                fileRepository.removeSkin(skin.getId());
                File skinFile = new File(skin.getPath());
                skinFile.delete();
                skins.remove(skin);
                adapter.notifyDataSetChanged();
                if (skins.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, "皮肤已删除", Toast.LENGTH_SHORT).show();
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
