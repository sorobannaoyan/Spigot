package jp.buildjadge.buildjadge.Services;

import jp.buildjadge.buildjadge.BuildJadge;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    public static void setTimer(Player player, BuildJadge plugin, int timerSecond, Location location) {

        new BukkitRunnable() {
            int timeLeft = timerSecond;

            @Override
            public void run() {
                if (timeLeft == 0) {//残りの時間がゼロの時
                    player.sendMessage("Time UP!!");
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(location);
                    cancel();//中止する
                    return;
                }
                if (timeLeft == timerSecond/2 || timeLeft == timerSecond/4) {
                    player.sendMessage("残り" + String.valueOf(timeLeft) + "秒");
                }
                if (timeLeft <= 10){
                    player.sendMessage(ChatColor.DARK_RED + "残り" + String.valueOf(timeLeft) + "秒");
                }
                timeLeft--;//残り時間から１引く
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
