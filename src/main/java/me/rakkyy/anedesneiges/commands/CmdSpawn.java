package me.rakkyy.anedesneiges.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;


public class CmdSpawn implements CommandExecutor {

    private boolean log(@NotNull String msg, CommandSender sender) {
        sender.sendMessage("§c[AneDesNeiges] " + msg);
        return false;
    }
    private boolean isInt(@NotNull String s) {
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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // ***********************************************************************
        // Conditions + Arguments

        // Doit être un joueur pour spawn l'ane
        if (!(sender instanceof Player)) return log("Vous devez être un joueur pour executer cette commande !", sender);

        // Minimum 3 arguments: Vie, Coffre, Nom
        if(args.length < 3) return log("Commande: /spawn <vie> <coffre: Oui/Non> <Nom de l'ane>", sender);

        // Si arg0 Nombre, si Arg1 Oui/Non, si arg2 chaacter < 64(?)
        if (!((isInt(args[0]))
                &&
                (args[1].equalsIgnoreCase("Oui") || args[1].equalsIgnoreCase("Non") || args[1].equalsIgnoreCase("True") || args[1].equalsIgnoreCase("False"))
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

        Player player = (Player) sender;
        // ***********************************************************************
        // Spawn

        Location loc = player.getLocation();
        try {
            Entity donkey = loc.getWorld().spawnEntity(loc, EntityType.DONKEY);
            donkey.setCustomNameVisible(true);
            donkey.setCustomName(name.toString());
            ((LivingEntity) donkey).setMaxHealth(health);
            ((LivingEntity) donkey).setHealth(health);
            ((Donkey) donkey).setCarryingChest(coffre);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


}
