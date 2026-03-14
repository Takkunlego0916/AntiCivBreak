package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private final AntiCivBreak plugin;
    private final Map<String, String> messages = new HashMap<>();
    private String current = "ja";

    public LanguageManager(AntiCivBreak plugin) {
        this.plugin = plugin;
        loadLanguage();
    }

    public void setLanguage(String lang) {
        if (!lang.equals("ja") && !lang.equals("en")) return;
        this.current = lang;
        loadLanguage();
    }

    public void loadLanguage() {
        messages.clear();
        File file = new File(plugin.getDataFolder(), "lang/messages_" + current + ".yml");
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        for (String key : yml.getKeys(false)) {
            messages.put(key, yml.getString(key));
        }
    }

    public String get(String key) {
        return messages.getOrDefault(key, "§cMISSING: " + key);
    }
}
