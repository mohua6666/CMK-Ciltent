package com.mk.pvp.client.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.mk.pvp.client.data.model.Account;
import com.mk.pvp.client.data.model.AppSettings;
import com.mk.pvp.client.data.model.PVPSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    private static final String PREF_NAME = "mk_pvp_settings";
    private static final String KEY_APP_SETTINGS = "app_settings";
    private static final String KEY_ACCOUNTS = "accounts";
    private static final String KEY_PVP_SETTINGS = "pvp_settings";
    private static final String KEY_SELECTED_ACCOUNT = "selected_account";
    
    private static SettingsManager instance;
    private SharedPreferences prefs;
    private Gson gson;
    
    private AppSettings appSettings;
    private List<Account> accounts;
    private PVPSettings pvpSettings;
    private String selectedAccountId;

    private SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadAllSettings();
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context.getApplicationContext());
        }
        return instance;
    }

    private void loadAllSettings() {
        loadAppSettings();
        loadAccounts();
        loadPVPSettings();
        loadSelectedAccount();
    }

    private void loadAppSettings() {
        String json = prefs.getString(KEY_APP_SETTINGS, null);
        if (json != null) {
            appSettings = gson.fromJson(json, AppSettings.class);
        } else {
            appSettings = new AppSettings();
        }
    }

    private void loadAccounts() {
        String json = prefs.getString(KEY_ACCOUNTS, null);
        if (json != null) {
            Type listType = new TypeToken<List<Account>>(){}.getType();
            accounts = gson.fromJson(json, listType);
        } else {
            accounts = new ArrayList<>();
        }
    }

    private void loadPVPSettings() {
        String json = prefs.getString(KEY_PVP_SETTINGS, null);
        if (json != null) {
            pvpSettings = gson.fromJson(json, PVPSettings.class);
        } else {
            pvpSettings = new PVPSettings();
        }
    }

    private void loadSelectedAccount() {
        selectedAccountId = prefs.getString(KEY_SELECTED_ACCOUNT, null);
    }

    public void saveAppSettings() {
        String json = gson.toJson(appSettings);
        prefs.edit().putString(KEY_APP_SETTINGS, json).apply();
    }

    public void saveAccounts() {
        String json = gson.toJson(accounts);
        prefs.edit().putString(KEY_ACCOUNTS, json).apply();
    }

    public void savePVPSettings() {
        String json = gson.toJson(pvpSettings);
        prefs.edit().putString(KEY_PVP_SETTINGS, json).apply();
    }

    public void saveSelectedAccount(String accountId) {
        selectedAccountId = accountId;
        prefs.edit().putString(KEY_SELECTED_ACCOUNT, accountId).apply();
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    public void updateAppSettings(AppSettings newSettings) {
        this.appSettings = newSettings;
        saveAppSettings();
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        saveAccounts();
    }

    public void removeAccount(String accountId) {
        accounts.removeIf(account -> account.getId().equals(accountId));
        saveAccounts();
    }

    public void updateAccount(Account updatedAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getId().equals(updatedAccount.getId())) {
                accounts.set(i, updatedAccount);
                saveAccounts();
                return;
            }
        }
    }

    public Account getSelectedAccount() {
        for (Account account : accounts) {
            if (account.getId().equals(selectedAccountId)) {
                return account;
            }
        }
        return accounts.isEmpty() ? null : accounts.get(0);
    }

    public void selectAccount(String accountId) {
        saveSelectedAccount(accountId);
        for (Account account : accounts) {
            account.setSelected(account.getId().equals(accountId));
        }
        saveAccounts();
    }

    public PVPSettings getPvpSettings() {
        return pvpSettings;
    }

    public void updatePVPSettings(PVPSettings newSettings) {
        this.pvpSettings = newSettings;
        savePVPSettings();
    }

    public String getTheme() {
        return appSettings.getTheme();
    }

    public void setTheme(String theme) {
        appSettings.setTheme(theme);
        saveAppSettings();
    }

    public int getMaxMemory() {
        return appSettings.getMaxMemory();
    }

    public void setMaxMemory(int memory) {
        appSettings.setMaxMemory(memory);
        saveAppSettings();
    }

    public String getRenderer() {
        return appSettings.getRenderer();
    }

    public void setRenderer(String renderer) {
        appSettings.setRenderer(renderer);
        saveAppSettings();
    }

    public String getSelectedVersion() {
        return appSettings.getSelectedVersion();
    }

    public void setSelectedVersion(String version) {
        appSettings.setSelectedVersion(version);
        saveAppSettings();
    }
}
