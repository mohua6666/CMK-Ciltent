package com.mk.pvp.client.data.repository;

import android.content.Context;
import android.os.Environment;

import com.mk.pvp.client.data.model.GameSave;
import com.mk.pvp.client.data.model.GameVersion;
import com.mk.pvp.client.data.model.KeyMapping;
import com.mk.pvp.client.data.model.ModInfo;
import com.mk.pvp.client.data.model.PVPClient;
import com.mk.pvp.client.data.model.ResourcePack;
import com.mk.pvp.client.data.model.Skin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileRepository {
    private static FileRepository instance;
    private Context context;
    private Gson gson;

    private static final String BASE_DIR = "MKPVPClient";
    private static final String MINECRAFT_DIR = ".minecraft";
    private static final String VERSIONS_DIR = "versions";
    private static final String MODS_DIR = "mods";
    private static final String RESOURCE_PACKS_DIR = "resourcepacks";
    private static final String SAVES_DIR = "saves";
    private static final String SKINS_DIR = "skins";
    private static final String CONFIG_DIR = "config";
    private static final String KEY_MAPPING_FILE = "keymapping.json";
    private static final String SKINS_FILE = "skins.json";

    private FileRepository(Context context) {
        this.context = context.getApplicationContext();
        this.gson = new Gson();
        initializeDirectories();
    }

    public static synchronized FileRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FileRepository(context);
        }
        return instance;
    }

    private void initializeDirectories() {
        File baseDir = new File(getExternalStoragePath());
        File minecraftDir = new File(baseDir, MINECRAFT_DIR);
        File versionsDir = new File(minecraftDir, VERSIONS_DIR);
        File modsDir = new File(minecraftDir, MODS_DIR);
        File resourcePacksDir = new File(minecraftDir, RESOURCE_PACKS_DIR);
        File savesDir = new File(minecraftDir, SAVES_DIR);
        File skinsDir = new File(baseDir, SKINS_DIR);
        File configDir = new File(baseDir, CONFIG_DIR);

        createDirIfNotExists(minecraftDir);
        createDirIfNotExists(versionsDir);
        createDirIfNotExists(modsDir);
        createDirIfNotExists(resourcePacksDir);
        createDirIfNotExists(savesDir);
        createDirIfNotExists(skinsDir);
        createDirIfNotExists(configDir);
    }

    private void createDirIfNotExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public String getExternalStoragePath() {
        File extStorage = Environment.getExternalStorageDirectory();
        return new File(extStorage, BASE_DIR).getAbsolutePath();
    }

    public String getMinecraftPath() {
        return new File(getExternalStoragePath(), MINECRAFT_DIR).getAbsolutePath();
    }

    public String getVersionsPath() {
        return new File(getMinecraftPath(), VERSIONS_DIR).getAbsolutePath();
    }

    public String getModsPath() {
        return new File(getMinecraftPath(), MODS_DIR).getAbsolutePath();
    }

    public String getResourcePacksPath() {
        return new File(getMinecraftPath(), RESOURCE_PACKS_DIR).getAbsolutePath();
    }

    public String getSavesPath() {
        return new File(getMinecraftPath(), SAVES_DIR).getAbsolutePath();
    }

    public String getSkinsPath() {
        return new File(getExternalStoragePath(), SKINS_DIR).getAbsolutePath();
    }

    public String getConfigPath() {
        return new File(getExternalStoragePath(), CONFIG_DIR).getAbsolutePath();
    }

    public List<GameVersion> getInstalledVersions() {
        List<GameVersion> versions = new ArrayList<>();
        File versionsDir = new File(getVersionsPath());
        
        if (versionsDir.exists() && versionsDir.isDirectory()) {
            File[] versionFolders = versionsDir.listFiles(File::isDirectory);
            if (versionFolders != null) {
                Arrays.sort(versionFolders, Comparator.comparingLong(File::lastModified).reversed());
                
                for (File folder : versionFolders) {
                    File jarFile = new File(folder, folder.getName() + ".jar");
                    File jsonFile = new File(folder, folder.getName() + ".json");
                    
                    if (jarFile.exists() || jsonFile.exists()) {
                        GameVersion version = new GameVersion();
                        version.setId(folder.getName());
                        version.setName(folder.getName());
                        version.setPath(folder.getAbsolutePath());
                        version.setInstalled(true);
                        version.setSize(getFolderSize(folder));
                        versions.add(version);
                    }
                }
            }
        }
        return versions;
    }

    public List<ModInfo> getInstalledMods() {
        List<ModInfo> mods = new ArrayList<>();
        File modsDir = new File(getModsPath());
        
        if (modsDir.exists() && modsDir.isDirectory()) {
            File[] modFiles = modsDir.listFiles((dir, name) -> name.endsWith(".jar") || name.endsWith(".disabled"));
            if (modFiles != null) {
                for (File file : modFiles) {
                    ModInfo mod = new ModInfo();
                    mod.setName(removeExtension(file.getName()));
                    mod.setFileName(file.getName());
                    mod.setFilePath(file.getAbsolutePath());
                    mod.setFileSize(file.length());
                    mod.setEnabled(!file.getName().endsWith(".disabled"));
                    mods.add(mod);
                }
            }
        }
        return mods;
    }

    public List<ResourcePack> getInstalledResourcePacks() {
        List<ResourcePack> resourcePacks = new ArrayList<>();
        File resourcePacksDir = new File(getResourcePacksPath());
        
        if (resourcePacksDir.exists() && resourcePacksDir.isDirectory()) {
            File[] packFiles = resourcePacksDir.listFiles((dir, name) -> name.endsWith(".zip") || name.endsWith(".png"));
            if (packFiles != null) {
                for (File file : packFiles) {
                    ResourcePack pack = new ResourcePack();
                    pack.setName(removeExtension(file.getName()));
                    pack.setFileName(file.getName());
                    pack.setFilePath(file.getAbsolutePath());
                    pack.setFileSize(file.length());
                    resourcePacks.add(pack);
                }
            }
        }
        return resourcePacks;
    }

    public List<GameSave> getGameSaves() {
        List<GameSave> saves = new ArrayList<>();
        File savesDir = new File(getSavesPath());
        
        if (savesDir.exists() && savesDir.isDirectory()) {
            File[] saveFolders = savesDir.listFiles(File::isDirectory);
            if (saveFolders != null) {
                for (File folder : saveFolders) {
                    File iconFile = new File(folder, "icon.png");
                    GameSave save = new GameSave();
                    save.setName(folder.getName());
                    save.setFolderName(folder.getName());
                    save.setFilePath(folder.getAbsolutePath());
                    save.setSize(getFolderSize(folder));
                    save.setLastModified(folder.lastModified());
                    save.setHasIcon(iconFile.exists());
                    saves.add(save);
                }
            }
        }
        return saves;
    }

    public List<Skin> getSkins() {
        List<Skin> skins = new ArrayList<>();
        File configDir = new File(getConfigPath());
        File skinsFile = new File(configDir, SKINS_FILE);
        
        if (skinsFile.exists()) {
            try {
                String json = readFileToString(skinsFile);
                Type listType = new TypeToken<List<Skin>>(){}.getType();
                skins = gson.fromJson(json, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        File skinsDir = new File(getSkinsPath());
        if (skinsDir.exists() && skinsDir.isDirectory()) {
            File[] skinFiles = skinsDir.listFiles((dir, name) -> name.endsWith(".png"));
            if (skinFiles != null) {
                for (File file : skinFiles) {
                    boolean exists = false;
                    for (Skin skin : skins) {
                        if (skin.getName().equals(removeExtension(file.getName()))) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        Skin skin = new Skin();
                        skin.setName(removeExtension(file.getName()));
                        skin.setPath(file.getAbsolutePath());
                        skin.setCustom(true);
                        skins.add(skin);
                    }
                }
            }
        }
        
        return skins != null ? skins : new ArrayList<>();
    }

    public void saveSkins(List<Skin> skins) {
        try {
            File skinsFile = new File(getConfigPath(), SKINS_FILE);
            String json = gson.toJson(skins);
            writeStringToFile(skinsFile, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSkin(Skin skin) {
        List<Skin> skins = getSkins();
        skins.add(skin);
        saveSkins(skins);
    }

    public void removeSkin(String skinId) {
        List<Skin> skins = getSkins();
        skins.removeIf(skin -> skin.getId().equals(skinId));
        saveSkins(skins);
    }

    public boolean installMod(File sourceFile) {
        try {
            File destFile = new File(getModsPath(), sourceFile.getName());
            copyFile(sourceFile, destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMod(ModInfo mod) {
        File modFile = new File(mod.getFilePath());
        return modFile.delete();
    }

    public boolean toggleMod(ModInfo mod, boolean enable) {
        File modFile = new File(mod.getFilePath());
        File newFile;
        
        if (enable) {
            newFile = new File(getModsPath(), mod.getFileName().replace(".disabled", ""));
        } else {
            newFile = new File(getModsPath(), mod.getFileName() + ".disabled");
        }
        
        return modFile.renameTo(newFile);
    }

    public boolean installResourcePack(File sourceFile) {
        try {
            File destFile = new File(getResourcePacksPath(), sourceFile.getName());
            copyFile(sourceFile, destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteResourcePack(ResourcePack pack) {
        File packFile = new File(pack.getFilePath());
        return packFile.delete();
    }

    public boolean backupSave(GameSave save, String backupPath) {
        try {
            File sourceDir = new File(save.getFilePath());
            File destDir = new File(backupPath, save.getFolderName() + "_backup");
            copyDirectory(sourceDir, destDir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveSkinImage(File sourceFile, String skinName) {
        try {
            File destFile = new File(getSkinsPath(), skinName + ".png");
            copyFile(sourceFile, destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<KeyMapping> getSavedKeyMappings() {
        List<KeyMapping> mappings = new ArrayList<>();
        File configDir = new File(getConfigPath());
        File keyMappingFile = new File(configDir, KEY_MAPPING_FILE);
        
        if (keyMappingFile.exists()) {
            try {
                String json = readFileToString(keyMappingFile);
                Type listType = new TypeToken<List<KeyMapping>>(){}.getType();
                mappings = gson.fromJson(json, listType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return mappings != null ? mappings : new ArrayList<>();
    }

    public boolean saveKeyMappings(List<KeyMapping> mappings) {
        try {
            File keyMappingFile = new File(getConfigPath(), KEY_MAPPING_FILE);
            String json = gson.toJson(mappings);
            writeStringToFile(keyMappingFile, json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveKeyMapping(KeyMapping mapping) {
        List<KeyMapping> mappings = getSavedKeyMappings();
        
        int index = -1;
        for (int i = 0; i < mappings.size(); i++) {
            if (mappings.get(i).getName().equals(mapping.getName())) {
                index = i;
                break;
            }
        }
        
        if (index >= 0) {
            mappings.set(index, mapping);
        } else {
            mappings.add(mapping);
        }
        
        saveKeyMappings(mappings);
    }

    public List<PVPClient> getDefaultPVPClients() {
        List<PVPClient> clients = new ArrayList<>();
        
        clients.add(createPVPClient("moon", "Moon Client", "顶级PVP客户端，支持多种游戏优化和性能增强", "https://moonclient.com/download"));
        clients.add(createPVPClient("lion", "Lion Client", "专为Hypixel设计的PVP客户端，优化网络延迟", "https://lionclient.dev/download"));
        clients.add(createPVPClient("cmpack", "CM Pack", "CM团队出品，优化性能与画质，PVP专用", "https://cmpack.net/download"));
        clients.add(createPVPClient("badlion", "Badlion Client", "老牌PVP客户端，支持多种mod和优化", "https://client.badlion.net/download"));
        clients.add(createPVPClient("labymod", "LabyMod", "功能丰富的客户端，支持社交和PVP功能", "https://labymod.net/download"));
        clients.add(createPVPClient("feather", "Feather Client", "轻量级高性能客户端，专注PVP体验", "https://featherclient.com/download"));
        clients.add(createPVPClient("vanilla", "原版优化", "基于原版的轻量优化，纯净PVP体验", "https://minecraft.net"));
        
        return clients;
    }

    private PVPClient createPVPClient(String id, String name, String description, String url) {
        PVPClient client = new PVPClient(id, name, description, url);
        client.setRecommended(id.equals("moon") || id.equals("cmpack"));
        return client;
    }

    private void copyFile(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source);
             OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }

    private void copyDirectory(File source, File dest) throws IOException {
        if (!dest.exists()) {
            dest.mkdirs();
        }
        
        File[] children = source.listFiles();
        if (children != null) {
            for (File child : children) {
                File sourceChild = new File(source, child.getName());
                File destChild = new File(dest, child.getName());
                
                if (child.isDirectory()) {
                    copyDirectory(sourceChild, destChild);
                } else {
                    copyFile(sourceChild, destChild);
                }
            }
        }
    }

    private long getFolderSize(File folder) {
        long size = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += getFolderSize(file);
                }
            }
        }
        return size;
    }

    private String removeExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(0, lastDot);
        }
        return filename;
    }

    private String readFileToString(File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private void writeStringToFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }
}
