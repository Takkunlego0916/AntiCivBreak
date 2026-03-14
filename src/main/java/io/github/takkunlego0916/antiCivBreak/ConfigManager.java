package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ConfigManager {

    private final AntiCivBreak plugin;

    public ConfigManager(AntiCivBreak plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public boolean isAutoBanEnabled() {
        return plugin.getConfig().getBoolean("autoban.enabled", true);
    }

    public int getMaxBps() {
        return plugin.getConfig().getInt("max-breaks-per-second", 8);
    }

    public int getAutoBanThreshold() {
        return plugin.getConfig().getInt("auto-ban-threshold", 15);
    }

    public String getBanMessage() {
        return plugin.getConfig().getString("ban-message",
                "CivBreakの疑いがあるためBANされました。");
    }

    public int getBanDurationMinutes() {
        return plugin.getConfig().getInt("ban-duration-minutes", 0);
    }

    public boolean isLogEnabled() {
        return plugin.getConfig().getBoolean("log-enabled", true);
    }

    public String getLogFile() {
        return plugin.getConfig().getString("log-file", "break-log.txt");
    }

    public void appendLogToFile(UUID uuid, String msg) {
        if (!isLogEnabled()) return;

        File file = new File(plugin.getDataFolder(), getLogFile());
        file.getParentFile().mkdirs();

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(time + " | " + uuid + " | " + msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
