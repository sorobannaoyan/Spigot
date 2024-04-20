package jp.buildjadge.buildjadge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuildJadge extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Hello World!");
        new mainWorld(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
