package com.mk.pvp.client.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mk.pvp.client.R;
import com.mk.pvp.client.data.model.PVPSettings;
import com.mk.pvp.client.data.repository.SettingsManager;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Spinner rendererSpinner;
    private SeekBar memorySeekBar;
    private TextView memoryValueText;
    private CheckBox showFPSCheckbox;
    private CheckBox showPingCheckbox;
    private CheckBox autoJumpCheckbox;
    private CheckBox fastPlaceCheckbox;
    private CheckBox fastBreakCheckbox;
    private SeekBar reachSeekBar;
    private TextView reachValueText;
    private SeekBar clickSpeedSeekBar;
    private TextView clickSpeedValueText;
    private CheckBox keymacroCheckbox;
    private Spinner keymacroKeySpinner;
    private SeekBar keymacroDelaySeekBar;
    private TextView keymacroDelayValueText;
    private Button saveButton;

    private SettingsManager settingsManager;
    private PVPSettings pvpSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = SettingsManager.getInstance(this);
        pvpSettings = settingsManager.getPvpSettings();

        initViews();
        setupToolbar();
        loadSettings();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rendererSpinner = findViewById(R.id.renderer_spinner);
        memorySeekBar = findViewById(R.id.memory_seek_bar);
        memoryValueText = findViewById(R.id.memory_value_text);
        showFPSCheckbox = findViewById(R.id.show_fps_checkbox);
        showPingCheckbox = findViewById(R.id.show_ping_checkbox);
        autoJumpCheckbox = findViewById(R.id.auto_jump_checkbox);
        fastPlaceCheckbox = findViewById(R.id.fast_place_checkbox);
        fastBreakCheckbox = findViewById(R.id.fast_break_checkbox);
        reachSeekBar = findViewById(R.id.reach_seek_bar);
        reachValueText = findViewById(R.id.reach_value_text);
        clickSpeedSeekBar = findViewById(R.id.click_speed_seek_bar);
        clickSpeedValueText = findViewById(R.id.click_speed_value_text);
        keymacroCheckbox = findViewById(R.id.keymacro_checkbox);
        keymacroKeySpinner = findViewById(R.id.keymacro_key_spinner);
        keymacroDelaySeekBar = findViewById(R.id.keymacro_delay_seek_bar);
        keymacroDelayValueText = findViewById(R.id.keymacro_delay_value_text);
        saveButton = findViewById(R.id.save_button);

        String[] renderers = {"VirGL (推荐)", "Zink", "Mobiano"};
        ArrayAdapter<String> rendererAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, renderers);
        rendererSpinner.setAdapter(rendererAdapter);

        String[] keys = {"NONE", "KEY_R", "KEY_T", "KEY_G", "KEY_F", "KEY_X", "KEY_Z"};
        ArrayAdapter<String> keyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, keys);
        keymacroKeySpinner.setAdapter(keyAdapter);

        memorySeekBar.setMax(8);
        memorySeekBar.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
            int memory = (progress + 1) * 512;
            memoryValueText.setText(memory + " MB");
        });

        reachSeekBar.setMax(20);
        reachSeekBar.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
            float reach = 3.0f + progress / 10.0f;
            reachValueText.setText(String.format("%.1f", reach));
        });

        clickSpeedSeekBar.setMax(20);
        clickSpeedSeekBar.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
            float speed = 5.0f + progress;
            clickSpeedValueText.setText(String.format("%.0f", speed));
        });

        keymacroDelaySeekBar.setMax(100);
        keymacroDelaySeekBar.setOnSeekBarChangeListener((seekBar, progress, fromUser) -> {
            keymacroDelayValueText.setText(progress + " ms");
        });

        saveButton.setOnClickListener(v -> saveSettings());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.settings);
        }
    }

    private void loadSettings() {
        String renderer = settingsManager.getRenderer();
        if (renderer.equals("zink")) {
            rendererSpinner.setSelection(1);
        } else if (renderer.equals("mobiano")) {
            rendererSpinner.setSelection(2);
        } else {
            rendererSpinner.setSelection(0);
        }

        int memoryProgress = settingsManager.getMaxMemory() / 512 - 1;
        memorySeekBar.setProgress(Math.max(0, Math.min(8, memoryProgress)));
        memoryValueText.setText(settingsManager.getMaxMemory() + " MB");

        showFPSCheckbox.setChecked(pvpSettings.isShowFPS());
        showPingCheckbox.setChecked(pvpSettings.isShowPing());
        autoJumpCheckbox.setChecked(pvpSettings.isAutoJump());
        fastPlaceCheckbox.setChecked(pvpSettings.isFastPlace());
        fastBreakCheckbox.setChecked(pvpSettings.isFastBreak());

        int reachProgress = (int) ((pvpSettings.getReachDistance() - 3.0f) * 10);
        reachSeekBar.setProgress(Math.max(0, Math.min(20, reachProgress)));
        reachValueText.setText(String.format("%.1f", pvpSettings.getReachDistance()));

        int clickSpeedProgress = (int) (pvpSettings.getClickSpeed() - 5);
        clickSpeedSeekBar.setProgress(Math.max(0, Math.min(20, clickSpeedProgress)));
        clickSpeedValueText.setText(String.format("%.0f", pvpSettings.getClickSpeed()));

        keymacroCheckbox.setChecked(pvpSettings.isKeymacroEnabled());

        String key = pvpSettings.getKeymacroKey();
        String[] keys = {"NONE", "KEY_R", "KEY_T", "KEY_G", "KEY_F", "KEY_X", "KEY_Z"};
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(key)) {
                keymacroKeySpinner.setSelection(i);
                break;
            }
        }

        keymacroDelaySeekBar.setProgress(pvpSettings.getKeymacroDelay());
        keymacroDelayValueText.setText(pvpSettings.getKeymacroDelay() + " ms");
    }

    private void saveSettings() {
        int rendererIndex = rendererSpinner.getSelectedItemPosition();
        switch (rendererIndex) {
            case 1:
                settingsManager.setRenderer("zink");
                break;
            case 2:
                settingsManager.setRenderer("mobiano");
                break;
            default:
                settingsManager.setRenderer("virgl");
                break;
        }

        int memory = (memorySeekBar.getProgress() + 1) * 512;
        settingsManager.setMaxMemory(memory);

        pvpSettings.setShowFPS(showFPSCheckbox.isChecked());
        pvpSettings.setShowPing(showPingCheckbox.isChecked());
        pvpSettings.setAutoJump(autoJumpCheckbox.isChecked());
        pvpSettings.setFastPlace(fastPlaceCheckbox.isChecked());
        pvpSettings.setFastBreak(fastBreakCheckbox.isChecked());

        float reach = 3.0f + reachSeekBar.getProgress() / 10.0f;
        pvpSettings.setReachDistance(reach);

        float clickSpeed = 5.0f + clickSpeedSeekBar.getProgress();
        pvpSettings.setClickSpeed(clickSpeed);

        pvpSettings.setKeymacroEnabled(keymacroCheckbox.isChecked());
        pvpSettings.setKeymacroKey((String) keymacroKeySpinner.getSelectedItem());
        pvpSettings.setKeymacroDelay(keymacroDelaySeekBar.getProgress());

        settingsManager.updatePVPSettings(pvpSettings);

        new AlertDialog.Builder(this)
                .setTitle("设置已保存")
                .setMessage("部分设置需要重启游戏才能生效")
                .setPositiveButton("确定", (dialog, which) -> finish())
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
