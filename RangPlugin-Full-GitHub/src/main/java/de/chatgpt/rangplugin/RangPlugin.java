
package de.chatgpt.rangplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RangPlugin extends JavaPlugin implements Listener {

    private Map<String, String> ranks;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadRanks();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("RangPlugin aktiviert.");
    }

    private void loadRanks() {
        ranks = new HashMap<>();
        if (getConfig().getConfigurationSection("ranks") != null) {
            for (String key : getConfig().getConfigurationSection("ranks").getKeys(false)) {
                ranks.put(key, getConfig().getString("ranks." + key + ".prefix"));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ränge")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("Nur Spieler können dieses Menü öffnen.");
                return true;
            }
            if (!p.hasPermission("rang.menu")) {
                p.sendMessage("§cKeine Berechtigung!");
                return true;
            }
            openMainMenu(p);
            return true;
        }
        return false;
    }

    private void openMainMenu(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6Rang-Menü");

        inv.setItem(11, createItem(Material.NAME_TAG, "§aRang erstellen"));
        inv.setItem(13, createItem(Material.PLAYER_HEAD, "§eRang vergeben"));
        inv.setItem(15, createItem(Material.BOOK, "§bRänge bearbeiten"));

        p.openInventory(inv);
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("§6Rang-Menü")) {
            e.setCancelled(true);
        }
    }
}
