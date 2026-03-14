package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class LogClickBlocker implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().startsWith("ACB Logs")) {
            e.setCancelled(true);
        }
    }
}
