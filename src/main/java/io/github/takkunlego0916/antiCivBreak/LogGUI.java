package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class LogGUI implements Listener {

    private final AntiCivBreak plugin;

    public LogGUI(AntiCivBreak plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open(Player p, UUID target, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "ACB Logs - Page " + (page + 1));

        List<AntiCivBreak.BreakLog> logs = plugin.getLogs(target);

        int start = page * 45;
        for (int i = 0; i < 45; i++) {
            int idx = start + i;
            if (idx >= logs.size()) break;

            AntiCivBreak.BreakLog log = logs.get(idx);

            ItemStack paper = new ItemStack(Material.PAPER);
            ItemMeta meta = paper.getItemMeta();
            meta.setDisplayName("§e" + log.time.toString());
            meta.setLore(List.of("§f" + log.message));
            paper.setItemMeta(meta);

            inv.setItem(i, paper);
        }

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nm = next.getItemMeta();
        nm.setDisplayName("§a次のページ");
        next.setItemMeta(nm);
        inv.setItem(53, next);

        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith("ACB Logs")) {
            e.setCancelled(true);
        }
    }
}
