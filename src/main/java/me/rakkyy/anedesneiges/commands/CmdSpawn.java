package me.rakkyy.anedesneiges.commands;

import com.destroystokyo.paper.entity.ai.Goal;
import me.rakkyy.anedesneiges.entities.CustomDonkey;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;


public class CmdSpawn implements CommandExecutor {

    private boolean log(String msg, CommandSender sender) {
        sender.sendMessage("§c[AneDesNeiges] " + msg);
        return false;
    }
    private boolean isInt(String s) {
        try { Integer.parseInt(s);
        } catch (Exception e) {return false;}
        return true;
    }

    @Override
    //
    // Commande: /spawn <vie> <coffre: Oui/Non> <nom de l'ane>
    //
    // vie: Integer
    // coffre: String
    // nom de l'ane: String
    //
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // ***********************************************************************
        // Conditions + Arguments

        // Doit être un joueur pour spawn l'ane
        if (!(sender instanceof Player player)) return log("Vous devez être un joueur pour executer cette commande !", sender);

        // Minimum 3 arguments: Vie, Coffre, Nom
        // Si arg0 Nombre, si Arg1 Oui/Non, si arg2 character < 64(?)
        if (args.length < 3 || (!((isInt(args[0]))
                &&
                (args[1].equalsIgnoreCase("Oui") || args[1].equalsIgnoreCase("Non") || args[1].equalsIgnoreCase("True") || args[1].equalsIgnoreCase("False")))
                // &&
                // Character > 64 ?
            )) return log("Commande: /spawn <vie> <coffre: Oui/Non> <Nom de l'ane>", sender);

        // ***********************************************************************
        // Valeurs Gestion

        int health = Integer.parseInt(args[0]);

        boolean coffre;
        coffre = (args[1].equalsIgnoreCase("Oui") || args[1].equalsIgnoreCase("True"));

        StringBuilder name = new StringBuilder();
        int i = 0;
        for (String arg : args) {
            if(i > 2) name.append(" ").append(arg);
            else if(i == 2) name.append(arg);
            i++;
        }
        String customName = name.toString();
        if(customName.contains("&")) customName = customName.replace("&", "§");

        // ***********************************************************************
        // Spawn


        Location loc = player.getLocation();
        try {
            CustomDonkey ane = new CustomDonkey(loc, customName, coffre, health);
            EntityType et = ane.getBukkitEntity().getType();

            ((CraftWorld)loc.getWorld()).getHandle().addFreshEntity(ane, CreatureSpawnEvent.SpawnReason.CUSTOM);
            /*
            donkey.setCustomNameVisible(true);
            donkey.setCustomName(name.toString());
            ((LivingEntity) donkey).setMaxHealth(health);
            ((LivingEntity) donkey).setHealth(health);
            ((Donkey) donkey).setCarryingChest(coffre);
            */
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
