package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.Bukkit;
import org.bukkit.BanList;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BreakListener implements Listener {

    private final AntiCivBreak plugin;
    private final Map<UUID, Integer> breakCount = new HashMap<>();
    private final Map<UUID, Long> lastCheck = new HashMap<>();

    public BreakListener(AntiCivBreak plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!plugin.isPluginEnabled()) return;

        UUID uuid = e.getPlayer().getUniqueId();
        long now = System.currentTimeMillis();

        int maxPerSec = plugin.getConfig().getInt("max-breaks-per-second", 8);
        int autoBanThreshold = plugin.getConfig().getInt("auto-ban-threshold", 15);

        if (lastCheck.containsKey(uuid) && now - lastCheck.get(uuid) < 1000) {
            breakCount.put(uuid, breakCount.getOrDefault(uuid, 0) + 1);
        } else {
            breakCount.put(uuid, 1);
        }

        lastCheck.put(uuid, now);

        // 高速破壊検知ログ
        if (breakCount.get(uuid) > maxPerSec) {
            String msg = "高速破壊検知 (" + breakCount.get(uuid) + "/s)";
            plugin.addLog(uuid, msg);
            e.getPlayer().sendMessage("§c" + msg);
        }

        // 自動BAN処理
        if (plugin.isAutoBanEnabled() && breakCount.get(uuid) >= autoBanThreshold) {

            String reason = plugin.getConfig().getString(
                    "ban-message",
                    "CivBreakの疑いがあるためBANされました。"
            );

            BanList banList = Bukkit.getBanList(BanList.Type.NAME);
            int minutes = plugin.getConfig().getInt("ban-duration-minutes", 0);

            if (minutes <= 0) {
                // ★★★ ここを明示的に Date 型にする（曖昧エラー対策） ★★★
                banList.addBan(
                        e.getPlayer().getName(),
                        reason,
                        (Date) null,
                        "AntiCivBreak"
                );
            } else {
                Date until = new Date(System.currentTimeMillis() + minutes * 60L * 1000L);
                banList.addBan(
                        e.getPlayer().getName(),
                        reason,
                        until,
                        "AntiCivBreak"
                );
            }

            e.getPlayer().kickPlayer(reason);
            plugin.addLog(uuid, "自動BAN実行");
        }
    }
}
