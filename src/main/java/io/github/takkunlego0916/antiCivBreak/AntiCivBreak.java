package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class AntiCivBreak extends JavaPlugin {

    private boolean pluginEnabled = true;
    private boolean autoBanEnabled = true;
    private String lang = "jp";

    private File logFile;
    private final Map<UUID, List<BreakLog>> logs = new HashMap<>();
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getResource("messages_jp.yml") != null) {
            saveResource("messages_jp.yml", false);
        } else {
            getLogger().warning("messages_jp.yml が見つかりません！フォルダに入っているか確認してください。");
        }

        if (getResource("messages_en.yml") != null) {
            saveResource("messages_en.yml", false);
        } else {
            getLogger().warning("messages_en.yml が見つかりません！フォルダに入っているか確認してください。");
        }


        loadMessages();

        logFile = new File(getDataFolder(), getConfig().getString("log-file", "break-log.txt"));

        Bukkit.getPluginManager().registerEvents(new BreakListener(this), this);
        getCommand("acb").setExecutor(new ACBCommand(this));

        getLogger().info("AntiCivBreak Enabled!");
    }

    public void reloadPlugin() {
        reloadConfig();
        loadMessages();
        autoBanEnabled = getConfig().getBoolean("autoban.enabled", true);
    }

    private void loadMessages() {
        File file = new File(getDataFolder(), "messages_" + lang + ".yml");
        messages = YamlConfiguration.loadConfiguration(file);
    }

    public String getMessage(String key) {
        return messages.getString(key, "§cMISSING: " + key);
    }

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void setPluginEnabled(boolean enabled) {
        this.pluginEnabled = enabled;
    }

    public boolean isAutoBanEnabled() {
        return autoBanEnabled;
    }

    public void setAutoBanEnabled(boolean enabled) {
        this.autoBanEnabled = enabled;
        getConfig().set("autoban.enabled", enabled);
        saveConfig();
    }

    public void setLang(String lang) {
        this.lang = lang;
        loadMessages();
    }

    public void addLog(UUID uuid, String message) {
        logs.putIfAbsent(uuid, new ArrayList<>());
        logs.get(uuid).add(new BreakLog(LocalDateTime.now(), message));

        if (getConfig().getBoolean("log-enabled", true)) {
            try {
                java.nio.file.Files.writeString(
                        logFile.toPath(),
                        "[" + LocalDateTime.now() + "] " + uuid + " - " + message + "\n",
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<BreakLog> getLogs(UUID uuid) {
        return logs.getOrDefault(uuid, new ArrayList<>());
    }

    public static class BreakLog {
        public final LocalDateTime time;
        public final String message;

        public BreakLog(LocalDateTime time, String message) {
            this.time = time;
            this.message = message;
        }
    }
}
