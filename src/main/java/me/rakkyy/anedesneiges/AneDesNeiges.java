package me.rakkyy.anedesneiges;

import org.bukkit.plugin.java.JavaPlugin;

public final class AneDesNeiges extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getServer().getLogger().info("[AneDesNeiges] Plugin AneDesNeiges correctement activé");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
