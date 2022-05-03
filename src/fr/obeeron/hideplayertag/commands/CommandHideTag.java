package fr.obeeron.hideplayertag.commands;

import fr.obeeron.hideplayertag.HidePlayerTag;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHideTag implements CommandExecutor {

    public static String commandName = "hidetag";

    private final HidePlayerTag hptPlugin;

    public CommandHideTag(HidePlayerTag hptPlugin){
        this.hptPlugin = hptPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // If the command comes from the console, exit silently
        if(!(sender instanceof Player))
            return true;

        // Check for permission
        if(!sender.hasPermission("hideplayertag.hidetag"))
        {
            sender.sendMessage(ChatColor.RED + "You don't have the permission to use this command.");
            return true;
        }

        // Check command validity
        if(args.length > 1)
            return false;

        Player player = (Player) sender;
        boolean hasTagsEnabled = HidePlayerTag.staffTagTeam.getEntries().contains(sender.getName());

        // ./hidetag
        if(args.length == 0){
            // Swap the tags visibility
            setTagsVisibility(player, !hasTagsEnabled);
            return true;
        }

        // ./hidetag true|false
        else if (args[0].equalsIgnoreCase("true")) {
            if(hasTagsEnabled)
                setTagsVisibility(player, false);
            else
                player.sendMessage("Player tags are already hidden.");
        }
        else if (args[0].equalsIgnoreCase("false")) {
            if (!hasTagsEnabled)
                setTagsVisibility(player, true);
            else
                player.sendMessage("Player tags are already visible.");
        }
        else
            return false;

        return true;
    }

    private void setTagsVisibility(Player player, boolean state){
        // See all tags
        if(state)
        {
            hptPlugin.joinStaffTagTeam(player);
            player.sendMessage("Player tags visibility is now : "+ChatColor.BOLD+ChatColor.GREEN+"[Visible]");
        }
        // Hide all tags
        else
        {
            hptPlugin.joinRpTagTeam(player);
            player.sendMessage("Player tags visibility is now : "+ChatColor.BOLD+ChatColor.RED+"[Hidden]");
        }
    }

}
