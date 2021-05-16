package me.ram.bedwarsscoreboardaddon.addon;

import io.github.bedwarsrel.BedwarsRel;
import io.github.bedwarsrel.game.Game;
import me.ram.bedwarsscoreboardaddon.Main;
import me.ram.bedwarsscoreboardaddon.events.BoardAddonPlayerRespawnEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Shears implements Listener {
    HashMap<Player,ItemStack> plist = new HashMap<>();


    @EventHandler
    public void onRespawn(BoardAddonPlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(player);
        if (game == null) {
            return;
        }
        if (!game.isSpectator(player)) {
            if (plist.containsKey(player)){
                giveShears(player);
            }
        }
    }

    @EventHandler
    private void ondrop(PlayerDropItemEvent e){
        if (e.getItemDrop().getItemStack().equals(plist.get(e.getPlayer())))e.setCancelled(true);
    }

    private void giveShears(Player player) {
        player.getInventory().addItem(plist.get(player));
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack[] stacks = player.getInventory().getContents();
                for (int i = 0; i < stacks.length; i++) {
                    ItemStack stack = stacks[i];
                    if (stack == null) {
                        continue;
                    }
                    ItemMeta meta = stack.getItemMeta();
                    if (!meta.hasLore()) {
                        continue;
                    }
                    if (stack.getType() == Material.SHEARS && meta.getLore().contains("§s§h§e§a§r§s")) {
                        if (!plist.containsKey(player)){
                            plist.put(player,stack);
                        }
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }


}
