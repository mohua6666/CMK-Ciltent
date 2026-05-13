package com.mk.pvp.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mk.pvp.client.data.adapter.VersionAdapter;
import com.mk.pvp.client.data.model.GameVersion;
import com.mk.pvp.client.data.repository.FileRepository;
import com.mk.pvp.client.data.repository.SettingsManager;
import com.mk.pvp.client.ui.AccountActivity;
import com.mk.pvp.client.ui.KeyMappingActivity;
import com.mk.pvp.client.ui.ModManagerActivity;
import com.mk.pvp.client.ui.PVPClientActivity;
import com.mk.pvp.client.ui.ResourcePackActivity;
import com.mk.pvp.client.ui.SaveManagerActivity;
import com.mk.pvp.client.ui.SettingsActivity;
import com.mk.pvp.client.ui.SkinActivity;
import com.mk.pvp.client.ui.VersionManagerActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleText;
    private TextView subtitleText;
    private TextView versionCountText;
    private RecyclerView versionRecyclerView;
    private CardView modManagerCard;
    private CardView resourcePackCard;
    private CardView saveManagerCard;
    private CardView keyMappingCard;
    private CardView pvpClientCard;
    private CardView skinCard;
    private CardView accountCard;
    private CardView settingsCard;
    private View playButton;
    private ImageView accountAvatar;

    private SettingsManager settingsManager;
    private FileRepository fileRepository;
    private VersionAdapter versionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingsManager = SettingsManager.getInstance(this);
        fileRepository = FileRepository.getInstance(this);

        initViews();
        setupToolbar();
        setupListeners();
        loadVersions();
        updateAccountInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadVersions();
        updateAccountInfo();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        titleText = findViewById(R.id.title_text);
        subtitleText = findViewById(R.id.subtitle_text);
        versionCountText = findViewById(R.id.version_count_text);
        versionRecyclerView = findViewById(R.id.version_recycler_view);
        modManagerCard = findViewById(R.id.mod_manager_card);
        resourcePackCard = findViewById(R.id.resource_pack_card);
        saveManagerCard = findViewById(R.id.save_manager_card);
        keyMappingCard = findViewById(R.id.key_mapping_card);
        pvpClientCard = findViewById(R.id.pvp_client_card);
        skinCard = findViewById(R.id.skin_card);
        accountCard = findViewById(R.id.account_card);
        settingsCard = findViewById(R.id.settings_card);
        playButton = findViewById(R.id.play_button);
        accountAvatar = findViewById(R.id.account_avatar);

        versionRecyclerView.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupListeners() {
        modManagerCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ModManagerActivity.class));
        });

        resourcePackCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ResourcePackActivity.class));
        });

        saveManagerCard.setOnClickListener(v -> {
            startActivity(new Intent(this, SaveManagerActivity.class));
        });

        keyMappingCard.setOnClickListener(v -> {
            startActivity(new Intent(this, KeyMappingActivity.class));
        });

        pvpClientCard.setOnClickListener(v -> {
            startActivity(new Intent(this, PVPClientActivity.class));
        });

        skinCard.setOnClickListener(v -> {
            startActivity(new Intent(this, SkinActivity.class));
        });

        accountCard.setOnClickListener(v -> {
            startActivity(new Intent(this, AccountActivity.class));
        });

        settingsCard.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });

        playButton.setOnClickListener(v -> launchGame());

        accountAvatar.setOnClickListener(v -> {
            startActivity(new Intent(this, AccountActivity.class));
        });
    }

    private void loadVersions() {
        List<GameVersion> versions = fileRepository.getInstalledVersions();
        String selectedVersionId = settingsManager.getSelectedVersion();

        versionAdapter = new VersionAdapter(versions, version -> {
            settingsManager.setSelectedVersion(version.getId());
            versionAdapter.setSelectedVersionId(version.getId());
            versionCountText.setText("当前: " + version.getId());
        });

        versionRecyclerView.setAdapter(versionAdapter);

        if (selectedVersionId != null && !selectedVersionId.isEmpty()) {
            versionAdapter.setSelectedVersionId(selectedVersionId);
            versionCountText.setText("当前: " + selectedVersionId);
        } else {
            versionCountText.setText("共 " + versions.size() + " 个版本");
        }
    }

    private void updateAccountInfo() {
        if (settingsManager.getAccounts().isEmpty()) {
            subtitleText.setText("点击头像登录账号");
        } else {
            String username = settingsManager.getSelectedAccount().getUsername();
            subtitleText.setText("玩家: " + username);
        }
    }

    private void launchGame() {
        String selectedVersion = settingsManager.getSelectedVersion();
        if (selectedVersion == null || selectedVersion.isEmpty()) {
            Toast.makeText(this, "请先选择一个游戏版本", Toast.LENGTH_SHORT).show();
            new android.app.AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("尚未安装任何游戏版本，是否前往版本管理下载？")
                .setPositiveButton("前往", (dialog, which) -> {
                    startActivity(new Intent(this, VersionManagerActivity.class));
                })
                .setNegativeButton("取消", null)
                .show();
            return;
        }

        if (settingsManager.getAccounts().isEmpty()) {
            Toast.makeText(this, "请先添加账号", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "正在启动 MK PVP Client...", Toast.LENGTH_LONG).show();
    }
}
