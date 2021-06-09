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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

//todo
public class Toolsupgrades implements Listener {

    HashMap<Player, Integer> pickaxeList = new HashMap<>();
    HashMap<Player, Integer> axeList = new HashMap<>();
    HashMap<Integer,ItemStack> pickaxeItem = new HashMap<>();
    HashMap<Integer,ItemStack> axeItem = new HashMap<>();



    @EventHandler
    public void onRespawn(BoardAddonPlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Game game = BedwarsRel.getInstance().getGameManager().getGameOfPlayer(player);
        if (game == null) {
            return;
        }
        if (!game.isSpectator(player)) {
//            if (plist.containsKey(player)){
//                giveShears(player);
//            }
        }
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
                    if (stack.getType().name().toLowerCase().contains("pickaxe")&& meta.getLore().contains("§p§i§c§k§a§x§e§")) {
                        int i1 = getpickaxeLevel(meta.getLore());
                        if (pickaxeList.containsKey(player)){
                            int integer = pickaxeList.get(player);
                            if (i1>integer){
                                player.getInventory().remove(pickaxeItem.get(integer));
                                pickaxeList.put(player,i1);

                            }
                        }else{
                            pickaxeList.put(player,i1);
                        }
                        pickaxeItem.put(i1,stack);
                    }
                }
            }
        }.runTaskLater(Main.getInstance(), 1L);
    }

    private int getpickaxeLevel(List<String> lore) {
        for (String s:lore){
            if (s.contains("§p§i§c§k§a§x§e§")){
                int level = Integer.parseInt(s.split("§p§i§c§k§a§x§e§")[1]);
                return level;
            }
        }
        return 0;
    }


}
