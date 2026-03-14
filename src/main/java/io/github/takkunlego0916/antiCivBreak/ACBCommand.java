package io.github.takkunlego0916.antiCivBreak;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ACBCommand implements CommandExecutor {

    private final AntiCivBreak plugin;
    private final LogGUI gui;

    public ACBCommand(AntiCivBreak plugin) {
        this.plugin = plugin;
        this.gui = new LogGUI(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("acb.admin")) {
            sender.sendMessage("§c権限がありません。");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§e/acb reload");
            sender.sendMessage("§e/acb autoban on/off");
            sender.sendMessage("§e/acb log <player>");
            sender.sendMessage("§e/acb lang jp/en");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            sender.sendMessage(plugin.getMessage("command.reload"));
            return true;
        }

        if (args[0].equalsIgnoreCase("autoban")) {
            if (args.length < 2) return false;

            if (args[1].equalsIgnoreCase("on")) {
                plugin.setAutoBanEnabled(true);
                sender.sendMessage(plugin.getMessage("command.autoban-on"));
            } else {
                plugin.setAutoBanEnabled(false);
                sender.sendMessage(plugin.getMessage("command.autoban-off"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("log")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("§cプレイヤーのみ使用可能");
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§cプレイヤーが見つかりません");
                return true;
            }
            gui.open(p, target.getUniqueId(), 0);
            return true;
        }

        if (args[0].equalsIgnoreCase("lang")) {
            plugin.setLang(args[1]);
            sender.sendMessage("§aLanguage set to: " + args[1]);
            return true;
        }

        return false;
    }
}
