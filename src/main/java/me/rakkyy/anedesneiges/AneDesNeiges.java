package me.rakkyy.anedesneiges;

import me.rakkyy.anedesneiges.commands.CmdSpawn;
import org.bukkit.plugin.java.JavaPlugin;

public final class AneDesNeiges extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getLogger().info("[AneDesNeiges] Plugin AneDesNeiges correctement activé");

        // Commande: /spawn <vie> <coffre> <nom de l'ane>
        // Passage par commande de base de Spigot, pas besoin de faire un Manager
        this.getCommand("spawn").setExecutor(new CmdSpawn());
    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
        getServer().getLogger().info("[AneDesNeiges] Plugin AneDesNeiges correctement désactivé");

    }
}
