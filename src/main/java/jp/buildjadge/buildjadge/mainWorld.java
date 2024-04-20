package jp.buildjadge.buildjadge;

import jp.buildjadge.buildjadge.Services.Point;
import jp.buildjadge.buildjadge.Services.PointSystem;
import jp.buildjadge.buildjadge.Services.Timer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class mainWorld implements Listener {
    BuildJadge plugin;
    int locationX = -283;
    int locationY = 4;
    int locationZ = -272;
    Location location, Blueteleport, TPteleport, Winnerteleport;
    World world;
    int count = 0;

    ArrayList<Player> playerList;
    HashMap<Integer, Point> scores = new HashMap<Integer, Point>();
    public int score;
    HashMap<Location,Player> chestLocMap = new HashMap<>();

    int id;
    public mainWorld(BuildJadge plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.world = Bukkit.getWorld("Build-Jadge");
        this.location = new Location(world, locationX, locationY, locationZ);
        this.Blueteleport = new Location(world, -268, 4, 33);
        this.TPteleport = new Location(world, -361,5,11);
        this.playerList = new ArrayList<>();
        this.score = 0;
        this.Winnerteleport = new Location(world,-358,8,8);
        this.id = 1;
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        ItemStack item = new ItemStack(Material.PAPER);
        player.getInventory().addItem(item);

        player.teleport(location);
        player.sendTitle("BuildJadge", "友達の建築を評価しよう!", 20, 40, 20);
        player.sendMessage("BuildJadgeへようこそ!");
        player.sendMessage("荒らし、悪口、政治や宗教に関わる活動、その他公序良俗に反する行為はおやめください。");
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Inventory chestInventory = e.getInventory();
        Player player = (Player) e.getPlayer();
        if(chestInventory.getLocation() == null) return;
        Block block = chestInventory.getLocation().getBlock();
        ItemStack[] items = chestInventory.getContents();
        if (chestInventory.getType() == InventoryType.CHEST) {
            Chest chest = (Chest) block.getState();
            if(chest.getCustomName() == null) return;
            player.sendMessage("投票箱を閉じました");
            player.sendMessage(chestInventory.getType().name());
            player.sendMessage(String.valueOf(e.getInventory().getLocation()));
            Player target = chestLocMap.get(e.getInventory().getLocation());
            if(chest.getCustomName().equals((player.getName()))){
                for (ItemStack item : items) {
                    if (item == null) {
                        continue;
                    }
                    else if (item.getType() == Material.PAPER) {
                        player.sendMessage("投票が完了しました");
                        this.score += 1;
                    }
                }
                scores.put(id,new Point(player.getName(), score));
                PointSystem.setPointList(scores);
                id += 1;
            }
        }

    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (block != null) {
            Material material = block.getType();

            Action action = e.getAction();
            double TeleportX = Blueteleport.getBlockX();

            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (material == Material.OAK_WALL_SIGN) {
                    BlockState state = block.getState();
                    if(state instanceof Sign){
                        Sign sign = (Sign) state;
                        String[] lines = sign.getLines();
                        for(String line:lines){
                            player.sendMessage(line);
                            if(line.equals("参加確認")){
                                if (playerList.contains(player)) {
                                    return;
                                } else {
                                    playerList.add(player);
                                    scores.put(id,new Point(player.getName(),0));
                                    id += 1;
                                    for(Player player1:Bukkit.getOnlinePlayers()){
                                        player1.sendMessage(player.getName() + "さんが参加しました!");
                                    }
                                }
                            } else if (line.equals("開始")) {
                                if (playerList.isEmpty()) {
                                    player.sendMessage("人数が足りないよ!");
                                } else {
                                    player.sendMessage(String.valueOf(playerList));
                                    for (int i = 0; i < playerList.size(); i++) {
                                        TeleportX += -23 * i;
                                        Location locationX = Blueteleport.clone().add(TeleportX, 0, 0) ;
                                        Blueteleport.setX(TeleportX);
                                        playerList.get(i).teleport(Blueteleport);
                                        playerList.get(i).sendMessage(String.valueOf(Blueteleport));
                                        playerList.get(i).sendTitle("建築区画", "自分の範囲で建築してね!", 20, 40, 20);
                                        playerList.get(i).setGameMode(GameMode.CREATIVE);
                                        Timer.setTimer(playerList.get(i),this.plugin,600,TPteleport);
                                        ItemStack lobbyitem = new ItemStack(Material.STICK);
                                        ItemMeta itemMeta = lobbyitem.getItemMeta();
                                        if(itemMeta == null) return;
                                        itemMeta.setDisplayName("ロビーへ戻る");
                                        lobbyitem.setItemMeta(itemMeta);
                                        playerList.get(i).getInventory().addItem(lobbyitem);
                                        ItemStack touhyou = new ItemStack(Material.CHEST);
                                        ItemMeta itemMeta1 = touhyou.getItemMeta();
                                        if(itemMeta1 == null) return;
                                        itemMeta1.setDisplayName(playerList.get(i).getName());
                                        touhyou.setItemMeta(itemMeta1);
                                        playerList.get(i).getInventory().addItem(touhyou);
                                    }
                                    playerList.clear();
                                }
                            }

                        }
                    }
                }
            }
        }
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Material material = e.getMaterial();
            player.sendMessage("移動クリック");
            if(material.equals(Material.COMPASS)){
                player.teleport(location);
                player.sendTitle("BuildJadge", "おかえりなさい!", 20, 40, 20);
            }
        }


    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location location = block.getLocation();
        Chest chest = (Chest)block.getState();
        Material material = e.getBlockPlaced().getType();
        Block underblock = location.add(0,-1,0).getBlock();
        player.sendMessage(underblock.getType().name());
        if (material == Material.CHEST) {
            for(Player player1:Bukkit.getOnlinePlayers()) {
                player1.sendMessage(player.getName() + "さんの建築が終了しました");
            }
            chest.setCustomName(player.getName());
            chest.update();
        }
    for(int id:scores.keySet()) {
        player.sendMessage(String.valueOf(id));
        if (id != 1 && underblock.getType()==Material.BLUE_CONCRETE) {
            e.setCancelled(true);
        }
        if(id != 2 && underblock.getType()==Material.RED_CONCRETE){
            e.setCancelled(true);
        }
    }


    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Location lo = block.getLocation();
        World world = e.getBlock().getWorld();
        lo.setY(15);
        if(e.getPlayer().getWorld().getBlockAt(lo).getType() != Material.AIR){
            e.getPlayer().sendMessage("壁を壊さないでね!");
            e.setCancelled(true);
        }
        scores.put(1,new Point("tofu",0));
        scores.put(2,new Point("Mark",100));
        scores.put(3,new Point("ToyPro",50));
        e.getPlayer().sendMessage("順位は"+PointSystem.getTopPlayers()+"です");
//        Player player = Bukkit.getPlayer(PointSystem.getTopPlayer());
//        if (player == null)return;
//        e.getPlayer().sendMessage("1位は"+PointSystem.getTopPlayer()+"です");
//        player.teleport(Winnerteleport);

    }




}
